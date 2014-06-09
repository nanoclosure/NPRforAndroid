package edu.nanoracket.npr.podcast;

import java.util.ArrayList;

public class PodcastLab {

	private static PodcastLab podcastLab;
	private ArrayList<Podcast> mPodcastList;

	private PodcastLab(){
        mPodcastList = new ArrayList<Podcast>();
	}
	
	public static PodcastLab getInstance(){
		if(podcastLab == null){
			podcastLab = new PodcastLab();
		}
		return podcastLab;
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

    public void clearPodcastList(){
        if(mPodcastList.size() != 0){
            mPodcastList.clear();
        }
    }

	public void addPodcast(Podcast podcast){
        mPodcastList.add(podcast);
	}
}
