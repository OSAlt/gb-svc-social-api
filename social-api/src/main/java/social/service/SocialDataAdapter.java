package social.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import social.model.AuthResponse;
import social.model.SocialType;

import java.util.Map;

@Component
public class SocialDataAdapter {

    Map<SocialType, SocialDataService> serviceMap = Maps.newHashMap();

    @Autowired
    public SocialDataAdapter(@Qualifier("twitter") SocialDataService twitterService) {
        serviceMap.put(SocialType.TWITTER, twitterService);
    }

    private SocialDataService getService(SocialType socialType) {
        if (serviceMap.containsKey(socialType)) {
            return serviceMap.get(socialType);
        } else {
            throw new RuntimeException("Unsupported social media type: " + socialType.name());
        }
    }

    public AuthResponse authorize(SocialType type) {
        return getService(type).authorize();
    }


    public void saveRequestToken(SocialType type, String token, String verifier) {
        getService(type).saveRequestToken(token, verifier);
    }

    public int getCount(SocialType type) {
        return getService(type).getCount();

    }
}
