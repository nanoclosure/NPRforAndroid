package edu.nanoracket.npr.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.ui.fragment.ArticleFragment;

public class ArticleActivity extends ActionBarActivity {

    public static final String TAG = "ArticleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String uri = getIntent().getStringExtra(ArticleFragment.Article_URI);
        Log.i(TAG, "Article URI is: " + uri);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ArticleFragment.newInstance(uri))
                    .commit();
        }
    }
}
