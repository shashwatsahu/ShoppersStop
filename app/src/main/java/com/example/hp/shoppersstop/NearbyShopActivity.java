package com.example.hp.shoppersstop;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Filter;
import android.widget.Toast;

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
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

<<<<<<< HEAD
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

=======
public class NearbyShopActivity extends AppCompatActivity implements GeoQueryEventListener{
>>>>>>> 0682d8328fa403c3e4ceb503136c603eb0372aae

public class NearbyShopActivity extends AppCompatActivity implements GeoQueryEventListener {

    private static final String SHOP_LAST_SEEN = "online" ;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private Toolbar mToolbar;
    private RecyclerView mListOfShops;
    private FusedLocationProviderClient mLocationProvider;
    private Location myLocation;
    private GeoFire mGeoFire;
    private FirebaseDatabase mDatabase;
    private double radius = 0.4;
    private FloatingActionButton filterBtn;
    private boolean authenticated = false;
    private boolean deliveryAvailable = false;
    private boolean excludeClosedShop = false;
    public Map<String, String> nameDistanceMap;
    private boolean mRequestingLocationUpdates;
    private GeoQuery query;

    private LocationSettingsRequest mLocationSettingsRequest;
    private SettingsClient mSettingsClient;
    private boolean filterApplied=false;



    private Map<String, ShopModel> mShopsMap;
    private Map<String, GeoLocation> keyLocationMap;
    private CustomAdapter adapter;
    NumberFormat mFormat;
    RequestQueue mRequestQueue;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    public static final String APP_NAME = "shopper_stop";


    public static final String SHOP_LOCATION = "Shops_locations";
    public static final String BASE_URL = "https://myfirstapp-b720d.firebaseio.com/Shops_database/Shops_info/";
    public static final String SHOP_NAME = "shopName";
    public static final String SHOP_DISPLAY_URL = "displayPhotoUrl";
    public static final String SHOP_CONTACT = "shopContactNumber";
    public static final String SHOP_ADDRESS = "address";
    public static final String SHOP_RATINGS = "ratings";
    public static final String SHOP_OPEN_TIME = "openTime";
    public static final String SHOP_CLOSE_TIME = "closeTime";
    public static final String RADIUS = "radius";
    public static final int FILTER_SHOP_CODE = 1109;
    public static final String IS_AUTHENTICATED = "authenticated";
    public static final String IS_DELIVERY_AVAILABLE = "deliveryAvailable";
    public static final int NEARBYSHOP_ACTIVITY_CODE = 1099;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_shop);

        mToolbar = findViewById(R.id.toolbar2);
        mListOfShops = findViewById(R.id.list_of_shops);
        mShimmerFrameLayout = findViewById(R.id.shimmerViewContainer);
        filterBtn = findViewById(R.id.filter_btn);
        mLocationProvider = getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance();
        mGeoFire = new GeoFire(mDatabase.getReference().child("Shops_database").child(SHOP_LOCATION));
        mShimmerFrameLayout.startShimmerAnimation();
        setSupportActionBar(mToolbar);

        mListOfShops.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(this);
        mListOfShops.setAdapter(adapter);
        mFormat = new DecimalFormat("0.0#");
        mRequestingLocationUpdates = true;
        mShopsMap = new HashMap<>();
        keyLocationMap = new HashMap<>();

        mSettingsClient = LocationServices.getSettingsClient(this);

        mRequestQueue = MySingleton.getInstance(this).getRequestQueue();


        Intent intent = getIntent();
        if(intent.getIntExtra("Code",000)==FilterShopActivity.FILTER_ACTIVITY_CODE)
        {
            authenticated = intent.getBooleanExtra(FilterShopActivity.AUTHENTICATED,false);
            deliveryAvailable = intent.getBooleanExtra(FilterShopActivity.DELIVERY,false);
            excludeClosedShop = intent.getBooleanExtra(FilterShopActivity.EXCLUDE_CLOSE,false);
            radius = intent.getDoubleExtra(FilterShopActivity.RANGE,0.4);
            Log.d(APP_NAME,"Radius is "+radius);
        }







        buildLocationRequest();
        buildLocationCallback();
        buildLocationSettingRequest();

    }

    public void buildLocationSettingRequest(){
        mLocationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .build();
    }

    public void buildLocationRequest()
    {

        mLocationRequest = LocationRequest.create()
                .setInterval(50000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setSmallestDisplacement(200);


    }

    public void buildLocationCallback()
    {

        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {

                Location location = locationResult.getLastLocation();
                if (myLocation == null) {
                    myLocation = new Location(location);
                    adapter.setSourceLocation(myLocation);
                    Log.d(APP_NAME," "+radius);
                    getNearbyShopLocation();
                } else {
                    myLocation.set(location);
                    adapter.setSourceLocation(myLocation);
                    query.setCenter(new GeoLocation(myLocation.getLatitude(),myLocation.getLongitude()));


                }


            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                if (!locationAvailability.isLocationAvailable()) {
                    //check location setting
                    if(myLocation!=null)
                    {
                       //call locationSettingUnaiavlavle activity
                        Intent intent = new Intent(NearbyShopActivity.this,EnableGpsAcitivty.class);
                        intent.putExtra("Code",NEARBYSHOP_ACTIVITY_CODE);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.search_option: //start search activity
                break;
            case R.id.map_view_option: //start map activity
             Intent mapIntent = new Intent(this, MapsActivity.class);
                mapIntent.putExtra(RADIUS, radius);
                startActivity(mapIntent);
                break;


        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(checkLocationPermission())
        {
            startLocationUpdates();
        }else{
            //start permission required activity
            Intent intent = new Intent(this,Location_Permission_Required.class);
            intent.putExtra("CODE",NEARBYSHOP_ACTIVITY_CODE);
            finish();

        }




    }


    public boolean checkLocationPermission(){

        return ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;

    }





    public void startLocationUpdates() {


        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                        Log.d(APP_NAME,"All location settings are satisfied start location updates");
                        try {
                            mLocationProvider.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

                        } catch (SecurityException e) {
                            Log.d("Error_msg", "unknown error occured");
                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                            //startLocationSetting activity

                        Intent intent = new Intent(NearbyShopActivity.this,EnableGpsAcitivty.class);
                        intent.putExtra("CODE",NEARBYSHOP_ACTIVITY_CODE);
                        finish();


                    }


                });





    }



    @Override
    protected void onPause() {
        super.onPause();

        if (mRequestingLocationUpdates) {

            Log.d(APP_NAME, "Stopped location updates");
            mLocationProvider.removeLocationUpdates(mLocationCallback);

        }
    }


    public void getNearbyShopLocation() {


        Toast.makeText(NearbyShopActivity.this, "Getting nearby shops", Toast.LENGTH_SHORT).show();
        if(query==null)
        {
            query = mGeoFire.queryAtLocation(new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), radius);
            query.addGeoQueryEventListener(this);
        }



    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {


        Log.d(APP_NAME, key + "  entered"+location.toString());
        keyLocationMap.put(key, location);






    }




    @Override
    public void onKeyExited(String key) {


        ShopModel sm = mShopsMap.get(key);
        if (sm != null) {
            Log.d(APP_NAME, key + " Exited");
            keyLocationMap.remove(key);
            mShopsMap.remove(key);
            adapter.removeItem(sm);
        }


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

                for (final String key : keyLocationMap.keySet()) {

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + key + ".json",
                            null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                final String shop_name = response.getString(SHOP_NAME);
                                String shop_profile_photo = response.getString(SHOP_DISPLAY_URL);
                                String shop_contact = response.getString(SHOP_CONTACT);
                                String shop_address = response.getString(SHOP_ADDRESS);
                                String shop_open_time = response.getString(SHOP_OPEN_TIME);
                                String shop_close_time = response.getString(SHOP_CLOSE_TIME);
                                Long online = response.getLong(SHOP_LAST_SEEN);
                                int ratings = response.getInt(SHOP_RATINGS);
                                boolean isAuthenticated = response.getBoolean(IS_AUTHENTICATED);
                                boolean isDeliveryAvailable = response.getBoolean(IS_DELIVERY_AVAILABLE);











                                boolean isShopOpen = setShopStatus(shop_open_time, shop_close_time);

                                ShopModel sm = new ShopModel(shop_name, shop_profile_photo, shop_contact, shop_open_time, shop_close_time, shop_address, ratings, isAuthenticated, isDeliveryAvailable,online);
                                if ((isAuthenticated || !authenticated) && (isDeliveryAvailable || !deliveryAvailable) && (isShopOpen || !excludeClosedShop)) {

                                    mShopsMap.put(key,sm);
                                    adapter.getNameKeyMap().put(shop_name,key);
                                    adapter.addItem(sm);

                                }
                            } catch (JSONException e) {
                                Log.d(APP_NAME, "Exception occured " + e.getLocalizedMessage());
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    mRequestQueue.add(jsonObjectRequest);
                }

                keyLocationMap.clear();


            }
        }).start();





    }

    public boolean setShopStatus(String open, String close) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String current = dateFormat.format(Calendar.getInstance().getTime());
        Log.d(APP_NAME,current);
        try {
            Date now = dateFormat.parse(current);
            Date openTime = dateFormat.parse(open);
            Date closeTime = dateFormat.parse(close);
            if (now.after(openTime) && now.before(closeTime)) {
                return true;
            } else {
                return false;
            }


        } catch (ParseException e) {
            Log.d("TAG", "Exception occured");
        }

        return false;

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        Log.d(APP_NAME, "Some error occured");
    }

    public void filterButtonClick(View view) {


        Intent intent = new Intent(this, FilterShopActivity.class);
        startActivity(intent);
        finish();
    }







}
