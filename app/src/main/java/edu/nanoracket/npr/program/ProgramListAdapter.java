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
    private int[] imagePositions;

    public ProgramListAdapter(Context context, ArrayList<Program> stories, int[] imagePositions){
        super(context, R.layout.program_list_item, stories);
        this.imagePositions = imagePositions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Program program = getItem(position);
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.program_list_item,parent,false);
        }
        holder.programImageView = (ImageView)convertView.findViewById(R.id.programImageView);
        holder.programImageView.setImageResource(imagePositions[position]);
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
