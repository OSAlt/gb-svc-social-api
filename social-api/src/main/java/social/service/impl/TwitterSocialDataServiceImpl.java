package social.service.impl;

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
import lombok.extern.log4j.Log4j2;
import org.geekbeacon.social.db.models.config.tables.records.SocialAppRecord;
import org.geekbeacon.social.db.models.config.tables.records.UserSocialRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import social.model.AuthResponse;
import social.model.SocialType;
import social.model.Status;
import social.repository.SocialRepository;
import social.service.SocialDataService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Qualifier("twitter")
@Log4j2
public class TwitterSocialDataServiceImpl implements SocialDataService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    //    private final DSLContext dslContext;
    private final SocialType socialType = SocialType.TWITTER;
    OAuth10aService service;
    OAuth1RequestToken requestToken;
    boolean enabled;
    OAuth1AccessToken accessToken;
    private final SocialRepository socialRepository;

    //                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {};
    private static final String PROTECTED_RESOURCE_URL = "https://api.twitter.com/1.1/account/verify_credentials.json";
    private static final String FOLLOWERS = "https://api.twitter.com/1.1/users/show.json?screen_name=nixiepixel&include_entities=followers_count";

    public TwitterSocialDataServiceImpl(SocialRepository socialRepository) {
        this.socialRepository = socialRepository;
        constructTwitterService();

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

    @Override
    public int getCount() {
        RestTemplate restTemplate = new RestTemplate();
        String quote = restTemplate.getForObject("https://cdn.syndication.twimg.com/widgets/followbutton/info.json?screen_names=nixiepixel", String.class);
        return JsonPath.read(quote, "$[0].followers_count");
    }


    public void saveRequestToken(String token, String verifier) {
        try {
            accessToken = service.getAccessToken(requestToken, verifier);
            log.debug(accessToken.toString());
            socialRepository.saveUserSocailTokens(socialType, accessToken.getToken(), accessToken.getTokenSecret());
        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error("woot");
        }
    }


    @Override
    public AuthResponse authorize() {
        if (accessToken != null) {
            return
                AuthResponse.builder()
                    .status(Status.PREEXISTING)
                    .build();
        }
        SocialAppRecord socialAppRecord = socialRepository.getSocialAppRecord(socialType);

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

    public void run() throws IOException, InterruptedException, ExecutionException {
//        final OAuth10aService service = new ServiceBuilder("your client id")
//            .apiSecret("your client secret")
//            .build(TwitterApi.instance());
//        final Scanner in = new Scanner(System.in);

//        System.out.println("=== Twitter's OAuth Workflow ===");
//        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        final OAuth1RequestToken requestToken = service.getRequestToken();
//        System.out.println("Got the Request Token!");
//        System.out.println();

        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(service.getAuthorizationUrl(requestToken));
        System.out.println("And paste the verifier here");
        System.out.print(">>");
//        final String oauthVerifier = in.nextLine();
//        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
//        System.out.println("Trading the Request Token for an Access Token...");
//        final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauthVerifier);
//        System.out.println("Got the Access Token!");
//        System.out.println("(The raw response looks like this: " + accessToken.getRawResponse() + "')");
//        System.out.println();

        // Now let's go and ask for a protected resource!
//        System.out.println("Now we're going to access a protected resource...");
//        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
//        service.signRequest(accessToken, request);
//        try (Response response = service.execute(request)) {
//            System.out.println("Got it! Lets see what we found...");
//            System.out.println();
//            System.out.println(response.getBody());
//        }
        System.out.println();
        System.out.println("That's it man! Go and build something awesome with ScribeJava! :)");
    }

}
