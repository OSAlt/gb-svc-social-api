package org.geekbeacon.social.service.impl;

import lombok.NoArgsConstructor;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.geekbeacon.social.model.SocialActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.geekbeacon.social.service.SocialDataService;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClients.class, EntityUtils.class})
@NoArgsConstructor
public class InstagramSocialDataServiceImplTest {

    SocialDataService service;

    @Before
    public void setup() throws IOException {
        SocialDataHelperTest.setupMockedObjects("/responses/instagram_count.json");
        service= new InstagramSocialDataServiceImpl("dummy");

    }

    @Test
    public void getCount() {
        int expected = 4807;
        int count = service.getFollowerCount();
        assertEquals(expected,count);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void authorize() {
        service.authorizeApplication();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void saveRequestToken() {
        service.saveRequestToken("dummy", "value");
    }


    @Test
    public void testSocialActivity() {
        List<SocialActivity> activities = service.getSocialActivity(10);
        assertNotNull(activities);
        assertEquals(activities.size(), 10);
        SocialActivity item = activities.get(0);
        String expectedUrl = "https://scontent-sjc3-1.cdninstagram.com/vp/8f87f44388a13d6bf7a57ddf9b5b0bb6/5D9D5946/t51.2885-15/e35/66669977_634891716997189_155298499957585776_n.jpg?_nc_ht=scontent-sjc3-1.cdninstagram.com&_nc_cat=101";
        assertEquals(item.getMediaUrl(), expectedUrl);
        assertTrue(item.isVideo());
        assertEquals(item.getDimensions().getHeight(), 750);
        assertEquals(item.getDimensions().getWidth(), 750);
        assertEquals(item.getText(), "I'm releasing my new video premiere here, and chatting live now: https://youtu.be/X0ckN02atkM \n" +
            "MANY crazy things have happened that require epic story time, so let’s go on this wild, 1 hour long adventure together. \n" +
            "Come say hi!");
        assertEquals(item.getSocialLove().get("comments"), 23);
        assertEquals(item.getSocialLove().get("likes"), 236);
        assertEquals(item.getSourceUri().toString(), "https://www.instagram.com/p/Bz9Qo_QAWCA/");

    }
}