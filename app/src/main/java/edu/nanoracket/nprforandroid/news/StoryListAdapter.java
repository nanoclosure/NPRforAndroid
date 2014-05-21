package edu.nanoracket.nprforandroid.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import edu.nanoracket.nprforandroid.R;
import edu.nanoracket.nprforandroid.util.StringToDateUtils;
import it.sephiroth.android.library.picasso.Picasso;

public class StoryListAdapter extends ArrayAdapter<Story> {

    public StoryListAdapter(Context context, ArrayList<Story> stories){
        super(context, R.layout.story_list_item, stories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Story story = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                          .inflate(R.layout.story_list_item,parent,false);
        }

        TextView topicTextView = (TextView)convertView.findViewById(R.id.story_topic);
        ImageView storyImageView = (ImageView)convertView.findViewById(R.id.story_imageView);
        TextView titleTextView = (TextView)convertView.findViewById(R.id.story_title);
        TextView authorTextView = (TextView)convertView.findViewById(R.id.story_byline);
        TextView pubDateTextView = (TextView)convertView.findViewById(R.id.story_pubDate);

        topicTextView.setText(story.getSlug());

        if(story.getImage()!= null){
            Picasso.with(getContext()).load(story.getImage().getSrc()).fit()
                    .centerCrop().into(storyImageView);
        }else{
            storyImageView.setVisibility(View.INVISIBLE);
        }


        titleTextView.setText(story.getTitle());

        if(story.getByline()!= null){
            authorTextView.setText("by " + story.getByline().getName());
        }else {
            authorTextView.setText("by NPR Staff");
        }

        pubDateTextView.setText(new StringToDateUtils().getPubDate(story.getPubDate()));

        return convertView;
    }

}
