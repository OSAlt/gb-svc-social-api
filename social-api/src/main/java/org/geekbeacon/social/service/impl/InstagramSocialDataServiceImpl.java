package org.geekbeacon.social.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.geekbeacon.social.model.AuthResponse;
import org.geekbeacon.social.model.Dimensions;
import org.geekbeacon.social.model.SocialActivity;
import org.geekbeacon.social.model.SocialType;
import org.geekbeacon.social.service.SocialDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.geekbeacon.social.support.JsonHttpHelper.safeExtract;
import static org.geekbeacon.social.support.JsonHttpHelper.validateResponse;

@Service
@Qualifier("instagram")
@Log4j2
public class InstagramSocialDataServiceImpl implements SocialDataService {

    private final static String INSTAGRAM_URL_FORMAT = "https://www.instagram.com/p/%s/";
    private static final String INSTAGRAM_API_URL = "https://www.instagram.com/%s/";

    private final SocialType socialType = SocialType.INSTAGRAM;
    private final String username;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public InstagramSocialDataServiceImpl(@Value("${social.instagram.username:nixietron}") String username) {
        this.username = username;
    }

    /**
     * @see SocialDataService#getFollowerCount()
     */
    @Override
    public int getFollowerCount() {
        try {
            HttpUriRequest request = getRequest();
            HttpResponse response = HttpClients.createMinimal().execute(request);
            validateResponse(response, true);
            int followers = JsonPath.read(EntityUtils.toString(response.getEntity()), "$.graphql.user.edge_followed_by.count");
            log.debug("The {} channel: {} has {} followers.", socialType.name(), username, followers);
            return followers;
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
        List<SocialActivity> activities = Lists.newArrayList();
        try {
            HttpUriRequest request = getRequest();
            HttpResponse response = HttpClients.createMinimal().execute(request);
            validateResponse(response, true);
            String rawJson = EntityUtils.toString(response.getEntity());
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(rawJson);

            //get Array of all posts
            JSONArray array = safeExtract(document, "$.graphql.user.edge_owner_to_timeline_media.edges[*].node", new JSONArray());

            for (Object item : array) {
                if (item instanceof Map) {
                    SocialActivity.SocialActivityBuilder builder = SocialActivity.builder();
                    Map<String, Object> items = (Map<String, Object>) item;
                    //Convert items back to String to easily extract using JsonPath
                    String jsonString = mapper.writeValueAsString(items);
                    Object nodeDocument = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
                    String caption = safeExtract(nodeDocument, "$.edge_media_to_caption.edges[0].node.text", "");
                    String shortCode =items.get("shortcode").toString();
                    if(StringUtils.isNotEmpty(shortCode)) {
                       String originalUri = String.format(INSTAGRAM_URL_FORMAT, shortCode);
                       builder.sourceUri(new URI(originalUri));
                    }
                    Map<String, Object> socialMediaLove = Maps.newHashMap();
                    builder.socialLove(socialMediaLove);

                    int count =  safeExtract(nodeDocument, "$.edge_media_to_comment.count", 0);
                    socialMediaLove.put("comments", count);
                    count = safeExtract(nodeDocument, "$.edge_liked_by.count", 0);
                    socialMediaLove.put("likes", count);


                    builder.text(caption);
                    builder.mediaUrl(items.getOrDefault("display_url", "").toString());
                    builder.isVideo(items.containsKey("is_video") && BooleanUtils.toBoolean(items.get("is_video").toString()));
                    //Get dimensions
                    if (items.containsKey("dimensions")) {
                        Map<String, Object> size = (Map<String, Object>) items.get("dimensions");
                        builder.dimensions(new Dimensions(size));
                    }
                    activities.add(builder.build());
                    if (activities.size() == limit) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to retrieve data, falling back on defaults", e);
        }
        return activities;
    }

    /**
     * @return the construct HttpRequest for API
     */
    private HttpUriRequest getRequest() {
        HttpUriRequest request = RequestBuilder.get()
            .setUri(String.format(INSTAGRAM_API_URL, username))
            .addParameter("__a", "1")
            .build();
        return request;
    }


}
