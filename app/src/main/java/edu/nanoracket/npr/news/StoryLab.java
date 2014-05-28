package edu.nanoracket.npr.news;

import android.content.Context;

import java.util.ArrayList;

public class StoryLab {
    private Context context;
    private static StoryLab storyLab;
    private ArrayList<Story> storyList;

    private StoryLab(Context context){
        this.context = context;
        storyList = new ArrayList<Story>();
    }

    public static StoryLab getInstance(Context context){
        if(storyLab == null){
            storyLab = new StoryLab(context.getApplicationContext());
        }

        return storyLab;
    }

    public ArrayList<Story> getStoryList(){

        return storyList;
    }

    public Story getStory(String id){
        for(Story story : storyList){
            if(story.getId().equals(id)){
                return story;
            }
        }
        return null;
    }

    public void clearStories(){
        if(!storyList.isEmpty()){
            storyList.clear();
        }
    }

    public void addStory(Story story){
        storyList.add(story);
    }
}
