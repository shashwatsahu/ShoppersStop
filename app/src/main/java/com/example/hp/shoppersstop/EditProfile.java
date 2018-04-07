package com.example.hp.shoppersstop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditProfile extends AppCompatActivity {

    EditText first_name;
    EditText last_name;
    Button save_button;
    TextView error_text_1;
    TextView error_text_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        save_button = findViewById(R.id.save_btn);
        error_text_1 = findViewById(R.id.first_name_error);
        error_text_2 = findViewById(R.id.last_name_error);

        Intent intent = getIntent();
        String[] customerName = intent.getStringExtra(Customer_Profile_Main_Page.CUSTOMER_NAME).split("\\s");
        first_name.setText(customerName[0]);
        last_name.setText(customerName[1]);

        first_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(error_text_1.isShown())
                {
                    error_text_1.setVisibility(View.GONE);
                }
            }
        });

        last_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(error_text_2.isShown())
                {
                    error_text_2.setVisibility(View.GONE);
                }


            }
        });



        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fName = first_name.getText().toString();
                String lName = last_name.getText().toString();

                if(fName.matches("[a-zA-Z]{2,15}"))
                {
                    if(lName.matches("[a-zA-Z]{2,15}"))
                    {
                       fName = fName.substring(0,1).toUpperCase()+fName.substring(1).toLowerCase();
                       lName = lName.substring(0,1).toUpperCase()+lName.substring(1).toLowerCase();

                       Intent resultIntent = new Intent();
                       resultIntent.putExtra(Customer_Profile_Main_Page.CUSTOMER_NAME,fName+" "+lName);
                       setResult(RESULT_OK,resultIntent);
                       finish();
                    }else{
                        error_text_2.setVisibility(View.VISIBLE);
                    }
                }else{
                    error_text_1.setVisibility(View.VISIBLE);
                }



            }
        });


        }
}
