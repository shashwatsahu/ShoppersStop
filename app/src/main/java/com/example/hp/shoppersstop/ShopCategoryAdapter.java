package com.example.hp.shoppersstop;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.shoppersstop.R;
import com.example.hp.shoppersstop.UserViewHolder;

import java.util.ArrayList;


/**
 * Created by hp on 25-02-2018.
 */

public class ShopCategoryAdapter extends ArrayAdapter<ShopCategoryItem> {


    public ShopCategoryAdapter(Context context, ArrayList<ShopCategoryItem> items) {

        super(context, 0, items);

    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.shop_category_item, parent, false);
        }

        ShopCategoryItem currentItem = getItem(position);

        ImageView imageView = listItemView.findViewById(R.id.image_view);
        TextView textView = listItemView.findViewById(R.id.shops_cat_txt);
            textView.setText(currentItem.getTextView());
        imageView.setImageDrawable(currentItem.getImageView());
        return listItemView;
        // references to our images

    }
}