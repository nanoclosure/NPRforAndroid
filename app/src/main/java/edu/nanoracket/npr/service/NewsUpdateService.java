package edu.nanoracket.npr.service;

import android.app.*;
import android.content.Intent;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.home.NewsListFragment;
import edu.nanoracket.npr.home.YourAppMainActivity;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.nanoracket.npr.model.Story;
import edu.nanoracket.npr.util.HttpHelper;
import org.json.JSONObject;

public class NewsUpdateService extends IntentService {

    public static final String TAG = "NewsUpdateService";
    public static final int MINUTE = 60000;
    private HttpHelper httpHelper = new HttpHelper();
    private ArrayList<Story> stories = new ArrayList<Story>();

    public NewsUpdateService() {
       super("NewsUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = httpHelper.createURL("1001", "Stories");
        try {
            String storyJsonStr = httpHelper.sendURLConnectionRequest(url);
            stories = parseStoryJson(storyJsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String updatedTitle = stories.get(0).getTitle();
        String savedTitle = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(NewsListFragment.NEWS_NEW_TITLE, null);

        if(!updatedTitle.equals(savedTitle)){
            Intent notiIntent = new Intent(this, YourAppMainActivity.class);
            notiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                    notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            Notification notification = builder.setSmallIcon(R.drawable.ic_launcher)
                    .setTicker(updatedTitle)
                    .setAutoCancel(true)
                    .setContentTitle("New Stories")
                    .setContentText(updatedTitle)
                    .setContentIntent(pendInt)
                    .build();
            NotificationManager notificationManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
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

    private ArrayList<Story> parseStoryJson(String jsonStr) throws JSONException {
        ArrayList<Story> stories = new ArrayList<Story>();
        JSONObject storiesJsonObject = new JSONObject(jsonStr);
        JSONArray storiesJsonArray = storiesJsonObject.getJSONObject("list").getJSONArray("story");
        for(int i = 0; i< storiesJsonArray.length(); i++){
            JSONObject storyJson = storiesJsonArray.getJSONObject(i);
            Story story = new Story();
            story.setId(storyJson.getString("id"));
            story.setTitle(storyJson.getJSONObject("title").getString("$text"));
            if(storyJson.has("teaser")){
                story.setTeaser(storyJson.getJSONObject("teaser").getString("$text"));
            }
            if(storyJson.has("slug")){
                story.setSlug(storyJson.getJSONObject("slug").getString("$text"));
            }
            story.setPubDate(storyJson.getJSONObject("pubDate").getString("$text"));
            if(storyJson.has("byline")){
                JSONArray bylineJson = storyJson.getJSONArray("byline");
                Story.Byline byline = new Story.Byline(
                        bylineJson.getJSONObject(0).getJSONObject("name").getString("$text"));
                story.setByline(byline);
            }
            if(storyJson.has("image")){
                JSONObject imageJson = storyJson.getJSONArray("image").getJSONObject(0);
                String imageId = imageJson.getString("id");
                String imageType = imageJson.getString("type");
                String imageWidth = imageJson.getString("width");
                String imageSrc = imageJson.getString("src");
                String imageHasBorder = imageJson.getString("hasBorder");
                Story.Image image = new Story.Image(imageId, imageType,imageWidth, imageSrc,
                        imageHasBorder);
                story.setImage(image);
            }

            if(storyJson.has("textWithHtml")){
                JSONArray paraJsonArray = storyJson.getJSONObject("textWithHtml")
                        .getJSONArray("paragraph");
                HashMap<Integer, String> paragraphsHtml = new HashMap<Integer, String>();
                Story.TextWithHtml textWithHtml;

                for(int k = 0; k < paraJsonArray.length(); k++){
                    paragraphsHtml.put(k,
                            paraJsonArray.getJSONObject(k).getString("$text"));
                }
                textWithHtml = new Story.TextWithHtml(paragraphsHtml);
                story.setTextWithHtml(textWithHtml);
            }
            stories.add(story);
        }

        return stories;
    }
}
