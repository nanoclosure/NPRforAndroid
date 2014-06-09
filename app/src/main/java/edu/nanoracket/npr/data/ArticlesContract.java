package edu.nanoracket.npr.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ArticlesContract {
    private ArticlesContract(){}

    public static final String TABLE = "articles";
    public static final String AUTHORITY = "edu.nanoracket.npr.ARTICLES";
    public static final Uri URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .appendPath(TABLE)
            .build();

    public static final String CONTENT_TYPE_DIR =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.edu.nanoracket.npr.articles";
    public static final String CONTENT_TYPE_ITEM =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.edu.nanoracket.npr.articles";

    public static final class Columns implements BaseColumns {
        private Columns(){}

        public static final String ID = BaseColumns._ID;
        public static final String TOPIC = "topic";
        public static final String TITLE = "title";
        public static final String BYLINE = "byline";
        public static final String PUB_DATE = "pubDate";
        public static final String IMAGE_PATH = "imagePath";
        public static final String NEWS_TEXT = "newsText";
    }
}
