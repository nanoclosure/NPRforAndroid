package edu.nanoracket.npr.ui.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.data.ArticlesContract;
import edu.nanoracket.npr.model.Story;
import edu.nanoracket.npr.lab.StoryLab;
import edu.nanoracket.npr.util.DateUtils;
import edu.nanoracket.npr.util.StringUtils;

public class StoryFragment extends Fragment {

    public static final String TAG = "StoryFragment";
    public static final String STORY_ID = "story_id";

    private ShareActionProvider actionProvider;
    private TextView storyTitleTextView, storyBylineTextView, storyDateTextView;
    private ImageView storyImageView;
    private WebView storyWebView;
    private Story story;
    private String imagePath;

    public static StoryFragment newInstance(String id){
        Bundle args = new Bundle();
        args.putString(STORY_ID, id);
        StoryFragment storyFragment = new StoryFragment();
        storyFragment.setArguments(args);
        return storyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        String id = getArguments().getString(STORY_ID);
        Log.i(TAG, "Story id is " + id);
        story = StoryLab.getInstance(getActivity()).getStory(id);
        Log.i(TAG, "Story id is " + story.getId());
        Log.i(TAG, "Story title is " + story.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_story, parent, false);

        storyTitleTextView = (TextView)view.findViewById(R.id.storyTitle);
        storyBylineTextView = (TextView)view.findViewById(R.id.storyByline);
        storyDateTextView = (TextView)view.findViewById(R.id.storyDate);
        storyImageView = (ImageView)view.findViewById(R.id.storyImage);
        storyWebView = (WebView)view.findViewById(R.id.storyWebView);

        storyTitleTextView.setText(story.getTitle());
        storyBylineTextView.setText("By " + story.getByline().getName());
        storyDateTextView.setText(DateUtils.getPubDate(story.getPubDate()));
        Log.i(TAG, "Story Image url is: " + story.getImage().getSrc());

        Picasso.with(getActivity().getApplicationContext())
                .load(story.getImage().getSrc())
                .into(storyImageView);

        storyWebView.loadDataWithBaseURL(null,getTextHtml(story),
                "text/html","utf-8", null);
        return view;
    }

    public String getTextHtml(Story story){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Integer, String> entry : story.getTextWithHtml().getParagraphs().entrySet()){
            String paragraph = entry.getValue();
            paragraph = paragraph.replaceAll("<img .*/>", "");
            sb.append("<p>").append(paragraph).append("</p>");
        }
        return sb.toString();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_storyactivity, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        actionProvider.setShareIntent(getDefaultIntent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_save:
                Log.i(TAG, "save the article.");
                saveArticle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveArticle() {
        ContentValues values = new ContentValues();
        imagePath = StringUtils.convertStoryTitle(story.getTitle()) + ".png";
        values.put(ArticlesContract.Columns.TOPIC, story.getSlug());
        values.put(ArticlesContract.Columns.TITLE, story.getTitle());
        values.put(ArticlesContract.Columns.BYLINE, story.getByline().getName());
        values.put(ArticlesContract.Columns.PUB_DATE, DateUtils.getPubDate(story.getPubDate()));
        values.put(ArticlesContract.Columns.IMAGE_PATH, imagePath);
        values.put(ArticlesContract.Columns.NEWS_TEXT, getTextHtml(story));
        Picasso.with(getActivity()).load(story.getImage().getSrc()).into(target);
        Log.i(TAG, "save the image to: " + imagePath);
        new SaveArticleTask(getActivity().getContentResolver(),values).execute();
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        return intent;
    }

    static class SaveArticleTask extends AsyncTask<Void, Void, Uri>{
        private final ContentResolver resolver;
        private final ContentValues vals;

        public SaveArticleTask(ContentResolver resolver, ContentValues vals){
            this.resolver = resolver;
            this.vals = vals;
            Log.i(TAG, "Save article task is started.");
        }

        @Override
        public Uri doInBackground(Void... args){
            Uri uri = resolver.insert(ArticlesContract.URI, vals);
            Log.i(TAG, "The return uri is: " + uri);
            return uri;
        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OutputStream out = getActivity()
                                .openFileOutput(imagePath, Context.MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 75, out);
                        out.close();
                    } catch (FileNotFoundException e) {
                        Log.i(TAG, "can not save image to file.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {}

        @Override
        public void onPrepareLoad(Drawable drawable) {
            if(drawable != null){}
        }
    };
}
