package edu.nanoracket.npr.podcast;

import java.util.ArrayList;

public class PodcastList {

	private static PodcastList sPodcastList;
	private ArrayList<Podcast> mPodcastList;
	//private Context mAppContext;
	
	private PodcastList(){
		//mAppContext = context;
		mPodcastList = new ArrayList<Podcast>();
	}
	
	public static PodcastList get(){
		if(sPodcastList == null){
			sPodcastList = new PodcastList();
		}
		return sPodcastList;
	}
	
	public ArrayList<Podcast> getPodcastsList(){
		return mPodcastList;
	}

    public Podcast getPodcast(String url){
		for(Podcast podcast : mPodcastList){
			if(podcast.getUrl().equals(url))
				return podcast;
			}
		return null;
	}

	public void addPodcast(Podcast podcast){
        mPodcastList.add(podcast);
	}
}
