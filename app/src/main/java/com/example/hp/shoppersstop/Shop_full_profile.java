package com.example.hp.shoppersstop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.shoppersstop.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Shop_full_profile extends AppCompatActivity {

    Toolbar mToolbar;
    CardView shop_contact_card_view;
    TextView shop_contact;
    ImageView shop_status;
    CardView navigation_card_view;
    TextView shop_address;
    TextView opensAt,closesAt;
    ImageView shop_profile;



    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);

         mToolbar = findViewById(R.id.shop_name_toolbar);
         shop_contact_card_view = findViewById(R.id.shop_contact_card_view);
         shop_contact = findViewById(R.id.contact_number);
         shop_status = findViewById(R.id.shop_status);
         navigation_card_view = findViewById(R.id.navigation_card_view);
         shop_profile = findViewById(R.id.shop_display_full);
            opensAt = findViewById(R.id.openingTime);
            closesAt = findViewById(R.id.closingTime);
            shop_address = findViewById(R.id.shop_address);


         Bundle bundle = getIntent().getBundleExtra(CustomAdapter.SHOP_INFO);
         String shop_name = bundle.getString(NearbyShopActivity.SHOP_NAME);
         String shop_display_url = bundle.getString(NearbyShopActivity.SHOP_DISPLAY_URL);
         final String contact_number = bundle.getString(NearbyShopActivity.SHOP_CONTACT);
         String address = bundle.getString(NearbyShopActivity.SHOP_ADDRESS);
         String shopOpenTime = bundle.getString(CustomAdapter.SHOP_OPEN);
         String shopClose = bundle.getString(CustomAdapter.SHOP_CLOSE);


         setSupportActionBar(mToolbar);
        mToolbar.setTitle(shop_name);
        Glide.with(this).load(shop_display_url).into(shop_profile);
        shop_contact.setText(contact_number);
        shop_address.setText(address);
        opensAt.setText(shopOpenTime);
        closesAt.setText(shopClose);








         shop_contact_card_view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 CallShopKeeperDialog dialog = new CallShopKeeperDialog();
                 Bundle bundle = new Bundle();
                 bundle.putString(CallShopKeeperDialog.PHONE_NUMBER,contact_number);
                 dialog.setArguments(bundle);
                 dialog.show(getSupportFragmentManager(),"CallShopKeeperDialog");
             }
         });


        navigation_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /** Implement google map navigation **/

               String uri = "google.navigation:q=23.265405,77.376146&avoid=tf";
               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
               intent.setPackage("com.google.android.apps.maps");
               startActivity(intent);


            }
        });

        setShopStatus(shopOpenTime,shopClose);


    }


    public void setShopStatus(String open,String close)
    {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        String time = dateFormat.format(Calendar.getInstance().getTime());


        try{

            Date current = dateFormat.parse(time);
            Date openTime =dateFormat.parse(open);
            Date closeTime  = dateFormat.parse(close);

            if(current.after(openTime)&&current.before(closeTime))
            {
                shop_status.setImageResource(R.drawable.ic_shop_open);

            }else{
                shop_status.setImageResource(R.drawable.ic_shop_closed);
            }

        }catch (ParseException e)
        {
            Log.e("TAG",e.getLocalizedMessage());
        }


    }

}

