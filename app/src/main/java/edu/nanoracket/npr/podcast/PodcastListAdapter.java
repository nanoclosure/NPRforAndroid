package edu.nanoracket.npr.podcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nanoracket.npr.R;
import edu.nanoracket.npr.util.StringUtils;

public class PodcastListAdapter extends ArrayAdapter<Podcast> {

    private int imagePosition;

    public PodcastListAdapter(Context context, ArrayList<Podcast> podcasts, int imagePosition){
        super(context, R.layout.program_list_item, podcasts);
        this.imagePosition = imagePosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Podcast podcast = getItem(position);
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.program_list_item,parent,false);
        }
        holder.programImageView = (ImageView)convertView.findViewById(R.id.programImageView);
        holder.programImageView.setImageResource(imagePosition);
        holder.programTextView = (TextView)convertView.findViewById(R.id.programTextView);
        holder.programTextView.setText(StringUtils.convertString(podcast.getTitle()));
        convertView.setTag(holder);
        return convertView;
    }

    public static class ViewHolder{
        ImageView programImageView;
        TextView programTextView;
    }
}
