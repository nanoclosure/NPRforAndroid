package edu.nanoracket.npr.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import edu.nanoracket.npr.R;

public class ArticleListAdapter extends CursorAdapter{

    public ArticleListAdapter(Context context, Cursor cursor){
        super(context, cursor, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.story_list_item, viewGroup, false);
        holder.topicTextView = (TextView)view.findViewById(R.id.story_topic);
        holder.articleImageView = (ImageView)view.findViewById(R.id.story_imageView);
        holder.titleTextView = (TextView)view.findViewById(R.id.story_title);
        holder.bylineTextVew = (TextView)view.findViewById(R.id.story_byline);
        holder.pubDateTextView = (TextView)view.findViewById(R.id.story_pubDate);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder)view.getTag();

        if(cursor != null){
            String topic = cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.TOPIC));
            String title = cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.TITLE));
            String imagePath = cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.IMAGE_PATH));
            String byline = cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.BYLINE));
            String pubDate = cursor.getString(cursor.getColumnIndex(ArticlesContract.Columns.PUB_DATE));

            holder.topicTextView.setText(topic);
            Picasso.with(context).load(new File(context.getFilesDir() + "/" + imagePath)).fit()
                    .centerCrop().into(holder.articleImageView);
            holder.titleTextView.setText(title);
            holder.bylineTextVew.setText(byline);
            holder.pubDateTextView.setText(pubDate);
        }
    }

    public static class ViewHolder{
        TextView topicTextView;
        ImageView articleImageView;
        TextView titleTextView;
        TextView bylineTextVew;
        TextView pubDateTextView;
    }
}
