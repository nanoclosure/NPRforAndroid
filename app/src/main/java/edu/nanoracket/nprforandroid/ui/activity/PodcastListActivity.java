package edu.nanoracket.nprforandroid.ui.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import edu.nanoracket.nprforandroid.ui.fragment.PodcastListFragment;

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

        String programName = getIntent().getStringExtra(PodcastListFragment.PODCAST_PROGRAM);

         int imagePostion = getIntent()
                    .getIntExtra(PodcastListFragment.PODCAST_IMAGE, -1);

		 return PodcastListFragment.newInstance(mProgramSrc, programName, imagePostion);
	}

}
