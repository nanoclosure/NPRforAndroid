package edu.nanoracket.npr.ui.fragment;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.adapter.ArticleListAdapter;
import edu.nanoracket.npr.data.ArticlesContract;
import edu.nanoracket.npr.ui.activity.ArticleActivity;

public class ArticlesListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,AbsListView.OnItemClickListener,
        AbsListView.OnItemLongClickListener{

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
    private int menuItemPosition;
    private ActionMode actionMode = null;


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
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            registerForContextMenu(listView);
        }else {
            listView.setOnItemLongClickListener(this);
        }
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_articlelistfragment, menu);
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

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        switch (item.getItemId()){
            case R.id.action_delete:
                deleteArticle(position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG, "listener is called");
        menuItemPosition = position;
        if (actionMode != null) {
            return false;
        }
        actionMode = getActivity().startActionMode(actionModeCallback);
        listView.setSelected(true);
        return true;
    }

    private void deleteArticle(int position){
        Cursor cursor = (Cursor)listAdapter.getItem(position);
        Uri uri = ArticlesContract.URI.buildUpon()
                .appendPath(cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.ID)))
                .build();
        new DeleteArticleTask(getActivity().getContentResolver()).execute(uri);
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback(){
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_articlelistfragment, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_delete:
                    deleteArticle(menuItemPosition);
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };


    static class DeleteArticleTask extends AsyncTask<Uri, Void, Void>{
        private final ContentResolver resolver;

        DeleteArticleTask(ContentResolver resolver) {
            this.resolver = resolver;
        }

        @Override
        protected Void doInBackground(Uri... uris) {
            resolver.delete(uris[0], null, null);
            return null;
        }
    }
}
