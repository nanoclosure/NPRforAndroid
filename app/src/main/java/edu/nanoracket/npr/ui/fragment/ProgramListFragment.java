package edu.nanoracket.npr.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.program.Program;
import edu.nanoracket.npr.program.ProgramListAdapter;
import edu.nanoracket.npr.ui.activity.PodcastListActivity;
import edu.nanoracket.npr.util.HttpHelper;
import edu.nanoracket.npr.util.JSONParser;
import edu.nanoracket.npr.util.JSONSerializer;

public class ProgramListFragment extends ListFragment {

    private static final String TAG = "ProgramListFragment";
    private static final String programURL =
            "http://www.npr.org/services/apps/iphone/news/programs.json";
    private static final String FILENAME = "programs.json";
    public static final String ID = "ProgramListFragment.ID";

    public ArrayList<Program> mPrograms = new ArrayList<Program>();
    private String mJSONString;
    public ProgramListAdapter listAdapter;
    public ListView listView;

    public static final int[] imagePositions = new int[] {
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

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        try {
            mPrograms = new JSONSerializer(getActivity().getApplicationContext(),FILENAME)
                            .loadPrograms();
        } catch (IOException e) {
            Log.e(TAG, "failed to open the file");
            new FetchProgramsTask().execute(programURL);
        } catch (JSONException e) {
            Log.e(TAG, "failed to convert json to class");
            new FetchProgramsTask().execute(programURL);
        }
        Log.i(TAG, "onCreated is called");
    }

    @Override
    public void onStart(){
        super.onStart();
        setupAdapter();
        Log.i(TAG, "onStart is called");
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mPrograms.size() > 0){
            Log.i(TAG, "the size of programs is " + mPrograms.size());
            try {
                new JSONSerializer(getActivity().getApplicationContext(),FILENAME)
                        .savePrograms(mPrograms);
            } catch (JSONException e) {
                Log.e(TAG, "failed to convert to JSONObject");
            } catch (IOException e) {
                Log.e(TAG, "failed to save to file.");
            }
        }
        Log.i(TAG, "onPause is called");
    }

    private class FetchProgramsTask extends AsyncTask<String, Void, ArrayList<Program>> {

        @Override
        protected ArrayList<Program> doInBackground(String... params){
            Activity activity = getActivity();
            ArrayList<Program> programs = new ArrayList<Program>();
            String jsonStr = null;
            if(activity == null)
                return programs;
            try {
                jsonStr = new HttpHelper().sendURLConnectionRequest(params[0]);
                new JSONSerializer(getActivity(), FILENAME).saveJSONString(jsonStr);
                programs = new JSONParser(getActivity()).parseProgramJson(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return programs;
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
        listAdapter = new ProgramListAdapter(getActivity(),mPrograms, imagePositions);
        if(mPrograms != null){
            setListAdapter(listAdapter);
        } else {
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
         i.putExtra(PodcastListFragment.PODCAST_IMAGE, imagePositions[position]);
         startActivity(i);
      }
}
