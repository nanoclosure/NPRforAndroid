package edu.nanoracket.npr.music;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.home.YourAppMainActivity;
import edu.nanoracket.npr.model.Podcast;
import edu.nanoracket.npr.lab.PodcastLab;
import edu.nanoracket.npr.adapter.PodcastListAdapter;
import edu.nanoracket.npr.ui.fragment.ProgramListFragment;
import edu.nanoracket.npr.util.HttpHelper;
import edu.nanoracket.npr.util.XMLParser;

public class MusicListFragment extends ListFragment implements MediaPlayerControl {

    private static final String TAG = "PodcastListFragment";
    public static final String PODCAST_SRC =
            "edu.nanoracket.npr.podcastListFragment.src";
    public static final String PODCAST_IMAGE = "imagePosition" ;
    public static final String PODCAST_PROGRAM = "program";
    private static final String PROGRAM_NAME = "programName";

    private ArrayList<Podcast> podcasts;
    private String programName;
    private PodcastListAdapter listAdapter;
    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;

    public static MusicListFragment newInstance(String podcastSrc,
                                                String programName,
                                                int imagePosition){
        Bundle args = new Bundle();
        args.putString(PODCAST_SRC, podcastSrc);
        args.putString(PODCAST_PROGRAM, programName);
        args.putInt(PODCAST_IMAGE, imagePosition);
        MusicListFragment fragment = new MusicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        podcasts = PodcastLab.getInstance().getPodcastsList();
        new FetchPodcastsTask().execute();

        programName = getArguments().getString(PODCAST_PROGRAM);
        if(programName == null && SavedInstanceState != null){
            programName = SavedInstanceState.getString(PROGRAM_NAME);
        }
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(programName);
        Log.i(TAG, "onCreated is called");
    }

    @Override
    public void onStart(){
        super.onStart();
        setController();
        if(!musicBound){
            if(playIntent == null){
                playIntent = new Intent(getActivity(), MusicService.class);
                getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
                getActivity().startService(playIntent);
            }
        }
        Log.i(TAG, "onStart is called");
    }

    @Override
    public void onPause(){
        super.onPause();
        paused=true;
        Log.i(TAG, "onPause is called");
    }

    @Override
    public void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
        Log.i(TAG, "onResume is called");
    }

    @Override
    public void onStop() {
        super.onStop();
        controller.hide();
        if(musicBound){
            getActivity().unbindService(musicConnection);
            musicBound = false;
            musicService = null;
        }
        playIntent = null;
        Log.i(TAG, "onStop is called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy is called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(PROGRAM_NAME, programName);
    }

    android.os.Handler handler = new android.os.Handler();

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)iBinder;
            musicService = binder.getService();
            musicService.setList(podcasts);
            musicBound = true;
            if(musicService.isPlaying()){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        controller.show(1000*60*2);
                    }
                });
            }
            musicService.setProgramUrl(getArguments().getString(PODCAST_SRC));
            musicService.setProgramName(getArguments().getString(PODCAST_PROGRAM));
            musicService.setProgramPosition(getArguments().getInt(PODCAST_IMAGE));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;
        }
    };

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(musicBound){
            podcastSelected(position);
        }
    }

    public void podcastSelected(int position){
        musicService.setPodcast(position);
        musicService.playPodcast();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }else{
            setController();
        }
        controller.show(0);
    }

    private class FetchPodcastsTask extends AsyncTask<Void, Void, ArrayList<Podcast>> {

        @Override
        protected ArrayList<Podcast> doInBackground(Void... params){
            Activity activity = getActivity();
            ArrayList<Podcast> podcasts = new ArrayList<Podcast>();
            if(activity == null)
                return podcasts;
            String podcastSrc = (String)getArguments().get(PODCAST_SRC);
            try {
                String xmlStr = new HttpHelper().sendURLConnectionRequest(podcastSrc);
                podcasts = new XMLParser(getActivity()).parsePodcast(xmlStr);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return podcasts;
        }

        @Override
        protected void onPostExecute(ArrayList<Podcast> podcasts){
            MusicListFragment.this.podcasts = podcasts;
            Log.i(TAG,"Podcast ArrayList Received: " + MusicListFragment.this.podcasts);
            setupAdapter();
        }
    }

    public void setupAdapter(){
        if(getActivity() == null  || getListView() == null) return;
        listAdapter = new PodcastListAdapter(getActivity(), podcasts,
                getArguments().getInt(PODCAST_IMAGE));
        if(podcasts != null){
            setListAdapter(listAdapter);
        } else {
            setListAdapter(null);
        }
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicService!=null && musicBound && musicService.isPlaying())
            return musicService.getPosition();
        else return 0;
    }

    @Override
    public int getDuration() {
        if(musicService!=null && musicBound && musicService.isPlaying())
            return musicService.getDuration();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicService!=null && musicBound)
            return musicService.isPlaying();
        return false;
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicService.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }

    @Override
    public void start() {
        musicService.go();
    }

    private void setController() {
        controller = new MusicController(getActivity());
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(getListView());
        controller.setEnabled(true);
    }

    private void playNext(){
        musicService.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    private void playPrev(){
        musicService.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_musiclistfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_stop_service){
            if (musicBound) {
                getActivity().unbindService(musicConnection);
                getActivity().stopService(playIntent);
                controller.hide();
                musicBound = false;
                musicService = null;
                Intent i = new Intent(getActivity(), YourAppMainActivity.class);
                i.putExtra(ProgramListFragment.ID, 888);
                startActivity(i);
                return true;
            }
        }else if(id == android.R.id.home){
            Intent i = new Intent(getActivity(), YourAppMainActivity.class);
            i.putExtra(ProgramListFragment.ID, 888);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
