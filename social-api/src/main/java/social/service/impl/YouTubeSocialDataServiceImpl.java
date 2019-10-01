package social.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import social.model.AuthResponse;
import social.service.SocialDataService;

@Service
@Qualifier("youtube")
public class YouTubeSocialDataServiceImpl implements SocialDataService {

    //return count for Nixie Pixel Vlogs + nixiedoeslinux
    @Override
    public int getCount() {
        return 221000 + 109000;
    }

    @Override
    public AuthResponse authorize() {
        return null;
    }

    @Override
    public void saveRequestToken(String token, String verifier) {

    }
}
