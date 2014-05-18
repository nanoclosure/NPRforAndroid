package edu.nanoracket.nprforandroid.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import edu.nanoracket.nprforandroid.R;
import edu.nanoracket.nprforandroid.news.Story;
import edu.nanoracket.nprforandroid.news.StoryLab;
import edu.nanoracket.nprforandroid.news.StoryListAdapter;
import edu.nanoracket.nprforandroid.ui.activity.StoryActivity;
import edu.nanoracket.nprforandroid.ui.fragment.StoryFragment;
import edu.nanoracket.nprforandroid.ui.view.LoadMoreNewsListView;
import edu.nanoracket.nprforandroid.util.HttpHelper;
import edu.nanoracket.nprforandroid.util.JSONParser;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class NewsListFragment extends Fragment {
    public static final String TAG = "NewsListFragment";
    public static final String NEWS_TOPIC_ID = "news_topic_id";
    private static final String START_NUM = "stratNum";

    //public StoryLab storyLab = StoryLab.getInstance(getActivity());

    private StoryLab storyLab;
    private static final String NUMRESULTS = "10" ;
    private LoadMoreNewsListView loadMoreNewsListView;
    private static ArrayList<Story> mStories;
    private StoryListAdapter adapter = null;
    private int startNum;
    private static String newTopicId;

    public static NewsListFragment newInstance(String id){
        Bundle args = new Bundle();
        args.putString(NEWS_TOPIC_ID, id);
        //args.putInt(START_NUM, startNum);

        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public NewsListFragment() {
        // Required empty public constructor
        startNum = 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //stories = StoryLab.get().getStoryList();
        storyLab = StoryLab.getInstance(getActivity());
        newTopicId = getArguments().getString(NEWS_TOPIC_ID);
        //startNum = getArguments().getInt(START_NUM);
        new LoadingStoriesTask().execute(newTopicId, Integer.toString(startNum));
        startNum += Integer.parseInt(NUMRESULTS);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist, container, false);

       // loadMoreNewsListView = (LoadMoreNewsListView)view.findViewById(R.id.newslist);
        loadMoreNewsListView = (LoadMoreNewsListView)view.findViewById(android.R.id.list);

        setupAdapter();

        loadMoreNewsListView.setOnLoadMoreListener(new LoadMoreNewsListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new LoadingStoriesTask().execute(newTopicId, Integer.toString(startNum));
                startNum += Integer.parseInt(NUMRESULTS);
            }
        });

        loadMoreNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Story story = ((StoryListAdapter) getListAdapter()).getItem(i);
                //Story story = (Story)loadMoreNewsListView.getItemAtPosition(i);
                Story story = (Story)adapter.getItem(i);


                Log.i(TAG, "Story id is " + story.getId());

                Intent intent = new Intent(getActivity(), StoryActivity.class);
                intent.putExtra(StoryFragment.STORY_ID, story.getId());
                startActivity(intent);
            }
        });
        return view;
    }

    /*final private AdapterView.OnItemClickListener mOnClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            onListItemClick((LoadMoreNewsListView) parent, v, position, id);
        }
    };

    public void onListItemClick(LoadMoreNewsListView listView, View view, int position, long id) {
        Story story = ((StoryListAdapter) getListAdapter()).getItem(position);
        Log.i(TAG, "Story id is" + story.getId());

        Intent intent = new Intent(getActivity(), StoryActivity.class);
        intent.putExtra(StoryFragment.STORY_ID, story.getId());
        startActivity(intent);
    }*/



   /* @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        Story story = ((StoryListAdapter)getListAdapter()).getItem(position);

        Intent intent = new Intent(getActivity(),StoryActivity.class);
        intent.putExtra(StoryFragment.STORY_ID,story.getId());
        startActivity(intent);

    }*/

    public void setupAdapter(){
        if(getActivity() == null || mStories == null ) return;

        if(mStories != null){
            adapter = new StoryListAdapter(getActivity(), mStories);
            loadMoreNewsListView.setAdapter(adapter);
        } else {
            loadMoreNewsListView.setAdapter(null);
        }
    }

    private class LoadingStoriesTask extends AsyncTask<String, Void, ArrayList<Story>> {

        @Override
        protected ArrayList<Story> doInBackground(String... params){
            HttpHelper helper = new HttpHelper();
            String url = helper.createURL(params[0], params[1]);

            try {
                String stroiesJsonStr = helper.sendURLConnectionRequest(url);
                return new JSONParser(getActivity()).parseStoryJson(stroiesJsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Story> stories){
            mStories = stories;

            if(adapter == null){
                setupAdapter();
            } else if(adapter != null) {
                adapter.notifyDataSetChanged();
            }

            loadMoreNewsListView.onLoadMoreComplete();

            super.onPostExecute(stories);
            //setupAdapter();
        }
    }
}
