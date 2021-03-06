package edu.nanoracket.npr.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.home.NewsListFragment;
import edu.nanoracket.npr.home.YourAppMainActivity;

public class NewsTopicsFragment extends ListFragment {

    private static final String TAG = "NewsTopicFragment";

    private String[] mTopics;
    private String[] mTopicsCode;
    private HashMap<String, String> mNewsTopicMap;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setHasOptionsMenu(true);

        mTopics = getResources().getStringArray(R.array.news_topic);
        mTopicsCode = getResources().getStringArray(R.array.news_topic_code);
        mNewsTopicMap = new HashMap<String, String>();
        for (int i = 0; i < mTopics.length; i++) {
            mNewsTopicMap.put(mTopics[i], mTopicsCode[i]);
        }

        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                mTopics));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String mNewsTopic = mTopics[position];
        Log.i(TAG, "News topic selected: " + mNewsTopic);
        Intent i = new Intent(getActivity(), YourAppMainActivity.class);
        i.putExtra(NewsListFragment.NEWS_TOPIC_ID, mNewsTopicMap.get(mNewsTopic));
        i.putExtra(NewsListFragment.NEWS_TOPIC, mNewsTopic);
        startActivity(i);
    }
}
