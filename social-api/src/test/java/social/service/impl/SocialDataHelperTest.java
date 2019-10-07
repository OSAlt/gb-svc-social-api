package social.service.impl;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocialDataHelperTest {

    public static void setupMockedObjects(String resource) throws IOException {
        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.mockStatic(EntityUtils.class);

        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        HttpEntity httpEntity = mock(HttpEntity.class);
        HttpClientBuilder builder = mock(HttpClientBuilder.class);

        when(HttpClients.createMinimal()).thenReturn(httpClient);
        when(HttpClients.custom()).thenReturn(builder);
        when(builder.build()).thenReturn(httpClient);
        when(httpClient.execute(any())).thenReturn(response);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getEntity()).thenReturn(httpEntity);

        InputStream stream = SocialDataHelperTest.class.getResourceAsStream(resource);
        String json = IOUtils.toString(stream, StandardCharsets.UTF_8);
        when(EntityUtils.toString(any())).thenReturn(json);
    }
}
