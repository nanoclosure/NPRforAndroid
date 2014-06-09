package edu.nanoracket.npr.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class ArticlesHelper extends SQLiteOpenHelper {

    static final int VERSION = 6;
    static final String DB_FILE = "nprnews.db";
    static final String TAB_ARTICLES = "articles";
    static final String COL_ID = "_id";

    public static final String COL_TOPIC = "topic";
    public static final String COL_TITLE = "title";
    public static final String COL_BYLINE = "byline";
    public static final String COL_PUB_DATE = "pubDate";
    public static final String COL_IMAGE_PATH = "imagePath";
    public static final String COL_NEWS_TEXT = "newsText";

    public ArticlesHelper(Context context) {
        super(context, DB_FILE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "Create TABLE " + TAB_ARTICLES + " ("
                + COL_ID + " integer PRIMARY KEY AUTOINCREMENT, "
                + COL_TOPIC + " text, "
                + COL_TITLE + " text UNIQUE, "
                + COL_BYLINE + " text, "
                + COL_PUB_DATE + " text, "
                + COL_IMAGE_PATH + " text, "
                + COL_NEWS_TEXT + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            sqLiteDatabase.execSQL("drop table " + TAB_ARTICLES);
        } catch (SQLiteException e){}
        onCreate(sqLiteDatabase);
    }
}
