package org.geekbeacon.social.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.jayway.jsonpath.JsonPath;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.geekbeacon.social.db.models.config.tables.records.SocialAppRecord;
import org.geekbeacon.social.db.models.config.tables.records.UserSocialRecord;
import org.geekbeacon.social.model.SocialActivity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.geekbeacon.social.model.AuthResponse;
import org.geekbeacon.social.model.SocialType;
import org.geekbeacon.social.model.Status;
import org.geekbeacon.social.repository.SocialRepository;
import org.geekbeacon.social.service.SocialDataService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Qualifier("twitter")
@Log4j2
public class TwitterSocialDataServiceImpl implements SocialDataService {
    private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String FOLLOWERS = "https://api.twitter.com/1.1/users/show.json?screen_name=nixiepixel&include_entities=followers_count";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SocialType socialType = SocialType.TWITTER;
    private final SocialRepository socialRepository;
    OAuth10aService service;
    OAuth1RequestToken requestToken;
    @Getter
    boolean enabled;
    OAuth1AccessToken accessToken;

    public TwitterSocialDataServiceImpl(
        @Value("${social.twitch.enabled:true}") boolean enabled,
        SocialRepository socialRepository) {
        this.enabled = enabled;
        this.socialRepository = socialRepository;

        if(!isEnabled()) {
            constructTwitterService();
        }
    }

    /**
     * @see SocialDataService#getFollowerCount()
     */
    @Override
    public int getFollowerCount() {
        if(!isEnabled()) {
            return 0;
        }
        long start = System.currentTimeMillis();
        RestTemplate restTemplate = new RestTemplate();
        String quote = restTemplate.getForObject("https://cdn.syndication.twimg.com/widgets/followbutton/info.json?screen_names=nixiepixel", String.class);
        int cnt = JsonPath.read(quote, "$[0].followers_count");
        log.debug("it took {} to fetch data", System.currentTimeMillis() -  start);
        return  cnt;
    }

    /**
     * @see SocialDataService#saveRequestToken(String, String)
     */
    @Override
    public void saveRequestToken(String token, String verifier) {
        if(!isEnabled()) {
            log.debug("Service type {} is not enabled, skipping", socialType.name());
            return;
        }
        try {
            accessToken = service.getAccessToken(requestToken, verifier);
            log.debug(accessToken.toString());
            socialRepository.saveUserSocialTokens(socialType, accessToken.getToken(), accessToken.getTokenSecret());
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error("woot");
        }
    }

    /**
     * @see SocialDataService#getSocialActivity(int)
     */
    @Override
    public List<SocialActivity> getSocialActivity(int limit) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @see SocialDataService#authorizeApplication()
     */
    @Override
    public AuthResponse authorizeApplication() {
        if(!isEnabled()) {
            return null;
        }

        if (accessToken != null) {
            return
                AuthResponse.builder()
                    .status(Status.PREEXISTING)
                    .build();
        }

        String authorizeUrl = null;
        try {
            requestToken = service.getRequestToken();
            authorizeUrl = service.getAuthorizationUrl(requestToken);
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error("Error, failed to get token");
        }
        return AuthResponse.builder().status(Status.SUCCESS)
            .url(authorizeUrl).build();
    }

    private void constructTwitterService() {
        SocialAppRecord record = socialRepository.getSocialAppRecord(socialType);

        if (record == null) {
            enabled = false;
            service = null;
        } else {
            service = new ServiceBuilder(record.getClientId())
                .apiSecret(record.getSecret())
                .callback(record.getCallbackUrl())
                .build(TwitterApi.instance());

            UserSocialRecord userSocialRecord = socialRepository.getUserSocialRecord(record.getAppId());
            if (userSocialRecord != null) {
                accessToken = new OAuth1AccessToken(userSocialRecord.getAccessToken(), userSocialRecord.getSecretToken());
            }

            enabled = true;
        }
    }

    @Deprecated
    private int getCountUsingApi()  {
        int cnt = 0;
        String nextCursor = "-1";
        try {
            do {
                final OAuthRequest request = new OAuthRequest(Verb.GET, FOLLOWERS.replaceAll("CURSOR", nextCursor));
                service.signRequest(accessToken, request);
                try (Response response = service.execute(request)) {
                    System.out.println(response.getBody());
                    String body = response.getBody();
                    Map map = objectMapper.readValue(body, Map.class);
                    if(map.containsKey("errors")) {
                        log.error(map);
                        return -1;
                    }
                    if(map.containsKey("ids")) {
                        cnt += ((List) map.get("ids")).size();
                    }
                    nextCursor = (String) map.get("next_cursor_str");
                }
            } while (!nextCursor.equals("0")) ;
            return cnt;
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
