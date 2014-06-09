package edu.nanoracket.npr.ui.fragment;

import java.io.IOException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.model.Podcast;
import edu.nanoracket.npr.lab.PodcastLab;
import edu.nanoracket.npr.util.CastUtils;

public class PodcastFragment extends Fragment implements
		OnSeekBarChangeListener {

	private static final String TAG = "ProPodcastFragment";
	public static final String PODCAST_URL= "podcast.url";
    public static final String PODCAST_PROGRAM = "program";

    private String programName;
	private ImageView mPodcastImageView;
	private TextView mTitleTextView,mDurationTextView,mCurrenTextView, mDescriptionTextView;
	static Podcast mPodcast;
	private ImageButton mPlayPauseButton,mForwardButton,mReverseButton,mReverse30Button;
	private SeekBar mProgressBar;
	private MediaPlayer mMediaPlayer;
	private int CurPlayTime;
	private CastUtils utils;
	private Handler mHandler = new Handler();
	
	public static PodcastFragment newInstance(String url, String progName) {
		Bundle args = new Bundle();
		args.putString(PODCAST_URL, url);
		args.putString(PODCAST_PROGRAM, progName);
		PodcastFragment fragment = new PodcastFragment();
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        programName = getArguments().getString(PODCAST_PROGRAM);
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle(programName);
        String url = getArguments().getString(PODCAST_URL);
        mPodcast = PodcastLab.getInstance().getPodcast(url);
        mMediaPlayer = new MediaPlayer();        
        utils = new CastUtils();
		Log.i(TAG,"Poadcast Received: " + mPodcast);
		if(mPodcast.getGuid() != null){
        	preparePodcast();
        }
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_podcast, parent, false);
        mPodcastImageView = (ImageView)v.findViewById(R.id.podcast_image);
        mDescriptionTextView = (TextView)v.findViewById(R.id.podcast_description);
        mDescriptionTextView.setText(getArguments().getString(PODCAST_PROGRAM));
        mTitleTextView= (TextView)v.findViewById(R.id.podcast_title);
        mDurationTextView = (TextView)v.findViewById(R.id.songTotalDurationLabel);
        mCurrenTextView = (TextView)v.findViewById(R.id.songCurrentDurationLabel);
        mCurrenTextView.setText("0:00");
        mTitleTextView.setText(mPodcast.getTitle());
		mDurationTextView.setText(mPodcast.getDuration());
        mProgressBar = (SeekBar)v.findViewById(R.id.play_progressBar);
        mReverseButton = (ImageButton)v.findViewById(R.id.podcast_rew_button);
        mReverseButton.setEnabled(false);
        mPlayPauseButton =(ImageButton)v.findViewById(R.id.podcast_play_button);
        mPlayPauseButton.setEnabled(false);
       
        mPlayPauseButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mMediaPlayer.isPlaying()){
					if(mMediaPlayer != null){
						mMediaPlayer.pause();
						CurPlayTime = mMediaPlayer.getCurrentPosition();
						mPlayPauseButton.setImageResource(R.drawable.pause_button_normal);
					}
				}else{
					if(mMediaPlayer != null){
						Log.i(TAG,"Resume Podcast");
						mMediaPlayer.start();
						mMediaPlayer.seekTo(CurPlayTime);
						mPlayPauseButton.setImageResource(R.drawable.play_button_pressed);
					}
				}
			}
		});
        
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
		      @Override
		      public void onPrepared(MediaPlayer mp) {
		    	mPlayPauseButton.setEnabled(true);
		    	mPlayPauseButton.setImageResource(R.drawable.play_button_pressed);
		 	    mp.start();
				mProgressBar.setProgress(0);
				mProgressBar.setMax(100);
				updateProgressBar();	
		       }
	    });
        
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mHandler.removeCallbacks(mUpdateTimeTask);
				mp.release();
				mPlayPauseButton.setImageResource(R.drawable.play_button_normal);
			}
		});
        mReverse30Button = (ImageButton)v.findViewById(R.id.podcast_stop_button);
        mForwardButton = (ImageButton)v.findViewById(R.id.podcast_ffwd_button);
        return v;
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mHandler.removeCallbacks(mUpdateTimeTask);
		mMediaPlayer.release();
	}
	
    
      
    public void  preparePodcast(){
  		try {
  			Log.i(TAG,"Media Player Called" );
          	mMediaPlayer.reset();
  			mMediaPlayer.setDataSource(mPodcast.getUrl());
			Log.i(TAG,"Podcast Received: " + mPodcast.getUrl());
  			mMediaPlayer.prepareAsync();
  		} catch (IllegalArgumentException e) {
  			Log.e(TAG, "Illegal Arguments:", e);
  		} catch (IllegalStateException e) {
  			Log.e(TAG, "IllegalStateException:", e);
  		} catch (IOException e) {
  			Log.e(TAG, "Failed to fetch podcast:", e);
  	    }
  	}
      
    public void updateProgressBar() {
          mHandler.postDelayed(mUpdateTimeTask, 100);        
    }	
      
 
  	private Runnable mUpdateTimeTask = new Runnable() {
  		   public void run() {
  			   long totalDuration = mMediaPlayer.getDuration();
  			   long currentDuration = mMediaPlayer.getCurrentPosition();
  			   mDurationTextView.setText(""+utils.milliSecondsToTimer(totalDuration));
  			   mCurrenTextView.setText(""+utils.milliSecondsToTimer(currentDuration));
  			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
  			   mProgressBar.setProgress(progress);
  		       mHandler.postDelayed(this, 100);
           }
    };
  		
  	
  	@Override
  	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {}

    @Override
  	public void onStartTrackingTouch(SeekBar seekBar) {
  		mHandler.removeCallbacks(mUpdateTimeTask);
     }
  	
  	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
  		mHandler.removeCallbacks(mUpdateTimeTask);
  		int totalDuration = mMediaPlayer.getDuration();
  		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
  		mMediaPlayer.seekTo(currentPosition);
  		updateProgressBar();
     }

}
