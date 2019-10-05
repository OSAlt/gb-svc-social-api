package social.api;

import com.github.scribejava.core.builder.api.DefaultApi10a;

public class TwitchApi extends DefaultApi10a {
    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return null;
    }
}
