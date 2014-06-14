package edu.nanoracket.npr.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class ImageSerializer {

    private static final String TAG = "ImageSerializer";
    private Context context;
    private String fileName;

    public ImageSerializer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void saveImage(ImageView imageView) throws IOException {
        OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        Bitmap bitmap = imageView.getDrawingCache();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.close();
    }
}
