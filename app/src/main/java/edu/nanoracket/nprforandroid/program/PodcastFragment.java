package edu.nanoracket.nprforandroid.program;

import java.io.IOException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import edu.nanoracket.nprforandroid.R;
import edu.nanoracket.nprforandroid.podcast.Podcast;
import edu.nanoracket.nprforandroid.podcast.PodcastList;
import edu.nanoracket.nprforandroid.podcast.Utilities;

public class PodcastFragment extends Fragment implements
		OnSeekBarChangeListener {
	private static final String TAG = "ProPodcastFragment";
	public static final String PODCAST_URL= "podcast.url";
	
	private ImageView mPodcastImageView;
	private TextView mTitleTextView,mDurationTextView,mCurrenTextView;
	static Podcast mPodcast;
	private ImageButton mPlayPauseButton,mForwardButton,mReverseButton,mReverse30Button;
	private SeekBar mProgressBar;
	
	private MediaPlayer mMediaPlayer;
	private int CurPlayTime;
	private Utilities utils;
	
	private Handler mHandler = new Handler();
	
	public static PodcastFragment newInstance(String url) {
		Bundle args = new Bundle();
		args.putString(PODCAST_URL, url);
		
		PodcastFragment fragment = new PodcastFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        String url = getArguments().getString(PODCAST_URL);
        mPodcast = PodcastList.get().getNPRPodcast(url);
        mMediaPlayer = new MediaPlayer();        
        utils = new Utilities();
        
		
		Log.i(TAG,"Poadcast Received: " + mPodcast);
		//mPlayButton.setEnabled(true);
		if(mPodcast.getGuid() != null){
        	preparePodcast();
        }
        
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_podcast, parent, false);
        
        mPodcastImageView = (ImageView)v.findViewById(R.id.podcast_image);
        
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
				//Log.i(TAG,"PreparedListener Called");
				//mMediaPlayer.start();
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
            //mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mPodcast.getGuid()));
			Log.i(TAG,"Poadcast Received: " + mPodcast.getUrl());
  			mMediaPlayer.prepareAsync();
			//mMediaPlayer.start();
  		} catch (IllegalArgumentException e) {
  			Log.e(TAG, "Illegal Arguments:", e);
  		} catch (IllegalStateException e) {
  			Log.e(TAG, "IllegalStateException:", e);
  		} catch (IOException e) {
  			Log.e(TAG, "Failed to fetch poadcast:", e);
  	    }
  	}
      
    public void updateProgressBar() {
          mHandler.postDelayed(mUpdateTimeTask, 100);        
    }	
      
 
  	private Runnable mUpdateTimeTask = new Runnable() {
  		   public void run() {
  			   long totalDuration = mMediaPlayer.getDuration();
  			   //Log.i(TAG,"Poadcast Duration: " + mPodcast.getDuration());

  			   //long totalDuration = utils.TimerToMilliSeconds(mPodcast.getDuration());
  			   long currentDuration = mMediaPlayer.getCurrentPosition();
  			  
  			   // Displaying Total Duration time
  			   mDurationTextView.setText(""+utils.milliSecondsToTimer(totalDuration));
  			   // Displaying time completed playing
  			   mCurrenTextView.setText(""+utils.milliSecondsToTimer(currentDuration));
  			   
  			   // Updating progress bar
  			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
  			   //Log.d("Progress", ""+progress);
  			   mProgressBar.setProgress(progress);
  			   
  			   // Running this thread after 100 milliseconds
  		       mHandler.postDelayed(this, 100);
  		   }
    };
  		
  	
  	@Override
  	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
  		 
  	}

  	/**
     * When user starts moving the progress handler
  	 * */
    @Override
  	public void onStartTrackingTouch(SeekBar seekBar) {
  		// remove message Handler from updating progress bar
  		mHandler.removeCallbacks(mUpdateTimeTask);
     }
  	
  	 /**
  	 * When user stops moving the progress hanlder
  	 * */
  	 @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
  		mHandler.removeCallbacks(mUpdateTimeTask);
  		int totalDuration = mMediaPlayer.getDuration();
  		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
  		
  		// forward or backward to certain seconds
  		mMediaPlayer.seekTo(currentPosition);
  		
  		// update timer progress again
  		updateProgressBar();
     }

}
