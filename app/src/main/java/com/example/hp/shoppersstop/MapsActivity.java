package com.example.hp.shoppersstop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryEventListener {

    private GoogleMap mMap;
    private GeoFire mGeoFire;
    private GeoQuery mQuery;
    private Map<String,Marker> keyMarkerMap;
    private double radius;
    private Location currentLocation;
    private Marker currentLocationMarker;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private SettingsClient mSettingsClient;
    private boolean mRequestingLocationUpdates = false;
    public static final int MAPS_ACTIVITY_CODE = 1222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
        keyMarkerMap = new HashMap<>();
        Intent intent = getIntent();
        radius = intent.getDoubleExtra(NearbyShopActivity.RADIUS,0.5);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mGeoFire = new GeoFire(mDatabase.getReference().child("Shops_database").child(NearbyShopActivity.SHOP_LOCATION));
        mSettingsClient = LocationServices.getSettingsClient(this);
        mRequestingLocationUpdates = true;
        buildLocationRequest();
        buildLocationCallback();
        buildLocationSettingRequest();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    public void buildLocationSettingRequest()
    {
        mLocationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest)
                .build();
    }

    public void buildLocationRequest(){

        mLocationRequest = new LocationRequest().
                setSmallestDisplacement(300)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setFastestInterval(10000)
                .setInterval(30000);
    }

    public void buildLocationCallback(){

        mLocationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(currentLocation==null)
                {
                    currentLocation = new Location(locationResult.getLastLocation());
                    LatLng c = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                    currentLocationMarker = mMap.addMarker(new MarkerOptions().position(c));
                    currentLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(c));
                    addNearbyShopsMarker();
                }else{
                    currentLocation.set(locationResult.getLastLocation());
                    LatLng c = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                    currentLocationMarker.setPosition(c);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(c,14f));
                    mQuery.setCenter(new GeoLocation(currentLocation.getLatitude(),currentLocation.getLongitude()));

                }

            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                if(!locationAvailability.isLocationAvailable()){
                    //start location gps setting
                    Intent intent = new Intent(MapsActivity.this,EnableGpsAcitivty.class);
                    intent.putExtra("Code",MAPS_ACTIVITY_CODE);
                    startActivity(intent);
                    finish();
                }
            }
        };



    }


    public boolean checkLocationPermission()
    {

        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
        )== PackageManager.PERMISSION_GRANTED;
    }




    @Override
    protected void onResume() {
        super.onResume();
        if(checkLocationPermission())
        {
            if(mRequestingLocationUpdates)
            {
                startLocationUpdates();
            }


        }else{

            Intent intent = new Intent(this,Location_Permission_Required.class);
            intent.putExtra("Code",MAPS_ACTIVITY_CODE);
            finish();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mRequestingLocationUpdates = true;

    }

    public void startLocationUpdates()
    {

        try{
            Log.d(NearbyShopActivity.APP_NAME,"All location settings satisfied in the map activity");

            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null);


        }catch (SecurityException e)
        {
            Log.d(NearbyShopActivity.APP_NAME,"Some error occured please restart the app");
        }
        }


        public  void addNearbyShopsMarker(){

        if(mQuery==null)
        {
            Log.d(NearbyShopActivity.APP_NAME,"GeoQuery fired");
            mQuery = mGeoFire.queryAtLocation(new GeoLocation(currentLocation.getLatitude(),currentLocation.getLongitude()),radius);
            mQuery.addGeoQueryEventListener(this);
        }
        }


    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        Log.d(NearbyShopActivity.APP_NAME,"Key entered"+key);
        Marker  marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.latitude,location.longitude)));
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


        keyMarkerMap.put(key,marker);

    }

    @Override
    public void onKeyExited(String key) {
        Marker m = keyMarkerMap.remove(key);
        m.remove();

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {


    }

    @Override
    public void onGeoQueryReady() {

        Toast.makeText(this, keyMarkerMap.size()+ " shops found!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if(currentLocation==null)
        {
            startLocationUpdates();
            mRequestingLocationUpdates= false;
        }




    }
}







