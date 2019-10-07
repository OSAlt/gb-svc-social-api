package social.service.impl;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import social.model.AuthResponse;
import social.model.SocialType;
import social.service.SocialDataService;

@Service
@Qualifier("instagram")
@Log4j2
public class InstagramSocialDataServiceImpl implements SocialDataService {

    private final SocialType socialType = SocialType.INSTAGRAM;
    private final String username;

    @Autowired
    public InstagramSocialDataServiceImpl(@Value("${social.instagram.username:nixietron}") String username) {
        this.username = username;
    }

    @Override
    public int getCount() {
        try {
            HttpClient client = HttpClients.createMinimal();
            HttpUriRequest request = RequestBuilder.get()
                .setUri(String.format("https://www.instagram.com/%s/", username))
                .addParameter("__a", "1")
                .build();

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new HttpResponseException(response.getStatusLine().getStatusCode(), "Invalid Status Code");
            }

            int followers = JsonPath.read(EntityUtils.toString(response.getEntity()), "$.graphql.user.edge_followed_by.count");
            log.debug("The {} channel: {} has {} followers.", socialType.name(), username, followers);
            return followers;
        } catch (Exception e) {
            log.error("Failed to retrieve data, falling back on defaults", e);
            return 0;
        }
    }

    @Override
    public AuthResponse authorize() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void saveRequestToken(String token, String verifier) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
