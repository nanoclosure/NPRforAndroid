package edu.nanoracket.npr.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ArticleHelperTest {

    Context context;
    ArticlesHelper helper;

    @Before
    public void setUp(){
        context = Robolectric.application;
        helper = new ArticlesHelper(context);
    }

    @Test
    public void testDatabase(){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ArticlesHelper.COL_ID, "1");
        values.put(ArticlesHelper.COL_TOPIC, "topic");
        values.put(ArticlesHelper.COL_TITLE, "title");
        values.put(ArticlesHelper.COL_BYLINE, "byline");
        values.put(ArticlesHelper.COL_PUB_DATE, "pubDate");
        values.put(ArticlesHelper.COL_IMAGE_PATH, "imagePath");
        values.put(ArticlesHelper.COL_NEWS_TEXT, "newsText");
        db.insert(ArticlesHelper.TAB_ARTICLES, null, values);
        Cursor cursor = db.rawQuery("SELECT * FROM articles", null);
        Assert.assertEquals(1, cursor.getCount());
    }
}
