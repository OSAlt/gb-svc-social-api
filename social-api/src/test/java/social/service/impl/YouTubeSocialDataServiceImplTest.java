package social.service.impl;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.geekbeacon.social.db.models.config.tables.SocialAppType;
import org.geekbeacon.social.db.models.config.tables.records.SocialAppRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import social.repository.SocialRepository;
import social.service.SocialDataService;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClients.class, EntityUtils.class})
@NoArgsConstructor
public class YouTubeSocialDataServiceImplTest {

    SocialDataService service;

    @Before
    public void setup() throws IOException {
        SocialRepository socialRepository = mock(SocialRepository.class);
        SocialAppRecord record = mock(SocialAppRecord.class);
        when(socialRepository.getSocialAppRecord(any())).thenReturn(record);
        when(record.getClientId()).thenReturn("secret");
        SocialDataHelperTest.setupMockedObjects("/responses/youtube_count.json");
        service = new YouTubeSocialDataServiceImpl(true, socialRepository);
    }


    @Test
    public void getCount() {
        int expected = 246912;
        int count = service.getCount();
        assertEquals(expected, count);
    }

    @Test(expected = NotImplementedException.class)
    public void authorize() {
        service.authorize();
    }

    @Test(expected = NotImplementedException.class)
    public void saveRequestToken() {
        service.saveRequestToken("a", "b");
    }
}