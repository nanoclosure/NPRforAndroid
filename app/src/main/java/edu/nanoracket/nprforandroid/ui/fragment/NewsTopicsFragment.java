package edu.nanoracket.nprforandroid.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;

import edu.nanoracket.nprforandroid.R;
import edu.nanoracket.nprforandroid.home.NewsListFragment;
import edu.nanoracket.nprforandroid.home.YourAppMainActivity;

public class NewsTopicsFragment extends ListFragment {

    private static final String TAG = "NewsTopicFragment";

    private String[] mTopics;
    private String[] mTopicsCode;
    HashMap<String, String> mNewsTopicMap;

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
        //setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mTopics));
    }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_topics, container, false);

        ListView listView = (ListView)view.findViewById(R.id.news_topics_listView);

        listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                mTopics));

        return view;
    }
*/
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String mNewsTopic = mTopics[position];
        Log.i(TAG, "News topic selected: " + mNewsTopic);
        Intent i = new Intent(getActivity(), YourAppMainActivity.class);
        i.putExtra(NewsListFragment.NEWS_TOPIC_ID, mNewsTopicMap.get(mNewsTopic));
        startActivity(i);
    }

}
