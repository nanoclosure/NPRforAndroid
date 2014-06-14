package edu.nanoracket.npr.util;


import android.content.Context;
import edu.nanoracket.npr.R;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ResourceUtilsTest {

    @Test
    public void testGetDrawableByName(){
        Context context = Robolectric.application;
        String name = "cartalk";
        Assert.assertNotNull(ResourceUtils.getDrawableByName(name, context));
    }

    @Test(expected = RuntimeException.class)
    public void testGetDrawableByNoName(){
        Context context = Robolectric.application;
        String name = "hello";
        ResourceUtils.getDrawableByName(name, context);
    }

    @Test
    public void testGetDrawableIdByName(){
        Context context = Robolectric.application;
        String name = "cartalk";
        Assert.assertEquals(R.drawable.cartalk,
                ResourceUtils.getDrawableIdByName(name,context));
    }
}
