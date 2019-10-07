package social.service.impl;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
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

import javax.validation.constraints.NotNull;
import java.io.IOException;

@Qualifier("twitch")
@Service
@Log4j2
public class TwitchSocialDataServiceImpl implements SocialDataService {
    private static final String CLIENT_ID_HEADER = "Client-ID";
    public static final String TWITCH_URL_COUNT = "https://api.twitch.tv/helix/users/follows";
    public static final String TWITCH_URL_GET_USER_ID = "https://api.twitch.tv/helix/users";
    private final String userId;
    private final HttpClient client;

    private final int DEFAULT_FALL_BACK = 2038;
    private final Header header;
    private String GB_CHANNEL;


    private final SocialType socialType = SocialType.TWITCH;
    @Getter
    private boolean enabled;

    @Autowired
    public TwitchSocialDataServiceImpl(SocialRepository socialRepository,
                                       @Value("${social.twitch.enabled:true}") boolean enabled,
                                       @Value("${social.twitch.user_name:nixiepixel}") String userName ) {

        SocialAppRecord record = null;
        if(enabled) {
            record =socialRepository.getSocialAppRecord(socialType);
        }
        if (!enabled || record == null) {
            enabled = false;
            client = null;
            header = null;
            userId = null;
        } else {
            GB_CHANNEL = userName;
            header = new BasicHeader(CLIENT_ID_HEADER, record.getClientId());
            client = HttpClients.custom().setDefaultHeaders(Lists.newArrayList(header)).build();
            userId = getUserId(GB_CHANNEL);
            enabled = userId != null;
        }
    }

    /**
     * @see SocialDataService#getCount()
     */

    @Override
    public int getCount() {
        if (!isEnabled()) {
            return DEFAULT_FALL_BACK;
        }

        HttpUriRequest request = RequestBuilder.get()
            .setUri(TWITCH_URL_COUNT)
            .addParameter("to_id", userId)
            .build();

        try {
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                return DEFAULT_FALL_BACK;
            }
            int cnt = JsonPath.read(responseBody, "$.total");
            return cnt;
        } catch (IOException e) {
            log.error("Could not retrieve followers of channel  for login: {}", userId, e);
            return DEFAULT_FALL_BACK;
        }
    }

    @Override
    public AuthResponse authorize() {
        throw new RuntimeException("Unsupported feature");
    }

    @Override
    public void saveRequestToken(String token, String verifier) {
        throw new RuntimeException("Unsupported feature");
    }


    private String getUserId(@NotNull String name) {
        HttpUriRequest request = RequestBuilder.get()
            .setUri(TWITCH_URL_GET_USER_ID)
            .addParameter("login", name)
            .build();
        try {
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            return JsonPath.read(responseBody, "$.data[0].id");
        } catch (IOException e) {
            log.error("Could not retrieve user id for login: {}", name, e);

        }

        return null;
    }
}
