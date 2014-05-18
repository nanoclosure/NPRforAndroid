package edu.nanoracket.nprforandroid.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.nanoracket.nprforandroid.program.Program;
import edu.nanoracket.nprforandroid.program.ProgramListFetcher;
import edu.nanoracket.nprforandroid.ui.activity.PodcastListActivity;

public class ProgramListFragment extends ListFragment {

    private static final String TAG = "NPRProgramsFragment";
    public static ArrayList<Program> mPrograms;

    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setHasOptionsMenu(true);
        new FetchProgramsTask().execute();

    }

    private class FetchProgramsTask extends AsyncTask<Void, Void, ArrayList<Program>> {

        @Override
        protected ArrayList<Program> doInBackground(Void... params){
            Activity activity = getActivity();
            if(activity == null)
                return new ArrayList<Program>();
            return new ProgramListFetcher().fetchPrograms();
        }

        @Override
        protected void onPostExecute(ArrayList<Program> programs){
            mPrograms = programs;
            Log.i(TAG,"Program List Received: " + mPrograms);
            setupAdapter();
        }
    }

    public void setupAdapter(){
        if(getActivity() == null  || getListView() == null) return;
        if(mPrograms != null){
            setListAdapter(new ArrayAdapter<Program>(getActivity(),
                    android.R.layout.simple_list_item_1, mPrograms));
        } else {
            setListAdapter(null);
        }
    }
     
     @Override
 	 public void onListItemClick(ListView l, View v, int position, long id) {
         
 		 Program program = (Program)mPrograms.get(position);
         Log.i(TAG,"News selected: " + program);
         Intent i = new Intent(getActivity(), PodcastListActivity.class);
         i.putExtra(PodcastListFragment.PODCAST_SRC,program.getSource());
         startActivity(i);
      }
}
