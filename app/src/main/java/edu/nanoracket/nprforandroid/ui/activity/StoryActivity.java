package edu.nanoracket.nprforandroid.ui.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import edu.nanoracket.nprforandroid.R;
import edu.nanoracket.nprforandroid.ui.fragment.StoryFragment;

public class StoryActivity extends ActionBarActivity {
    public static final String TAG = "StoryActivity";

    private ShareActionProvider actionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = (String)getIntent().getStringExtra(StoryFragment.STORY_ID);
        Log.i(TAG, "Story id is: " + id);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, StoryFragment.newInstance(id))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.story, menu);

        MenuItem saveItem = menu.findItem(R.id.action_save);
        MenuItem shareItem = menu.findItem(R.id.action_share);

        actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        actionProvider.setShareIntent(getDefaultIntent());

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        return intent;
    }

}
