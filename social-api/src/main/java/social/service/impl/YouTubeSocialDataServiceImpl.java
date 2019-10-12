package social.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.geekbeacon.social.db.models.config.tables.records.SocialAppRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import social.model.AuthResponse;
import social.model.SocialType;
import social.repository.SocialRepository;
import social.service.SocialDataService;

import java.util.Map;
import java.util.Set;

@Service
@Qualifier("youtube")
@Log4j2
public class YouTubeSocialDataServiceImpl implements SocialDataService {
    private static final String NIXIEPIXEL = "NixiePixel";
    private static final String OSALT = "nixiedoeslinux";
    public static final String CHANNEL_COUNT_URL = "https://www.googleapis.com/youtube/v3/channels";
    private final Set<String> channels = ImmutableSet.of(NIXIEPIXEL, OSALT);
    private final Map<String, Integer> defaults = ImmutableMap.of(NIXIEPIXEL, 221000, OSALT, 109000);

    private final SocialType socialType = SocialType.YOUTUBE;
    private final HttpClient client;
    @Getter
    private boolean enabled;

    private String apiKey;

    @Autowired
    public YouTubeSocialDataServiceImpl(@Value("${social.youtube.enabled:true}") boolean enabled,
        SocialRepository socialRepository) {
        this.enabled = enabled;
        SocialAppRecord record = null;
        if(enabled) {
            record =socialRepository.getSocialAppRecord(socialType);
        }
        if (enabled && record != null) {
            apiKey = record.getClientId();
            this.client = HttpClients.custom().build();
        } else {
            this.client = null;
        }
    }


    /**
     * @see SocialDataService#getCount()
     */
    @Override
    public int getCount() {
        return channels.stream().mapToInt(this::getChannelCount).sum();
    }

    private int getChannelCount(String channelName) {
        if(!isEnabled()) {
            return ObjectUtils.defaultIfNull(defaults.get(channelName), 0);
        }
        try {
            HttpUriRequest request = RequestBuilder.get()
                .setUri(CHANNEL_COUNT_URL)
                .addParameter("part", "id,statistics")
                .addParameter("forUsername", channelName)
                .addParameter("key", apiKey)
                .build();

            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Invalid Status Code");
            }
            int followers = NumberUtils.toInt(JsonPath.read(responseBody, "$.items[0].statistics.subscriberCount"));
            log.debug("The channel: {} has {} followers.", channelName, followers);
            return followers;
        } catch (Exception e) {
            log.error("Failed to retrieve data, falling back on defaults", e);
            return ObjectUtils.defaultIfNull(defaults.get(channelName), 0);
        }

    }

    @Override
    public AuthResponse authorize() {
        throw new NotImplementedException("Not supported");
    }

    @Override
    public void saveRequestToken(String token, String verifier) {
        throw new NotImplementedException("Not supported");
    }
}
