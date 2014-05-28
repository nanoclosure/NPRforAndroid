package edu.nanoracket.npr.ui.activity;

import android.support.v4.app.Fragment;

import edu.nanoracket.npr.program.PodcastFragment;

public class PodcastActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String podcastUrl = (String)getIntent()
            .getStringExtra(PodcastFragment.PODCAST_URL);
        String progName = getIntent().getStringExtra(PodcastFragment.PODCAST_PROGRAM);
        return PodcastFragment.newInstance(podcastUrl, progName);
    }
}
