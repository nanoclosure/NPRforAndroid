package edu.nanoracket.nprforandroid.program;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.nanoracket.nprforandroid.R;


public class ProgramListAdapter extends ArrayAdapter<Program> {

    public ProgramListAdapter(Context context, ArrayList<Program> stories){
        super(context, R.layout.program_list_item, stories);
    }

    public static final int[] imageIndex = new int[] {
            R.drawable.all_things_considered,
            R.drawable.cartalk,
            R.drawable.tedradiohour,
            R.drawable.planetmoney,
            R.drawable.snapjudgment,
            R.drawable.bullseye,
            R.drawable.onpoint,
            R.drawable.latinousa,
            R.drawable.radiolab,
            R.drawable.popculture,
            R.drawable.onthemedia,
            R.drawable.dianerehm,
            R.drawable.intelligence,
            R.drawable.stateofreunion,
            R.drawable.yourhealth,
            R.drawable.thistleshamrock,
            R.drawable.worldcafe,
            R.drawable.fromthetop,
            R.drawable.onlyagame
    };

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
        holder.programImageView.setImageResource(imageIndex[position]);
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
