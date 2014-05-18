package edu.nanoracket.nprforandroid.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.nanoracket.nprforandroid.podcast.Podcast;
import edu.nanoracket.nprforandroid.podcast.PodcastList;
import edu.nanoracket.nprforandroid.podcast.PodcastListFetcher;
import edu.nanoracket.nprforandroid.program.PodcastFragment;
import edu.nanoracket.nprforandroid.ui.activity.PodcastActivity;


public class PodcastListFragment extends ListFragment {
	private static final String TAG = "NPRPodcastListFragment";
	public static final String PODCAST_SRC = 
			"edu.nanoracket.npr.nprpodcastlistfragment.src";
	
	public ArrayList<Podcast> mNPRPodcastList;
	
	public static PodcastListFragment newInstance(String podcastSrc){
		Bundle args = new Bundle();
		args.putString(PODCAST_SRC, podcastSrc);
		
		PodcastListFragment fragment = new PodcastListFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		setHasOptionsMenu(true);
		mNPRPodcastList = PodcastList.get().getNPRPodcastsList();
		//mNewsesList = NPRNewsList.get().getNPRNewsList();
		new FetchPodcastsTask().execute();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
        
		Podcast podcast = (Podcast) mNPRPodcastList.get(position);
        Log.i(TAG,"Podcasts selected: " + podcast);
        Intent i = new Intent(getActivity(), PodcastActivity.class);
        i.putExtra(PodcastFragment.PODCAST_URL,podcast.getUrl());
        Log.i(TAG,"Podcasts URL selected: " + podcast.getUrl());
        startActivity(i);
    }
	
    private class FetchPodcastsTask extends AsyncTask<Void, Void, ArrayList<Podcast>> {
		
		@Override
		protected ArrayList<Podcast> doInBackground(Void... params){
		    Activity activity = getActivity();
		    if(activity == null)
		    	return new ArrayList<Podcast>();
		    String podcastSrc = (String)getArguments().get(PODCAST_SRC);
		    return new PodcastListFetcher().fetchPodcasts(podcastSrc);
		}
	
		@Override
		protected void onPostExecute(ArrayList<Podcast> podcasts){
			mNPRPodcastList = podcasts;
			Log.i(TAG,"News ArrayList Received: " + mNPRPodcastList);
			setupAdapter();
		} 
	}
	
	public void setupAdapter(){
		if(getActivity() == null  || getListView() == null) return;
		if(mNPRPodcastList != null){
			setListAdapter(new ArrayAdapter<Podcast>(getActivity(),
                    android.R.layout.simple_list_item_1,mNPRPodcastList));
		} else {
			setListAdapter(null);
		}
	}

}
