package org.geekbeacon.social.service.impl;

import lombok.NoArgsConstructor;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.geekbeacon.social.service.SocialDataService;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClients.class, EntityUtils.class})
@NoArgsConstructor
class DiscordSocialDataServiceImplTest {

    SocialDataService service;

    @Before
    public void setup() throws IOException {
        service = new DiscordSocialDataServiceImpl("dummy");
        SocialDataHelperTest.setupMockedObjects("/responses/discord_count.json");
    }

    @Test
    public void getCount() {
        int expected = 4368;
        int count = service.getCount();
        assertEquals(expected,count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void authorize() {
        service.authorize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void saveRequestToken() {
        service.saveRequestToken("dummy", "value");
    }
}