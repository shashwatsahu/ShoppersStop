package com.example.hp.shoppersstop;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 10-02-2018.
 */

public class DrawerAdapter extends ArrayAdapter<DrawerList> {
    private static final String TAG = "DrawerAdapter";

    public DrawerAdapter(@NonNull Context context, ArrayList<DrawerList> arrayList) {
        super(context, 0, arrayList);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, parent, false);
        }
        TextView textView = listItemView.findViewById(R.id.text);
        DrawerList drawerList = getItem(position);
        Drawable drawables = drawerList.getDrawable();

        textView.setText(drawerList.getText());
        ImageView imageView = listItemView.findViewById(R.id.image_id);
        imageView.setImageDrawable(drawables);
        return listItemView;
    }
}
