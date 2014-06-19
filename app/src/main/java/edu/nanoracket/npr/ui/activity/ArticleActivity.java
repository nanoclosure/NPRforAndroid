package edu.nanoracket.npr.ui.activity;

import android.support.v4.app.Fragment;
import android.util.Log;

import edu.nanoracket.npr.ui.fragment.ArticleFragment;

public class ArticleActivity extends NprFragmentActivity {
    public static final String TAG = "ArticleActivity";

    @Override
    protected Fragment createFragment() {
        String uri = getIntent().getStringExtra(ArticleFragment.Article_URI);
        Log.i(TAG, "Article URI is: " + uri);
        return ArticleFragment.newInstance(uri);
    }
}
