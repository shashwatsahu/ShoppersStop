package com.example.hp.shoppersstop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NearbyShopActivity extends AppCompatActivity implements GeoQueryEventListener{

    private  ShimmerFrameLayout mShimmerFrameLayout;
    private Toolbar mToolbar;
    private RecyclerView mListOfShops;
    private FusedLocationProviderClient mLocationProvider;
    private Location myLocation;
    private GeoFire mGeoFire;
    private FirebaseDatabase mDatabase;
    private static final String SHOP_LOCATION = "shop_locations";
    private Map<String,ShopModel> mShopsMap;
    private Map<String,GeoLocation> keyLocationMap;
    private CustomAdapter adapter;
    NumberFormat mFormat;
    RequestQueue mRequestQueue;


    public static final String BASE_URL = "url";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHOP_DISPLAY_URL = "shop_photo_url";
    public static final String SHOP_CONTACT = "shop_contact";
    public static final String SHOP_ADDRESS = "shop_address";
    public static final String SHOP_TIME = "shop_time";
    public static final String SHOP_RATINGS = "shop_ratings";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_shop);

        mToolbar = findViewById(R.id.toolbar2);
        mListOfShops = findViewById(R.id.list_of_shops);
        mShimmerFrameLayout = findViewById(R.id.shimmerViewContainer);


        mDatabase = FirebaseDatabase.getInstance();
        mGeoFire = new GeoFire(mDatabase.getReference().child(SHOP_LOCATION));




        mShimmerFrameLayout.startShimmerAnimation();
        mLocationProvider = new FusedLocationProviderClient(this);
        setSupportActionBar(mToolbar);
        mListOfShops.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this);
        mListOfShops.setAdapter(adapter);
        mFormat = new DecimalFormat("0.0#");

        mShopsMap = new HashMap<>();
        keyLocationMap = new HashMap<>();

        mRequestQueue = MySingleton.getInstance(this).getRequestQueue();








    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.search_option: //start search activity
                        break;
            case R.id.map_view_option : //start map activity
                        break;



        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            setCurrentLocation();
            getNearbyShopLocation();

        }else{
            Intent intent = new Intent(this,Location_Permission_Required.class);
            startActivityForResult(intent,101);

        }

    }

    public void setCurrentLocation(){


        try{
            Task<Location> locationTask = mLocationProvider.getLastLocation();

            locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()){
                        myLocation = task.getResult();
                    }

                }
            });

        }catch(SecurityException e){
            Log.d("TAG","Security exception occured");
        }



    }


    public void getNearbyShopLocation(){

        GeoQuery query = mGeoFire.queryAtLocation(new GeoLocation(myLocation.getLatitude(),myLocation.getLongitude()),0.5);
        query.addGeoQueryEventListener(this);

    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {



        keyLocationMap.put(key,location );
    }

    @Override
    public void onKeyExited(String key) {



        ShopModel sm = mShopsMap.get(key);
        keyLocationMap.remove(key);
        mShopsMap.remove(key);
        adapter.removeItem(sm);



    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

    }

    @Override
    public void onGeoQueryReady() {

        //add all the shopitems in the recycler view stop shimmer animation update the ui
        mShimmerFrameLayout.stopShimmerAnimation();
        mShimmerFrameLayout.setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {


                for(final String key : keyLocationMap.keySet())
                {
                    Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            try{

                                String shop_name = response.getString(SHOP_NAME);
                                String shop_profile_photo = response.getString(SHOP_DISPLAY_URL);
                                String shop_contact = response.getString(SHOP_CONTACT);
                                String shop_address = response.getString(SHOP_ADDRESS);
                                String shop_time = response.getString(SHOP_TIME);
                                int ratings = response.getInt(SHOP_RATINGS);
                                GeoLocation go = keyLocationMap.get(key);
                                float[] results = new float[1];
                                Location.distanceBetween(myLocation.getLatitude(),myLocation.getLongitude(),go.latitude,go.longitude,results);
                                String distance = mFormat.format(results[0]*0.000621371);


                                String[] timings = shop_time.split("\\s");

                                ShopModel sm = new ShopModel(shop_name,shop_profile_photo,shop_contact,
                                        distance,timings[0],timings[1],shop_address,ratings);

                                mShopsMap.put(key,sm);
                                adapter.addItem(sm);


                            }catch (JSONException e)
                            {
                                Log.d("TAG","Exception occured "+e.getLocalizedMessage() );
                            }




                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    };

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,BASE_URL+key+".json",
                            null,listener,errorListener);

                    mRequestQueue.add(jsonObjectRequest);


                }

                keyLocationMap.clear();

            }
        }).start();




    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Log.d("TAG","Some error occured");
    }
}
