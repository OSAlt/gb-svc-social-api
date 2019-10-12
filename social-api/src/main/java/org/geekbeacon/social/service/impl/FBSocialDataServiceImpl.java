package org.geekbeacon.social.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.geekbeacon.social.model.AuthResponse;
import org.geekbeacon.social.service.SocialDataService;

@Service
@Qualifier("FB")
public class FBSocialDataServiceImpl implements SocialDataService {
    @Override
    public int getCount() {
        return 50466;
    }

    @Override
    public AuthResponse authorize() {
        return null;
    }

    @Override
    public void saveRequestToken(String token, String verifier) {

    }
}
