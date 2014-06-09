package edu.nanoracket.npr.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.model.Podcast;
import edu.nanoracket.npr.util.StringUtils;
import edu.nanoracket.npr.ui.activity.PodcastListActivity;
import edu.nanoracket.npr.ui.fragment.PodcastListFragment;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private static final String TAG = "MusicService";
    public static final String NOTIFICATION_PLAY = "edu.nanoracket.npr.music.notification.play";
    public static final String NOTIFICATION_PAUSE = "edu.nanoracket.npr.music.notification.pause";
    public static final String NOTIFICATION_NEXT = "edu.nanoracket.npr.music.notification.next";
    public static final String NOTIFICATION_PREVIOUS = "edu.nanoracket.npr.music.notification.previous";
    public static final String NOTIFICATION_STOP = "edu.nanoracket.npr.music.notification.stop";

    private MediaPlayer player;
    private RemoteViews notificationViews;
    private ArrayList<Podcast> podcasts;
    private int podcastPos;
    private final IBinder musicBind = new MusicBinder();
    private String podcastTitle;
    private static final int NOTIFY_ID = 1;
    private boolean shuffle = false;
    private Random rand;
    private ServiceReceiver receiver;
    private Notification notification;
    private NotificationManager notificationManager;
    private String programUrl;
    private String programName;
    private int programPosition;

    public MusicService() {}

    @Override
    public void onCreate(){
        super.onCreate();
        podcastPos = 0;
        rand = new Random();
        player = new MediaPlayer();
        receiver = new ServiceReceiver();
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Podcast> podcasts){
        this.podcasts = podcasts;
    }

    public class MusicBinder extends Binder {
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFICATION_PAUSE);
        filter.addAction(NOTIFICATION_NEXT);
        filter.addAction(NOTIFICATION_PLAY);
        filter.addAction(NOTIFICATION_PREVIOUS);
        filter.addAction(NOTIFICATION_STOP);
        registerReceiver(receiver, filter);

        notificationViews = new RemoteViews(this.getPackageName(),R.layout.npr_noti);

        Intent pauseIntent = new Intent(NOTIFICATION_PAUSE);
        PendingIntent pendPauseIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        notificationViews.setOnClickPendingIntent(R.id.noti_pause, pendPauseIntent);

        Intent playIntent = new Intent(NOTIFICATION_PLAY);
        PendingIntent pendPlayIntent = PendingIntent.getBroadcast(this, 0, playIntent, 0);
        notificationViews.setOnClickPendingIntent(R.id.noti_play, pendPlayIntent);

        Intent previousIntent = new Intent(NOTIFICATION_PREVIOUS);
        PendingIntent pendPreviousIntent = PendingIntent.getBroadcast(this, 0, previousIntent, 0);
        notificationViews.setOnClickPendingIntent(R.id.noti_previous, pendPreviousIntent);

        Intent nextIntent = new Intent(NOTIFICATION_NEXT);
        PendingIntent pendNextIntent = PendingIntent.getBroadcast(this, 0, nextIntent, 0);
        notificationViews.setOnClickPendingIntent(R.id.noti_next, pendNextIntent);

        Intent stopIntent = new Intent(NOTIFICATION_STOP);
        PendingIntent pendStopIntent = PendingIntent.getBroadcast(this, 0, stopIntent, 0);
        notificationViews.setOnClickPendingIntent(R.id.noti_cancel, pendStopIntent);

        return START_STICKY;
    }

    public void genPlayPauseIntent(String action){
        Intent playPauseIntent = new Intent(action);
        PendingIntent pendPlayPauseIntent = PendingIntent.getBroadcast(this, 0,
                                           playPauseIntent, PendingIntent.FLAG_ONE_SHOT);
        notificationViews.setOnClickPendingIntent(R.id.noti_pause, pendPlayPauseIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        return false;
    }

    @Override
    public void onDestroy(){
        player.stop();
        player.release();
        stopForeground(true);
        unregisterReceiver(receiver);
        stopSelf();
    }

    public void playPodcast(){
        player.reset();
        Podcast podcast = podcasts.get(podcastPos);
        podcastTitle = podcast.getTitle();
        String podcastUrl = podcast.getUrl();
        try {
            player.setDataSource(podcastUrl);
        } catch (IOException e) {
            Log.e(TAG, "Error setting data source.", e);
        }
        player.prepareAsync();
    }

    public void setPodcast(int podcastIndex){
        podcastPos = podcastIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(mediaPlayer.getCurrentPosition() > 0){
            mediaPlayer.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Log.v(TAG, "Playback error.");
        mediaPlayer.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        //mediaPlayer.pause();
        /*Intent notifiIntent = new Intent(this, PodcastListActivity.class);
        notifiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notifiIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play_button_pressed)
                .setTicker(podcastTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(podcastTitle);

        Notification npr_noti;*/
        notification = generateNonfiction();
        notification.flags = Notification.FLAG_NO_CLEAR;
        startForeground(NOTIFY_ID, notification);
    }

    private Notification generateNonfiction(){
        Intent notiIntent = new Intent(getApplicationContext(), PodcastListActivity.class);
        notiIntent.putExtra(PodcastListFragment.PODCAST_SRC, programUrl);
        notiIntent.putExtra(PodcastListFragment.PODCAST_PROGRAM, programName);
        notiIntent.putExtra(PodcastListFragment.PODCAST_IMAGE, programPosition);
        notiIntent.setAction(Intent.ACTION_MAIN);
        notiIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                      0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this);
        builder.setTicker(StringUtils.convertString(podcastTitle))
                .setSmallIcon(R.drawable.navdrawer_program)
                .setContent(notificationViews)
                .setOngoing(true)
                .setContentIntent(pendingIntent);
        return builder.build();
    }

    public int getPosition(){
        return player.getCurrentPosition();
    }

    public int getDuration(){
        return player.getDuration();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }

    public void seek(int pos){
        player.seekTo(pos);
    }

    public void go(){
        player.start();
    }

    public void playPrev(){
        podcastPos--;
        if(podcastPos < 0){
            podcastPos = podcasts.size()-1;
        }
        playPodcast();
    }

    public void playNext(){
        if(shuffle){
            int newPodcast = podcastPos;
            while (newPodcast == podcastPos){
                newPodcast = rand.nextInt(podcasts.size());
            }
            podcastPos = newPodcast;
        } else {
            podcastPos++;
            if(podcastPos >= podcasts.size()){
                podcastPos = 0;
            }
        }
        playPodcast();
    }

    public void setShuffle(){
        if(shuffle){
            shuffle = false;
        }else {
            shuffle = true;
        }
    }

    public void setProgramUrl(String url){
        programUrl = url;
    }

    public void setProgramName(String name){
        programName = name;
    }

    public void setPorgramPosition(int position){
        programPosition = position;
    }

    private class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(NOTIFICATION_PAUSE)){
                if(isPlaying()){
                    pausePlayer();
                }
            }else if(action.equals(NOTIFICATION_PLAY)){
                player.start();
            }else if(action.equals(NOTIFICATION_STOP)){
                stopSelf();
            }else if(action.equals(NOTIFICATION_PREVIOUS)){
                playPrev();
            }else if(action.equals(NOTIFICATION_NEXT)){
                playNext();
            }
        }
    }
}
