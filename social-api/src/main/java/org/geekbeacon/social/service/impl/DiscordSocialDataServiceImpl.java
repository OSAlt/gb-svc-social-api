package org.geekbeacon.social.service.impl;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.geekbeacon.social.model.SocialActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.geekbeacon.social.model.AuthResponse;
import org.geekbeacon.social.model.SocialType;
import org.geekbeacon.social.service.SocialDataService;

import java.util.List;

@Service
@Qualifier("discord")
@Log4j2
public class DiscordSocialDataServiceImpl implements SocialDataService {

    private static final String DISCORD_MEMBER_COUNT_URL = "https://discordapp.com/api/v6/invites/";
    private final SocialType socialType = SocialType.DISCORD;
    private final String username;

    @Autowired
    public DiscordSocialDataServiceImpl(@Value("${social.discord.username:geekbeacon}") String username) {
        this.username = username;
    }

    /**
     * @see SocialDataService#getFollowerCount() 
     */
    @Override
    public int getFollowerCount() {
        try {
            HttpClient client = HttpClients.createMinimal();
            HttpUriRequest request = RequestBuilder.get()
                .setUri(DISCORD_MEMBER_COUNT_URL + username)
                .addParameter("with_counts", "true")
                .build();

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new HttpResponseException(response.getStatusLine().getStatusCode(), "Invalid Status Code");
            }

            int members = JsonPath.read(EntityUtils.toString(response.getEntity()), "$.approximate_member_count");
            log.debug("The {} channel: {} has {} members.", socialType.name(), username, members);
            return members;
        } catch (Exception e) {
            log.error("Failed to retrieve data, falling back on defaults", e);
            return 0;
        }
    }

    /**
     * @see SocialDataService#authorizeApplication() 
     */
    @Override
    public AuthResponse authorizeApplication() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    /**
     * @see SocialDataService#saveRequestToken(String, String) 
     */
    @Override
    public void saveRequestToken(String token, String verifier) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    /**
     * @see SocialDataService#getSocialActivity(int) 
     */
    @Override
    public List<SocialActivity> getSocialActivity(int limit) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
