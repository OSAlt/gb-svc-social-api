package org.geekbeacon.social.service;

import com.google.common.collect.Maps;
import org.geekbeacon.social.model.SocialActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.geekbeacon.social.model.AuthResponse;
import org.geekbeacon.social.model.SocialType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class SocialDataAdapter {

    Map<SocialType, SocialDataService> serviceMap = Maps.newHashMap();

    @Autowired
    public SocialDataAdapter(
        @Qualifier("twitter") SocialDataService twitterService,
        @Qualifier("FB") SocialDataService fbService,
        @Qualifier("youtube") SocialDataService youtube,
        @Qualifier("instagram") SocialDataService instagram,
        @Qualifier("twitch") SocialDataService twitch,
        @Qualifier("discord") SocialDataService discord) {
        serviceMap.put(SocialType.TWITTER, twitterService);
        serviceMap.put(SocialType.FACEBOOK, fbService);
        serviceMap.put(SocialType.YOUTUBE, youtube);
        serviceMap.put(SocialType.INSTAGRAM, instagram);
        serviceMap.put(SocialType.TWITCH, twitch);
        serviceMap.put(SocialType.DISCORD, discord);
    }

    @Cacheable(value = "socialData", cacheManager= "cacheManager")
    public List<SocialActivity> getActivity(SocialType type, int limit) {
        return getService(type).getSocialActivity(limit);
    }

    private SocialDataService getService(SocialType socialType) {
        if (serviceMap.containsKey(socialType)) {
            return serviceMap.get(socialType);
        } else {
            throw new RuntimeException("Unsupported social media type: " + socialType.name());
        }
    }

    public AuthResponse authorize(SocialType type) {
        return getService(type).authorizeApplication();
    }


    public void saveRequestToken(SocialType type, String token, String verifier) {
        getService(type).saveRequestToken(token, verifier);
    }

    @Cacheable(value = "durable", cacheManager= "durableCache")
    public int getCount(SocialType type) {
        if(SocialType.AGGREGATE.equals(type)) {
            return
                Arrays.stream(SocialType.values())
                    .filter(t -> !t.equals(SocialType.AGGREGATE))
                    .map(this::getService).mapToInt(SocialDataService::getFollowerCount).sum();

        }
        return getService(type).getFollowerCount();

    }
}
