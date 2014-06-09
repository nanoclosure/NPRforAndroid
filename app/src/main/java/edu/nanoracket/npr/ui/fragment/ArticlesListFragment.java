package edu.nanoracket.npr.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.data.ArticleListAdapter;
import edu.nanoracket.npr.data.ArticlesContract;
import edu.nanoracket.npr.ui.activity.ArticleActivity;

public class ArticlesListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,AbsListView.OnItemClickListener {

    public static final String TAG = "ArticleListFragment";
    private static final int LOADER_ID = 30;

    private static final String[] PROJ = new String[]{
            ArticlesContract.Columns.ID,
            ArticlesContract.Columns.TOPIC,
            ArticlesContract.Columns.TITLE,
            ArticlesContract.Columns.BYLINE,
            ArticlesContract.Columns.PUB_DATE,
            ArticlesContract.Columns.IMAGE_PATH,
    };

    private AbsListView listView;
    private ArticleListAdapter listAdapter;

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader = new CursorLoader(getActivity(), ArticlesContract.URI,
                PROJ, null, null, null);
        return cursorLoader;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        listAdapter.swapCursor(cursor);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        listAdapter.swapCursor(null);
    }

    public ArticlesListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new ArticleListAdapter(getActivity(), null);
        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articleslist, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = (Cursor)listAdapter.getItem(position);
        Uri uri = ArticlesContract.URI.buildUpon()
                .appendPath(cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.ID)))
                .build();
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        intent.putExtra(ArticleFragment.Article_URI, uri.toString());
        startActivity(intent);
    }
}
