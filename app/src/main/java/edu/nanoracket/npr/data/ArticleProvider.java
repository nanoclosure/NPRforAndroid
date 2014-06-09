package edu.nanoracket.npr.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ArticleProvider extends ContentProvider{

    private static final String TAG = "ContentProvider";
    private ArticlesHelper articleHelper;
    private static final int ARTICLES_DIR = 1;
    private static final int ARTICLES_ITEM = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ArticlesContract.AUTHORITY, ArticlesContract.TABLE, ARTICLES_DIR);
        uriMatcher.addURI(ArticlesContract.AUTHORITY, ArticlesContract.TABLE + "/#", ARTICLES_ITEM);
    }

    public ArticleProvider() {}

    @Override
    public boolean onCreate() {
        articleHelper = new ArticlesHelper(getContext());
        Log.i(TAG, "db is created.");
        return articleHelper != null;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case ARTICLES_DIR:
                return ArticlesContract.CONTENT_TYPE_DIR;
            case ARTICLES_ITEM:
                return ArticlesContract.CONTENT_TYPE_ITEM;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = articleHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(ArticlesHelper.TAB_ARTICLES);

        switch (uriMatcher.match(uri)){
            case ARTICLES_DIR:
                break;
            case ARTICLES_ITEM:
                qb.appendWhere(ArticlesContract.Columns.ID + "=" +uri.getLastPathSegment());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
        String orderBy = TextUtils.isEmpty(sortOrder)
                ? "" : sortOrder;

        Cursor cursor = qb.query(db,projection, selection, selectionArgs, null, null, orderBy);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result = null;

        if(uriMatcher.match(uri) != ARTICLES_DIR){
            throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }

        try{
            SQLiteDatabase db = articleHelper.getWritableDatabase();
            long id = db.insertOrThrow(ArticlesHelper.TAB_ARTICLES, null, values);
            if(id > 0){
                result = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(result, null);
            } else {
                result = null;
            }
            return result;
        }catch (SQLiteConstraintException e){
            Log.i(TAG, "Ignoring constraint failure.");
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = articleHelper.getWritableDatabase();
        int count = 0;

        switch (uriMatcher.match(uri)){
            case ARTICLES_DIR:
                count = db.delete(ArticlesHelper.TAB_ARTICLES, selection,selectionArgs);
                break;
            case ARTICLES_ITEM:
                String segment = uri.getLastPathSegment();
                String whereClause = ArticlesContract.Columns.ID + "=" + segment
                        + (!TextUtils.isEmpty(selection)? " AND (" + selection + ")" : "");
                count = db.delete(ArticlesHelper.TAB_ARTICLES,whereClause, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported URI: " + uri);
        }
        if(count > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
