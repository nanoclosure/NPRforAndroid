package edu.nanoracket.npr.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowContentResolver;

@RunWith(RobolectricTestRunner.class)
public class ArticleProviderTest {
    private ArticleProvider articleProvider;
    private ContentResolver contentResolver;
    private ShadowContentResolver shadowContentResolver;

    @Before
    public void setUp(){
        articleProvider = new ArticleProvider();
        contentResolver = Robolectric.application.getContentResolver();
        shadowContentResolver = Robolectric.shadowOf(contentResolver);
        articleProvider.onCreate();
        shadowContentResolver.registerProvider(ArticlesContract.AUTHORITY, articleProvider);
    }

    @Test
    public void testInsertAndDelete(){
        ContentValues values = new ContentValues();
        values.put(ArticlesContract.Columns.TOPIC, "topic");
        values.put(ArticlesContract.Columns.TITLE, "title");
        values.put(ArticlesContract.Columns.BYLINE, "byline");
        values.put(ArticlesContract.Columns.PUB_DATE, "pubDate");
        values.put(ArticlesContract.Columns.IMAGE_PATH, "imagePath");
        values.put(ArticlesContract.Columns.NEWS_TEXT, "newsText");
        Uri uri = contentResolver.insert(ArticlesContract.URI, values);
        Assert.assertEquals(uri.getLastPathSegment(), "1");
        Cursor cursor = contentResolver.query(ArticlesContract.URI, null, null, null, null);
        Assert.assertEquals(cursor.getCount(), 1);
        int count = contentResolver.delete(uri, null, null);
        Assert.assertTrue(count == 1);
        Cursor cursor1 = contentResolver.query(ArticlesContract.URI, null, null, null, null);
        Assert.assertTrue(cursor1.getCount() == 0);
    }
}
