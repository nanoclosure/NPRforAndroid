package edu.nanoracket.npr.ui.fragment;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.newscast.Newscast;
import edu.nanoracket.npr.util.CastUtils;
import edu.nanoracket.npr.util.HttpHelper;
import edu.nanoracket.npr.util.XMLParser;


public class NewscastFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{

    public static final String TAG = "NewscastFragment";
    public static final String NEWSCAST_URL= "newscast.url";
    private static final String url = "http://www.npr.org/rss/podcast.php?id=500005";

    private ImageView mNewscastImageView;
    private TextView mTitleTextView,mDurationTextView, mCurrentTextView;
    static Newscast mNewscast;
    private ImageButton mPlayPauseButton,mForwardButton,mReverseButton,mReverse30Button;
    private SeekBar mProgressBar;

    private MediaPlayer mMediaPlayer;
    private int mCurPlayTime;
    private CastUtils utils;

    private Handler mHandler = new Handler();
    private int CurPlayTime;

    public static NewscastFragment newInstance(String url) {
        NewscastFragment fragment = new NewscastFragment();
        Bundle args = new Bundle();
        args.putString(NEWSCAST_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public NewscastFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        new FetchNewscastTask().execute(url);

        mMediaPlayer = new MediaPlayer();
        utils = new CastUtils();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newscast, parent, false);
        mNewscastImageView = (ImageView)view.findViewById(R.id.podcast_image);
        mTitleTextView= (TextView)view.findViewById(R.id.podcast_title);
        mDurationTextView = (TextView)view.findViewById(R.id.songTotalDurationLabel);
        mCurrentTextView = (TextView)view.findViewById(R.id.songCurrentDurationLabel);
        mCurrentTextView.setText("0:00");
        mProgressBar = (SeekBar)view.findViewById(R.id.play_progressBar);
        mReverseButton = (ImageButton)view.findViewById(R.id.podcast_rew_button);
        mReverseButton.setEnabled(false);
        mPlayPauseButton =(ImageButton)view.findViewById(R.id.podcast_play_button);
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

        mReverse30Button = (ImageButton)view.findViewById(R.id.podcast_stop_button);
        mForwardButton = (ImageButton)view.findViewById(R.id.podcast_ffwd_button);
        return view;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mMediaPlayer.release();

    }

    private class FetchNewscastTask extends AsyncTask<String, Void, Newscast> {

        @Override
        protected Newscast doInBackground(String... params){
            Activity activity = getActivity();
            Newscast newscast = new Newscast();
            if(activity == null)
                return newscast;
            try {
                String xmlStr = new HttpHelper().sendURLConnectionRequest(params[0]);
                newscast = new XMLParser(getActivity()).parseNewscast(xmlStr);
            } catch (IOException e) {
                Log.i(TAG, "failed to fetch newscast xml feed.");
            } catch (XmlPullParserException e) {
                Log.i(TAG, "parse error.");
            }
            return newscast;
        }

        @Override
        protected void onPostExecute(Newscast newscast){
            mNewscast = newscast;
            mTitleTextView.setText(mNewscast.getTitle());
            mDurationTextView.setText(mNewscast.getDuration());
            Log.i(TAG, "Newscast Received: " + mNewscast);
            if(mNewscast.getGuid() != null){
                prepareNewscast();
            }
        }
    }

    public void  prepareNewscast(){
        try {
            Log.i(TAG,"Media Player Called" );
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mNewscast.getGuid());
            Log.i(TAG,"Newscast Received: " + mNewscast.getGuid());
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Illegal Arguments:", e);
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException:", e);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch newscast: ", e);
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
            mCurrentTextView.setText("" + utils.milliSecondsToTimer(currentDuration));
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
