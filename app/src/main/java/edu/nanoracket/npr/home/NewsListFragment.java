package edu.nanoracket.npr.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.model.Story;
import edu.nanoracket.npr.lab.StoryLab;
import edu.nanoracket.npr.adapter.StoryListAdapter;
import edu.nanoracket.npr.ui.activity.StoryActivity;
import edu.nanoracket.npr.ui.fragment.StoryFragment;
import edu.nanoracket.npr.ui.view.LoadMoreNewsListView;
import edu.nanoracket.npr.util.HttpHelper;
import edu.nanoracket.npr.util.JSONParser;

public class NewsListFragment extends Fragment implements
        LoadMoreNewsListView. OnLoadMoreListener, LoadMoreNewsListView.OnItemClickListener{
    public static final String TAG = "NewsListFragment";
    public static final String NEWS_TOPIC_ID = "news_topic_id";
    public static final String NEWS_TOPIC = "newsTopic";

    private static final String NUM_RESULTS = "10" ;
    private LoadMoreNewsListView loadMoreNewsListView;
    private ArrayList<Story> mStories;
    private StoryLab storyLab;
    private StoryListAdapter adapter = null;
    private int startNum;
    private static String newTopicId;

    public static NewsListFragment newInstance(String id, String topic){
        Bundle args = new Bundle();
        args.putString(NEWS_TOPIC_ID, id);
        args.putString(NEWS_TOPIC, topic);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NewsListFragment() {
        startNum = 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        String topic = getArguments().getString(NEWS_TOPIC);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        if(topic != null){
            actionBar.setTitle(topic);
        }
        storyLab = StoryLab.getInstance(getActivity());
        newTopicId = getArguments().getString(NEWS_TOPIC_ID);
        new LoadingStoriesTask().execute(newTopicId, Integer.toString(startNum));
        startNum += Integer.parseInt(NUM_RESULTS);
        Log.i(TAG, "on created is called");
    }

    @Override
    public void onResume(){
        super.onResume();
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist, container, false);
        loadMoreNewsListView = (LoadMoreNewsListView)view.findViewById(android.R.id.list);
        setupAdapter();
        loadMoreNewsListView.setOnLoadMoreListener(this);
        loadMoreNewsListView.setOnItemClickListener(this);
        return view;
    }

    public void setupAdapter(){
        if(getActivity() == null || mStories == null ) return;

        if(mStories != null){
            adapter = new StoryListAdapter(getActivity(), mStories);
            loadMoreNewsListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            loadMoreNewsListView.setAdapter(null);
        }
    }

    @Override
    public void onLoadMore() {
        new LoadingStoriesTask().execute(newTopicId, Integer.toString(startNum));
        startNum += Integer.parseInt(NUM_RESULTS);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Story story = adapter.getItem(i);
        Log.i(TAG, "Story id is " + story.getId());
        Intent intent = new Intent(getActivity(), StoryActivity.class);
        intent.putExtra(StoryFragment.STORY_ID, story.getId());
        startActivity(intent);
    }

    private class LoadingStoriesTask extends AsyncTask<String, Void, StoryLab> {

        @Override
        protected StoryLab doInBackground(String... params){
            HttpHelper helper = new HttpHelper();
            String url = helper.createURL(params[0], params[1]);
            try {
                String storyJsonStr = helper.sendURLConnectionRequest(url);
                return new JSONParser(getActivity()).parseStoryJson(storyJsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(StoryLab storyLab){
            mStories = StoryLab.getInstance(getActivity()).getStoryList();
            if(adapter == null){
                setupAdapter();
            } else if(adapter != null) {
                adapter.notifyDataSetChanged();
                loadMoreNewsListView.onLoadMoreComplete();
            }
            //loadMoreNewsListView.onLoadMoreComplete();
            super.onPostExecute(storyLab);
        }
    }
}
