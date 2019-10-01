package social.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import social.model.AuthResponse;
import social.service.SocialDataService;

@Qualifier("twitch")
@Service
public class TwitchSocialDataServiceImpl implements SocialDataService {
    @Override
    public int getCount() {
        return 2039;
    }

    @Override
    public AuthResponse authorize() {
        return null;
    }

    @Override
    public void saveRequestToken(String token, String verifier) {

    }
}
