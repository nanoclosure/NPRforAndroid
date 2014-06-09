package edu.nanoracket.npr.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.data.ArticlesContract;

public class ArticleFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String TAG = "ArticleFragment";
    public static final String Article_URI = "article_uri";
    private static final int LOADER_ID = 29;

    private static final String[] PROJ = new String[]{
            ArticlesContract.Columns.ID,
            ArticlesContract.Columns.TITLE,
            ArticlesContract.Columns.BYLINE,
            ArticlesContract.Columns.PUB_DATE,
            ArticlesContract.Columns.IMAGE_PATH,
            ArticlesContract.Columns.NEWS_TEXT,
    };

    private ShareActionProvider actionProvider;
    private TextView storyTitleTextView, storyBylineTextView, storyDateTextView;
    private ImageView storyImageView;
    private WebView storyWebView;
    private Uri articleUri;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), articleUri, PROJ, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        updateView(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}


    public static ArticleFragment newInstance(String uri){
        Bundle args = new Bundle();
        args.putString(Article_URI, uri);
        ArticleFragment articleFragment = new ArticleFragment();
        articleFragment.setArguments(args);
        return articleFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        String uri = null;
        if(savedInstanceState == null){
            uri = getArguments().getString(Article_URI);
        }else{
            uri = savedInstanceState.getString(Article_URI);
        }
        articleUri = Uri.parse(uri);
        Log.i(TAG, "Article URI is: " + uri);
        if(articleUri != null){
            getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        if(articleUri != null){
            savedInstanceState.putString(Article_URI, articleUri.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_story, parent, false);

        storyTitleTextView = (TextView)view.findViewById(R.id.storyTitle);
        storyBylineTextView = (TextView)view.findViewById(R.id.storyByline);
        storyDateTextView = (TextView)view.findViewById(R.id.storyDate);
        storyImageView = (ImageView)view.findViewById(R.id.storyImage);
        storyWebView = (WebView)view.findViewById(R.id.storyWebView);
        return view;
    }

    private void updateView(Cursor cursor){
        if(!cursor.moveToNext()){return;}

        String title =
                cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.TITLE));
        String byline =
                cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.BYLINE));
        String pubDate =
                cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.PUB_DATE));
        String imagePath =
                cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.IMAGE_PATH));
        String newsText =
                cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.NEWS_TEXT));

        storyTitleTextView.setText(title);
        storyBylineTextView.setText("By " + byline);
        storyDateTextView.setText(pubDate);
        Picasso.with(getActivity().getApplicationContext())
                .load(getActivity().getFilesDir() + "/" + imagePath)
                .into(storyImageView);
        storyWebView.loadDataWithBaseURL(null,newsText, "text/html", "utf-8", null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_storyactivity, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_save:
                Log.i(TAG, "save the article.");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
