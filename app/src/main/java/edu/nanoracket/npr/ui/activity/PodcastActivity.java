package edu.nanoracket.npr.ui.activity;

import android.support.v4.app.Fragment;

import edu.nanoracket.npr.ui.fragment.PodcastFragment;

public class PodcastActivity extends NprFragmentActivity {

	@Override
	protected Fragment createFragment() {
        String podcastUrl = getIntent()
            .getStringExtra(PodcastFragment.PODCAST_URL);
        String programName = getIntent().getStringExtra(PodcastFragment.PODCAST_PROGRAM);
        return PodcastFragment.newInstance(podcastUrl, programName);
    }
}
