package social.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import social.model.AuthResponse;
import social.service.SocialDataService;

@Service
@Qualifier("instagram")
public class InstragramSocialDataServiceImpl implements SocialDataService {
    @Override
    public int getCount() {
        return 4805;
    }

    @Override
    public AuthResponse authorize() {
        return null;
    }

    @Override
    public void saveRequestToken(String token, String verifier) {

    }
}
