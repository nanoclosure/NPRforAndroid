package edu.nanoracket.nprforandroid.ui.activity;

import android.support.v4.app.Fragment;

import edu.nanoracket.nprforandroid.program.PodcastFragment;

public class PodcastActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
        String podcastUrl = (String)getIntent()
            .getStringExtra(PodcastFragment.PODCAST_URL);
        return PodcastFragment.newInstance(podcastUrl);
    }
}
