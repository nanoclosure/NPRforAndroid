package edu.nanoracket.npr.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ArticleListAdapterTest {
    private Context context;
    private Cursor cursor;
    private ArticleListAdapter articleListAdapter;

    @Before
    public void setUp(){
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.create(null);
        sqLiteDatabase.execSQL("CREATE TABLE articles(_id INT PRIMARY KEY," +
                "topic TEXT, title TEXT UNIQUE, byline TEXT, pubDate Text, imagePath TEXT);");
        String[] inserts = {
                "INSERT INTO articles values(1, 'news','amazon','Chuck','Jun 12','hello.png');",
                "INSERT INTO articles values(2, 'story','google','Tom','Jun 11','hello.png');",
                "INSERT INTO articles values(3, 'story','apple','Jane','Jun 10','hello.png');"
        };

        for(String insert : inserts){
            sqLiteDatabase.execSQL(insert);
        }

        String sql = "SELECT * FROM articles;";
        cursor = sqLiteDatabase.rawQuery(sql, null);

        context = Robolectric.application;
        articleListAdapter = new ArticleListAdapter(context, cursor);
    }

    @Test
    public void articleListAdapter_behaves(){
        Assert.assertTrue(cursor.getCount() == 3);
        Assert.assertTrue(Robolectric.shadowOf(articleListAdapter).getCursor() != null);
        Assert.assertTrue(articleListAdapter.getCursor() == cursor);
        View view = articleListAdapter.newView(context, cursor, null);
        Assert.assertNotNull(view);
        Assert.assertNotNull(view.getTag());
    }
}
