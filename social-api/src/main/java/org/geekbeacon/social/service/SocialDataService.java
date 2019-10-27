package org.geekbeacon.social.service;

import org.geekbeacon.social.model.AuthResponse;
import org.geekbeacon.social.model.SocialActivity;

import java.util.List;

public interface SocialDataService {

    /**
     * @return follower count
     */
    int getFollowerCount();

    /**
     * Authorize the application to access user data or social media api at least on behalf of user
     */
    AuthResponse authorizeApplication();


    /**
     * persist data to DB
     */
    void saveRequestToken(String token, String verifier);

    /**
     * @return list of recent social activity up to the limit requested
     */
    List<SocialActivity> getSocialActivity(int limit);
}
