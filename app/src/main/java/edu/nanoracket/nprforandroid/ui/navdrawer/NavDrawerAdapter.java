package edu.nanoracket.nprforandroid.ui.navdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.nanoracket.nprforandroid.R;

public class NavDrawerAdapter extends ArrayAdapter<NavMenuItem> {

    private LayoutInflater inflater;

    public NavDrawerAdapter(Context context, int textViewResourceId, NavMenuItem[] objects ) {
        super(context, textViewResourceId, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = inflater.inflate(R.layout.navdrawer_list_item, parent, false);
        }

        NavMenuItem menuItem = this.getItem(position);
        NavMenuItemHolder navMenuItemHolder = null;

        TextView labelView = (TextView) convertView
                .findViewById(R.id.navmenuitem_label );
        ImageView iconView = (ImageView) convertView
                .findViewById(R.id.navmenuitem_icon );

        navMenuItemHolder = new NavMenuItemHolder();
        navMenuItemHolder.labelView = labelView ;
        navMenuItemHolder.iconView = iconView ;
        convertView.setTag(navMenuItemHolder);

        if ( navMenuItemHolder == null ) {
            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
        }

        navMenuItemHolder.labelView.setText(menuItem.getLabel());
        navMenuItemHolder.iconView.setImageResource(menuItem.getIcon());

        return convertView;

    }

    private static class NavMenuItemHolder {
        private TextView labelView;
        private ImageView iconView;
    }

}
