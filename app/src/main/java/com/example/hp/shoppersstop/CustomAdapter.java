package com.example.hp.shoppersstop;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by pc on 2/24/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<UserViewHolder> {


    Context mContext;
    List<ShopModel> mList;
    RequestQueue mRequestQueue;

    public static final String SHOP_INFO= "shop_info";
    public static final String SHOP_OPEN = "shop_open";
    public static final String SHOP_CLOSE = "shop_close";



    public static final int ITEM_VIEW_SIMPLE = 0;
    public static final int ITEM_VIEW_FOOTER =1;

    public CustomAdapter(Context mContext)
    {
        this.mContext = mContext;
        this.mList = new ArrayList<>();
       mRequestQueue = MySingleton.getInstance(mContext).getRequestQueue();


    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(com.example.hp.shoppersstop.R.layout.item_view,parent,false);

        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {

        holder.shop_name.setText(mList.get(position).getShopName());
        Resources res = mContext.getResources();
        holder.shop_distance.setText(res.getString(com.example.hp.shoppersstop.R.string.shop_distance,mList.get(position).getDistance()));
        holder.shop_ratings.setRating(mList.get(position).getRatings());

        Glide.with(mContext).load(mList.get(position).getDisplayPhotoUrl()).into(holder.shop_display_photo);
        holder.view_shop_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                //start shop profile
                Intent intent = new Intent(mContext, Shop_full_profile.class);
                Bundle bundle = new Bundle();
                bundle.putString(NearbyShopActivity.SHOP_NAME,mList.get(position).getShopName());
                bundle.putString(NearbyShopActivity.SHOP_CONTACT,mList.get(position).getShopContactNumber());
                bundle.putString(NearbyShopActivity.SHOP_ADDRESS,mList.get(position).getAddress());
                bundle.putString(NearbyShopActivity.SHOP_DISPLAY_URL,mList.get(position).getDisplayPhotoUrl());
                bundle.putString(SHOP_OPEN,mList.get(position).getOpenTime());
                bundle.putString(SHOP_CLOSE,mList.get(position).getCloseTime());
                intent.putExtra(SHOP_INFO,bundle);

                mContext.startActivity(intent);


            }
        });

        holder.call_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String uri = "tel:"+ mList.get(position).getShopContactNumber();
                intent.setData(Uri.parse(uri));
                mContext.startActivity(intent);
            }
        });

        holder.chat_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start chat activity
            }
        });




    }

    @Override
    public int getItemViewType(int position) {

       return mList.get(position)!=null?ITEM_VIEW_SIMPLE:ITEM_VIEW_FOOTER;

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void addItem(ShopModel u)
    {
        mList.add(u);
       notifyItemInserted(mList.indexOf(u));
    }

    public void removeItem(ShopModel u)
    {
        mList.remove(u);
        notifyItemRemoved(mList.indexOf(u));
    }



}
