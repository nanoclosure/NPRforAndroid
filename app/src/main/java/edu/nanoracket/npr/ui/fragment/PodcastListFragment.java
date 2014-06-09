package edu.nanoracket.npr.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import edu.nanoracket.npr.podcast.Podcast;
import edu.nanoracket.npr.podcast.PodcastLab;
import edu.nanoracket.npr.podcast.PodcastListAdapter;
import edu.nanoracket.npr.ui.activity.PodcastActivity;
import edu.nanoracket.npr.util.HttpHelper;
import edu.nanoracket.npr.util.XMLParser;

public class PodcastListFragment extends ListFragment {

	private static final String TAG = "PodcastListFragment";
	public static final String PODCAST_SRC = 
			"edu.nanoracket.npr.podcastListFragment.src";
    public static final String PODCAST_IMAGE = "imagePosition" ;
    public static final String PODCAST_PROGRAM = "program";

    public ArrayList<Podcast> podcasts;
    private String programName;
    public PodcastListAdapter listAdapter;
	
	public static PodcastListFragment newInstance(String podcastSrc, String programName,
                                                  int imagePostion){
		Bundle args = new Bundle();
		args.putString(PODCAST_SRC, podcastSrc);
        args.putString(PODCAST_PROGRAM, programName);
        args.putInt(PODCAST_IMAGE, imagePostion);
		PodcastListFragment fragment = new PodcastListFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		setHasOptionsMenu(true);
        setRetainInstance(true);
        programName = getArguments().getString(PODCAST_PROGRAM);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(programName);
		podcasts = PodcastLab.getInstance().getPodcastsList();
		new FetchPodcastsTask().execute();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Podcast podcast = podcasts.get(position);
        Log.i(TAG,"Podcasts selected: " + podcast);
        Intent i = new Intent(getActivity(), PodcastActivity.class);
        i.putExtra(PodcastFragment.PODCAST_URL,podcast.getUrl());
        i.putExtra(PodcastFragment.PODCAST_PROGRAM,programName);
        Log.i(TAG,"Podcasts URL selected: " + podcast.getUrl());
        startActivity(i);
    }
	
    private class FetchPodcastsTask extends AsyncTask<Void, Void, ArrayList<Podcast>> {

		@Override
		protected ArrayList<Podcast> doInBackground(Void... params){
		    Activity activity = getActivity();
            ArrayList<Podcast> podcasts = new ArrayList<Podcast>();
		    if(activity == null)
		    	return podcasts;
		    String podcastSrc = (String)getArguments().get(PODCAST_SRC);
            try {
                String xmlStr = new HttpHelper().sendURLConnectionRequest(podcastSrc);
                podcasts = new XMLParser(getActivity()).parsePodcast(xmlStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return podcasts;
		}

		@Override
		protected void onPostExecute(ArrayList<Podcast> podcasts){
			PodcastListFragment.this.podcasts = podcasts;
			Log.i(TAG,"Podcast ArrayList Received: " + PodcastListFragment.this.podcasts);
			setupAdapter();
		} 
	}
	
	public void setupAdapter(){
		if(getActivity() == null  || getListView() == null) return;
        listAdapter = new PodcastListAdapter(getActivity(), podcasts,
                                             getArguments().getInt(PODCAST_IMAGE));
		if(podcasts != null){
			setListAdapter(listAdapter);
		} else {
			setListAdapter(null);
		}
	}
}
