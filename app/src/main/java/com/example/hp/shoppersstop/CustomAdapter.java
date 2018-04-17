package com.example.hp.shoppersstop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hp.shoppersstop.NearbyShopActivity.SHOP_NAME;


/**
 * Created by pc on 2/24/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<UserViewHolder> {


    public  static final String SHOP_ID = "Shop_id" ;
    Activity activity;
    List<ShopModel> mList;
    RequestQueue mRequestQueue;
    private Map<String,String> nameKeyMap;
    private Location sourceLocation;
    public static final String SHOP_INFO= "shop_info";
    public static final String SHOP_OPEN = "shop_open";
    public static final String SHOP_CLOSE = "shop_close";

    private FirebaseDatabase mDatabase;

    public static final int ITEM_VIEW_SIMPLE = 0;
    public static final int ITEM_VIEW_FOOTER =1;

    public CustomAdapter(Activity mContext)
    {
        this.activity = mContext;
        this.mList = new ArrayList<>();
       mRequestQueue = MySingleton.getInstance(mContext).getRequestQueue();
       this.nameKeyMap = new HashMap<>();
       mDatabase = FirebaseDatabase.getInstance();


    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(com.example.hp.shoppersstop.R.layout.item_view,parent,false);

        return new UserViewHolder(view);

    }



    

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, final int position) {

        final String shop_name = mList.get(position).getShopName();
        final String key = nameKeyMap.get(shop_name);
        holder.shop_name.setText(shop_name);
        Resources res = activity.getResources();
        //holder.shop_distance.setText(res.getString(R.string.shop_distance,nameDistaceMap.get(shop_name)));
        holder.shop_ratings.setRating(mList.get(position).getRatings());
        setDistance(holder,shop_name);
        Glide.with(activity).load(mList.get(position).getDisplayPhotoUrl()).into(holder.shop_display_photo);
        holder.view_shop_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                //start shop profile
                Intent intent = new Intent(activity, Shop_full_profile.class);
                Bundle bundle = new Bundle();
                bundle.putString(SHOP_NAME,mList.get(position).getShopName());
                bundle.putString(NearbyShopActivity.SHOP_CONTACT,mList.get(position).getShopContactNumber());
                bundle.putString(NearbyShopActivity.SHOP_ADDRESS,mList.get(position).getAddress());
                bundle.putString(NearbyShopActivity.SHOP_DISPLAY_URL,mList.get(position).getDisplayPhotoUrl());
                bundle.putString(SHOP_OPEN,mList.get(position).getOpenTime());
                bundle.putString(SHOP_CLOSE,mList.get(position).getCloseTime());
                intent.putExtra(SHOP_INFO,bundle);

                activity.startActivity(intent);



            }
        });




        holder.call_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String uri = "tel:"+ mList.get(position).getShopContactNumber();
                intent.setData(Uri.parse(uri));
                activity.startActivity(intent);
            }
        });

        holder.chat_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start chat activity
                Intent intent = new Intent(activity,ChatActivity.class);
                String id = mList.get(position).getShopName();
                Log.d(NearbyShopActivity.APP_NAME,id);
                String shopName = mList.get(position).getShopName();
                intent.putExtra(SHOP_ID,key);
                intent.putExtra(SHOP_NAME,shopName);
                activity.startActivity(intent);
            }
        });




    }

    public void setDistance(final UserViewHolder  holder, String shop_name)
    {
        String key =nameKeyMap.get(shop_name);
        mDatabase.getReference().child("Shops_database").child("Shops_locations").child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double lat = (double)dataSnapshot.child("l").child("0").getValue();
                        double lon = (double)dataSnapshot.child("l").child("1").getValue();
                        Location l = new Location("shop");
                        l.setLatitude(lat);
                        l.setLongitude(lon);
                        double d=  sourceLocation.distanceTo(l)/1000;
                        DecimalFormat formatter = new DecimalFormat("0.##");
                        String distance = formatter.format(d);
                        holder.shop_distance.setText(activity.getResources().getString(R.string.shop_distance,distance));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
        if(mList.contains(u))
        {

        }else{
            mList.add(u);
            notifyItemInserted(mList.indexOf(u));

        }
    }

    public void removeItem(ShopModel u)
    {

            mList.remove(u);
            notifyItemRemoved(mList.indexOf(u));


    }

    public void removeAll()
    {
        mList.clear();
        notifyDataSetChanged();
    }


    public Map<String, String> getNameKeyMap() {
        return nameKeyMap;
    }

    public void setNameKeyMap(Map<String, String> nameKeyMap) {
        this.nameKeyMap = nameKeyMap;
    }

    public Location getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(Location sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
}
