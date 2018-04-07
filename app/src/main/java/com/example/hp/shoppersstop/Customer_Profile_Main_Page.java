package com.example.hp.shoppersstop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Customer_Profile_Main_Page extends AppCompatActivity {

    private CircleImageView mProfilePhoto;
    private static final int IMAGE_PICK = 100;

    private static final int EDIT_PROFILE = 101;


    public static final String CUSTOMER_NAME = "customer name";

    CardView edit_profile_cv;
    CardView my_coupons_cv;
    CardView share_and_win_cv;
    CardView terms_of_service_cv;
    CardView help_and_support_cv;
    CardView about_us_cv;
    CardView improve_us_cv;
    TextView customer_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_main_page);

        mProfilePhoto = findViewById(R.id.profilePhoto);

        customer_name = findViewById(R.id.customer_name);
        edit_profile_cv = findViewById(R.id.edit_profile_card_view);
        my_coupons_cv = findViewById(R.id.my_coupons_card_view);
        share_and_win_cv = findViewById(R.id.share_and_win_card_view);
        help_and_support_cv = findViewById(R.id.help_support_cv);
        improve_us_cv = findViewById(R.id.improve_us_cv);
        about_us_cv = findViewById(R.id.about_us_cv);
        terms_of_service_cv = findViewById(R.id.terms_of_service_cv);


        mProfilePhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,IMAGE_PICK);

            }
        });


        edit_profile_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(Customer_Profile_Main_Page.this,EditProfile.class);
                intent.putExtra(CUSTOMER_NAME,customer_name.getText().toString());

                startActivityForResult(intent,EDIT_PROFILE);




            }
        });

        my_coupons_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //start coupons activity

            }
        });


        about_us_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start about us activity
            }
        });



        help_and_support_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start help and support activity
            }
        });


        improve_us_cv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Customer_Profile_Main_Page.this,ImproveUsActivity.class);
                startActivity(intent);

                //start improve us activity
            }
        });

        share_and_win_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start share and win activity


            }
        });

        terms_of_service_cv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //start terms of service

                Intent intent = new Intent(Customer_Profile_Main_Page.this,TermsAndConditions.class);
                startActivity(intent);
            }
        });







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {

            switch (requestCode)
            {

                case IMAGE_PICK :
                    try{
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        mProfilePhoto.setImageBitmap(yourSelectedImage);

                    }catch (IOException e)
                    {
                        Log.d("TAG","Exception occured");
                    }
                    break;

                case EDIT_PROFILE : String customerName = data.getStringExtra(CUSTOMER_NAME);
                                        customer_name.setText(customerName);
                                        Toast.makeText(Customer_Profile_Main_Page.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                                        break;


            }



        }


    }



}
