package edu.nanoracket.npr.ui.activity;

import android.support.v4.app.Fragment;

import edu.nanoracket.npr.music.MusicListFragment;
import edu.nanoracket.npr.ui.fragment.PodcastListFragment;

public class PodcastListActivity extends NprFragmentActivity {

	@Override
	protected Fragment createFragment() {
		 String mProgramSrc = getIntent()
		         .getStringExtra(PodcastListFragment.PODCAST_SRC);
         String programName = getIntent()
                 .getStringExtra(PodcastListFragment.PODCAST_PROGRAM);
         int imagePosition = getIntent()
                 .getIntExtra(PodcastListFragment.PODCAST_IMAGE, -1);
		 return MusicListFragment.newInstance(mProgramSrc, programName, imagePosition);
	}
}
