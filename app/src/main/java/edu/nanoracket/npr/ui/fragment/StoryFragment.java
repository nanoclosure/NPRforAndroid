package edu.nanoracket.npr.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.news.Story;
import edu.nanoracket.npr.news.StoryLab;
import edu.nanoracket.npr.util.StringToDateUtils;
import it.sephiroth.android.library.picasso.Picasso;

public class StoryFragment extends Fragment {

    public static final String TAG = "StoryFragment";
    public static final String STORY_ID = "story_id";
    private TextView storyTitleTextView, storyBylineTextView, storyDateTextView;
    private ImageView storyImageView;
    private WebView storyWebView;
    private Story story;

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
        storyDateTextView.setText(new StringToDateUtils().getPubDate(story.getPubDate()));

        Log.i(TAG, "Story Image url is: " + story.getImage().getSrc());

        Picasso.with(getActivity().getApplicationContext())
                .load(story.getImage().getSrc())
                //.resize(720, 480)
                //.fit()
                //.centerCrop()
                .into(storyImageView);

        storyWebView.loadDataWithBaseURL(null,getTextHtml(story),
                "text/html","utf-8", null);

        /*storyWebView.loadDataWithBaseURL(null,String.format(HTML_FORMAT,getTextHtml(story)),
                "text/html","utf-8", null);*/

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

    // WebView is default black text.
    // Also add formatting for the image, if there is one.
    private static final String HTML_FORMAT =
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">" +
                    "<html><head><title></title>" +
                    "<style type=\"text/css\">" +
                    "body {color:#000; margin:0; font-size:12pt;}" +
                    "a {color:blue}" +
                    ".teaser {font-size: 10pt}" +
                    "#story-icon {width: 100px; float:left; " +
                    "margin-right: 6pt; margin-bottom: 3pt;}" +
                    "#story-icon img {vertical-align: middle; width: 100%%;}" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "%s" +
                    "</body></html>";

}
