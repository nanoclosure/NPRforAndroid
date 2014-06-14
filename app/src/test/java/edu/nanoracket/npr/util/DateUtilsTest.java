package edu.nanoracket.npr.util;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowToast;

public class DateUtilsTest {

    @Test
    public void testGetPubDate(){
        String input = "Thu, 12 Jun 2014 09:01:00 -0400";
        String result = "Jun 12 09:01:00 AM";
        Assert.assertEquals(result, DateUtils.getPubDate(input));
    }
}
