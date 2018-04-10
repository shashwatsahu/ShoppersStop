package com.example.hp.shoppersstop;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.shoppersstop.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GeoQueryEventListener{

    private GoogleMap mMap;
    private GeoFire mGeoFire;
    private GeoQuery mQuery;
    private Map<String,Marker> keyMarkerMap;
    private double radius;
    private Location currentLocation;
    private Marker currentLocationMarker;
    private FusedLocationProviderClient mFusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
        keyMarkerMap = new HashMap<>();
        Intent intent = getIntent();
        radius = intent.getDoubleExtra(NearbyShopActivity.RADIUS,0.5);
        mGeoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child(NearbyShopActivity.SHOP_LOCATION));
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        Toast.makeText(this, "Updating map", Toast.LENGTH_SHORT).show();

        getMyLocation();
        addNearbyShopsMarker();




    }

    public void getMyLocation(){

        try{

           Task<Location> locationTask =   mFusedLocationProviderClient.getLastLocation();
           locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
               @Override
               public void onComplete(@NonNull Task<Location> task) {

                   if(task.isSuccessful()){

                       currentLocation = task.getResult();
                       double lat= currentLocation.getLatitude();
                       double lon = currentLocation.getLongitude();
                       LatLng current = new LatLng(lat,lon);
                      currentLocationMarker =  mMap.addMarker(new MarkerOptions().position(current));
                        currentLocationMarker.setTitle("You are here");
                        /**add location marker icon **/
                       mMap.animateCamera(CameraUpdateFactory.newLatLng(current));

                   }

               }
           });


        }catch (SecurityException e){
            Log.d("TAG","Some error occured");
        }


    }



    public  void addNearbyShopsMarker(){


        mQuery = mGeoFire.queryAtLocation(new GeoLocation(currentLocation.getLatitude(),currentLocation.getLongitude()),radius);
        mQuery.addGeoQueryEventListener(this);



    }


    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        Marker  marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.latitude,location.longitude)));

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
}


