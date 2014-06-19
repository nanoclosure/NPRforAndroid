package edu.nanoracket.npr.ui.activity;

import android.support.v4.app.Fragment;
import android.util.Log;

import edu.nanoracket.npr.ui.fragment.StoryFragment;

public class StoryActivity extends NprFragmentActivity {
    public static final String TAG = "StoryActivity";

    @Override
    protected Fragment createFragment() {
        String id = getIntent().getStringExtra(StoryFragment.STORY_ID);
        Log.i(TAG, "Story id is: " + id);
        return StoryFragment.newInstance(id);
    }
}
