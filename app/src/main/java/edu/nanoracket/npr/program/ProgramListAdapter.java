package edu.nanoracket.npr.program;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nanoracket.npr.R;


public class ProgramListAdapter extends ArrayAdapter<Program> {

    private int[] imagePostions;

    public ProgramListAdapter(Context context, ArrayList<Program> stories, int[] imagePostions){
        super(context, R.layout.program_list_item, stories);
        this.imagePostions = imagePostions;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Program program = getItem(position);

        ViewHolder holder = new ViewHolder();

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.program_list_item,parent,false);
        }

        //ImageView imageView =(ImageView) convertView.findViewById(R.id.programImageView);
        //imageView.setImageResource(R.drawable.all_things_considered);
        holder.programImageView = (ImageView)convertView.findViewById(R.id.programImageView);
        holder.programImageView.setImageResource(imagePostions[position]);
        holder.programTextView = (TextView)convertView.findViewById(R.id.programTextView);
        holder.programTextView.setText(program.getName());
        convertView.setTag(holder);

        return convertView;
    }

    public static class ViewHolder{
        ImageView programImageView;
        TextView programTextView;
    }


}
