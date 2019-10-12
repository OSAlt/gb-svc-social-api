package org.geekbeacon.social.service;

import org.geekbeacon.social.model.AuthResponse;

public interface SocialDataService {

    int getCount();

    AuthResponse authorize();


    void saveRequestToken(String token, String verifier);
}
