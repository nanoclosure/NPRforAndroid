package edu.nanoracket.npr.service;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowNotificationManager;

@RunWith(RobolectricTestRunner.class)
public class NewsUpdateServiceTest {
    @Before
    public void setUp(){}

    @Test
    public void testOnHandleIntent(){
        Intent intent = new Intent(Robolectric.application, StubNewsUpdateService.class);
        NotificationManager notificationManager = (NotificationManager)Robolectric.application
                .getSystemService(Context.NOTIFICATION_SERVICE);
        StubNewsUpdateService service = new StubNewsUpdateService();
        service.onCreate();
        service.onHandleIntent(intent);

        ShadowNotificationManager manager = Robolectric.shadowOf(notificationManager);
        Assert.assertEquals("Expected on notification", 1, manager.size());
    }

    class StubNewsUpdateService extends NewsUpdateService{
        @Override
        public void onHandleIntent(Intent intent){
            super.onHandleIntent(intent);
        }
    }
}
