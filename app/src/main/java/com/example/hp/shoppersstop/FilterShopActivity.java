package com.example.hp.shoppersstop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.hp.shoppersstop.R;
import com.example.hp.shoppersstop.UserViewHolder;

import java.text.DecimalFormat;

public class FilterShopActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {


    private CheckBox authenticateCheckBox;
    private CheckBox deliveryAvailableChkBox;
    private CheckBox closedShopChkBox;
    private SeekBar rangeFilterSeekBar;
    private Button cancelBtn;
    private Button addFilterBtn;
    private double range;
    private int position;


    public static final int FILTER_ACTIVITY_CODE = 1233;
    public static final String AUTHENTICATED = "authenticated";
    public static final String DELIVERY = "delivery available";
    public static final String EXCLUDE_CLOSE = "exclude close shops";
    public static final String RANGE = "range";

    public static final String KEY_AUTH = "is_authentication checked";
    public static final String KEY_CLOSED_SHOP = "is_closed_shop_checked";
    public static final String KEY_DELIVERY_AVAIL = "is_delivery_available";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_shop);


        authenticateCheckBox = findViewById(R.id.authenticateChkBox);
        deliveryAvailableChkBox = findViewById(R.id.deliveryAvailableChkBox);
        closedShopChkBox = findViewById(R.id.closedShopChkBox);
        rangeFilterSeekBar = findViewById(R.id.rangeFilterSeekBar);
        rangeFilterSeekBar.setOnSeekBarChangeListener(this);
        range = rangeFilterSeekBar.getProgress()*(0.4);
        addFilterBtn = findViewById(R.id.addFilterButton);
        addFilterBtn.setOnClickListener(this);
        cancelBtn = findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(this);


        Intent intent = getIntent();
        authenticateCheckBox.setChecked(intent.getBooleanExtra(AUTHENTICATED,false));
        deliveryAvailableChkBox.setChecked(intent.getBooleanExtra(DELIVERY,false));
        closedShopChkBox.setChecked(intent.getBooleanExtra(EXCLUDE_CLOSE,false));
        double radius = intent.getDoubleExtra(RANGE,0.4);
         int i =(int) Math.round(radius);
        rangeFilterSeekBar.setProgress(i);












    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

         position = seekBar.getProgress();
        range = position *(0.4);
        DecimalFormat format = new DecimalFormat("0.#");
        String rangeFormatted = format.format(range);
        range = Double.parseDouble(rangeFormatted);

        Toast.makeText(this, "Selected range is "+range, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

        Intent dataBack = new Intent(FilterShopActivity.this,NearbyShopActivity.class);
        if(view.getId()==cancelBtn.getId())
        {
            dataBack.putExtra("Code",000);

        }else
        {

            dataBack.putExtra("Code",NearbyShopActivity.NEARBYSHOP_ACTIVITY_CODE);
            if(authenticateCheckBox.isChecked())
            {
                dataBack.putExtra(AUTHENTICATED,true);
            }else
            {
                dataBack.putExtra(AUTHENTICATED,false);
            }
            if(deliveryAvailableChkBox.isChecked())
            {
                dataBack.putExtra(DELIVERY,true);
            }else
            {
                dataBack.putExtra(DELIVERY,false);
            }
            if(closedShopChkBox.isChecked())
            {
                dataBack.putExtra(EXCLUDE_CLOSE,true);
            }else
            {
                dataBack.putExtra(EXCLUDE_CLOSE,false);
            }

            dataBack.putExtra(RANGE,range);

        }

        startActivity(dataBack);
        finish();


    }



}
