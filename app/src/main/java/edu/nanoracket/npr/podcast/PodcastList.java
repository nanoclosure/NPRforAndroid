package edu.nanoracket.npr.podcast;

import java.util.ArrayList;

public class PodcastList {
	private static PodcastList sNPRPodcastList;
	private ArrayList<Podcast> mPodcastList;
	//private Context mAppContext;
	
	private PodcastList(){
		//mAppContext = context;
		mPodcastList = new ArrayList<Podcast>();
	}
	
	public static PodcastList get(){
		if(sNPRPodcastList == null){
			sNPRPodcastList = new PodcastList();
		}
		
		return sNPRPodcastList;
	}
	
	public ArrayList<Podcast> getNPRPodcastsList(){
		return mPodcastList;
	}
	
	public void clearNPRPodcastList(){
		if(mPodcastList.size()>0)
			mPodcastList.clear();
	}
	
	public Podcast getNPRPodcast(String url){
		for(Podcast podcast : mPodcastList){
			if(podcast.getUrl().equals(url))
				return podcast;
			}
		return null;
	}

	
	public void addNPRPodcast(Podcast podcast){
		mPodcastList.add(podcast);
	}

}
