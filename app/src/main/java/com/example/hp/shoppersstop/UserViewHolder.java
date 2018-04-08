package com.example.hp.shoppersstop;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by pc on 2/24/2018.
 */

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


   ImageView shop_display_photo;
   TextView shop_name;
   TextView shop_distance;
   RatingBar shop_ratings;
   ImageButton call_shop;
   ImageButton chat_shop;
    ImageButton view_shop_profile;



    public UserViewHolder(View itemView)
    {

        super(itemView);
        shop_display_photo = itemView.findViewById(R.id.shopDisplay);
        shop_name = itemView.findViewById(R.id.shopName);
        shop_distance = itemView.findViewById(R.id.shop_distance);
        shop_ratings = itemView.findViewById(R.id.ratings);
        call_shop = itemView.findViewById(R.id.call_shop);
        chat_shop = itemView.findViewById(R.id.chat_shop);
        view_shop_profile = itemView.findViewById(R.id.view_shop_profile);


    }





    @Override
    public void onClick(View view) {



    }
}





