package org.geekbeacon.social.www;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.geekbeacon.social.model.SocialType;
import org.geekbeacon.social.service.SocialDataAdapter;

@RestController
public class CallbackController {

    private final SocialDataAdapter socialDataAdapter;

    @Autowired
    public CallbackController(SocialDataAdapter socialDataAdapter) {
        this.socialDataAdapter = socialDataAdapter;
    }

    @RequestMapping(value = "v1.0/callback/{type}", method = RequestMethod.GET)
    public String  authorize(@PathVariable("type") SocialType type,
                             @RequestParam(name="oauth_token", defaultValue="Token")
                              String token,
                             @RequestParam(name="oauth_verifier", defaultValue="Token")
                              String verifier) {

        socialDataAdapter.saveRequestToken(type, token, verifier);
        return "SUCCESS";

    }

}
