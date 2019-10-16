package org.geekbeacon.social.service.impl;

import org.geekbeacon.social.model.SocialActivity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.geekbeacon.social.model.AuthResponse;
import org.geekbeacon.social.service.SocialDataService;

import java.util.List;

@Service
@Qualifier("FB")
public class FBSocialDataServiceImpl implements SocialDataService {

    /**
     * @see SocialDataService#getFollowerCount() 
     */
    @Override
    public int getFollowerCount() {
        return 50466;
    }

    /**
     * @see SocialDataService#authorizeApplication() 
     */
    @Override
    public AuthResponse authorizeApplication() {
        return null;
    }
    
    /**
     * @see SocialDataService#saveRequestToken(String, String) 
     */
    @Override
    public void saveRequestToken(String token, String verifier) {

    }

    /**
     * @see SocialDataService#getSocialActivity(int) 
     */
    @Override
    public List<SocialActivity> getSocialActivity(int limit) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
