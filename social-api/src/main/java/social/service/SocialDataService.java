package social.service;

import social.model.AuthResponse;

public interface SocialDataService {

    int getCount();

    AuthResponse authorize();


    void saveRequestToken(String token, String verifier);
}
