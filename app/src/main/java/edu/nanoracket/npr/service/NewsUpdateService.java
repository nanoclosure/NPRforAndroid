package edu.nanoracket.npr.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import edu.nanoracket.npr.model.Story;
import edu.nanoracket.npr.util.HttpHelper;
import edu.nanoracket.npr.util.JSONParser;

public class NewsUpdateService extends IntentService {

    public static final String TAG = "NewsUpdateService";
    public static final int MINUTE = 60000;

    private static ArrayList<Story> mStories;
    private HttpHelper httpHelper = new HttpHelper();

    public NewsUpdateService() {
        super("NewsUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = httpHelper.createURL("1001", "Stories");
        try {
            String storyJsonStr = httpHelper.sendURLConnectionRequest(url);
            mStories = new JSONParser(getApplicationContext()).parseStoryJson(storyJsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "onHandleIntent is called");
    }

    public static void setServiceAlarm(Context context, int updatePref){
        Intent intent = new Intent(context, NewsUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                updatePref*MINUTE, pendingIntent);
    }
}
