package edu.nanoracket.npr.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import edu.nanoracket.npr.R;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.File;
import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
public class ImageSerializerTest {

    @Test
    public void testSaveImage() throws IOException {
        String fileName = "npr.png";
        Context context = Robolectric.application;

        Resources resources = Robolectric.application.getResources();
        ImageView imageView = new ImageView(context);
        Bitmap bitMap = BitmapFactory.decodeResource(resources, R.drawable.navdrawer_news);
        imageView.setImageBitmap(bitMap);

        ImageSerializer imageSerializer = new ImageSerializer(context, fileName);
        imageSerializer.saveImage(imageView);

        File expectedFile = new File(fileName);
        Assert.assertNotNull(expectedFile.exists());
        expectedFile.delete();
    }
}
