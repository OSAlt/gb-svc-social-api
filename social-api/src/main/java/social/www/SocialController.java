package social.www;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import social.model.AuthResponse;
import social.model.SocialType;
import social.model.Status;
import social.service.SocialDataAdapter;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class SocialController {

    private final SocialDataAdapter socialDataAdapter;

    @Autowired
    public SocialController(SocialDataAdapter socialDataAdapter) {
        this.socialDataAdapter = socialDataAdapter;
    }


    @CrossOrigin(origins = {"https://landing.nixiepixel.com",
        "https://www.nixiepixel.com",
        "https://nixiepixel.com",
        "http://localhost:8000",
        "http://127.0.0.1:8000" })
    @GetMapping("api/social/types")
    public List<String> getSocialTypes() {
        List<String> types = Arrays.stream(SocialType.values()).map(Enum::name).collect(Collectors.toList());
        return types;
    }

    @RequestMapping(value = "api/social/{type}/authorize", method = RequestMethod.GET)
    public AuthResponse authorize(@PathVariable("type") SocialType type, HttpServletResponse httpResponse) throws IOException {
        AuthResponse response =  socialDataAdapter.authorize(type);
        if (response == null) {
            throw new WebServiceException("Invalid State, cannot authorize");
        }
        if(response.getStatus().equals(Status.PREEXISTING) || response.getStatus().equals(Status.ERROR)) {
            return response;
        } else {
            httpResponse.sendRedirect(response.getUrl());
        }

        return null;
    }

    @CrossOrigin(origins = {"https://landing.nixiepixel.com",
        "https://www.nixiepixel.com",
        "https://nixiepixel.com",
        "http://localhost:8000",
        "http://127.0.0.1:8000" })
    @GetMapping("api/social/{type}/count")
    public long sayCount(@PathVariable("type") SocialType type) {
        return socialDataAdapter.getCount(type);
    }


    @CrossOrigin(origins = {"https://landing.nixiepixel.com",
        "https://www.nixiepixel.com",
        "https://nixiepixel.com",
        "http://localhost:8000",
        "http://127.0.0.1:8000" })
    @GetMapping("api/social/count/all")
    public Map<String, Integer> getAllCounts() {
        Map<String, Integer> collect = Arrays.stream(SocialType.values())
            .filter(v-> !v.equals(SocialType.AGGREGATE))
            .collect(Collectors.toMap(t -> t.name().toLowerCase(), socialDataAdapter::getCount));

        return collect;


    }

}
