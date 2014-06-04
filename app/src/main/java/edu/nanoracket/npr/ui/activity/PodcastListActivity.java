package edu.nanoracket.npr.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import edu.nanoracket.npr.music.MusicListFragment;
import edu.nanoracket.npr.ui.fragment.PodcastListFragment;

public class PodcastListActivity extends SingleFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		//setHasOptionsMenu(true);
	}

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
