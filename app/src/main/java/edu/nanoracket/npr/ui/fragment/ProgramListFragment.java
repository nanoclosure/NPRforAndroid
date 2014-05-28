package edu.nanoracket.npr.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.program.Program;
import edu.nanoracket.npr.program.ProgramListAdapter;
import edu.nanoracket.npr.program.ProgramListFetcher;
import edu.nanoracket.npr.ui.activity.PodcastListActivity;

public class ProgramListFragment extends ListFragment {

    private static final String TAG = "NPRProgramsFragment";
    public static ArrayList<Program> mPrograms;
    public ProgramListAdapter listAdapter;
    public ListView listView;

    public static final int[] imagePostions = new int[] {
            R.drawable.all_things_considered,
            R.drawable.cartalk,
            R.drawable.tedradiohour,
            R.drawable.planetmoney,
            R.drawable.snapjudgment,
            R.drawable.bullseye,
            R.drawable.onpoint,
            R.drawable.latinousa,
            R.drawable.radiolab,
            R.drawable.popculture,
            R.drawable.onthemedia,
            R.drawable.dianerehm,
            R.drawable.intelligence,
            R.drawable.stateofreunion,
            R.drawable.yourhealth,
            R.drawable.thistleshamrock,
            R.drawable.worldcafe,
            R.drawable.fromthetop,
            R.drawable.onlyagame
    };


    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setHasOptionsMenu(true);
        new FetchProgramsTask().execute();
        //setupAdapter();
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

        listView = getListView();
        listAdapter = new ProgramListAdapter(getActivity(),mPrograms, imagePostions);
        if(mPrograms != null){
            //listView.setAdapter(listAdapter);
            setListAdapter(listAdapter);
        } else {
            //listView.setAdapter(null);
            setListAdapter(null);
        }
    }
     
     @Override
 	 public void onListItemClick(ListView l, View v, int position, long id) {
         
 		 Program program = (Program)mPrograms.get(position);
         Log.i(TAG,"News selected: " + program);
         Intent i = new Intent(getActivity(), PodcastListActivity.class);
         i.putExtra(PodcastListFragment.PODCAST_SRC, program.getSource());
         i.putExtra(PodcastListFragment.PODCAST_PROGRAM, program.getName());
         i.putExtra(PodcastListFragment.PODCAST_IMAGE, imagePostions[position]);
         startActivity(i);
      }
}
