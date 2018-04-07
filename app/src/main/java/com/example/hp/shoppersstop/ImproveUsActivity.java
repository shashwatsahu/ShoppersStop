package com.example.hp.shoppersstop;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ImproveUsActivity extends AppCompatActivity {


    EditText suggestion_msg;
    Button send_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve_us);


        suggestion_msg = findViewById(R.id.suggestion_text);
        send_btn = findViewById(R.id.send_suggestion);




        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String msg = suggestion_msg.getText().toString();

                if(msg!=null&&msg.length()>8)
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL,"motwanirahul1996@gmail.com");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Suggestion for you");
                    intent.putExtra(Intent.EXTRA_TEXT,msg);
                    try{
                        startActivity(Intent.createChooser(intent,"Send Email..."));
                        Toast.makeText(ImproveUsActivity.this, "Suggestion sent successfully", Toast.LENGTH_SHORT).show();
                    }catch (ActivityNotFoundException e)
                    {
                        Toast.makeText(ImproveUsActivity.this, "No clients installed", Toast.LENGTH_SHORT).show();
                    }




                }else{
                    Toast.makeText(ImproveUsActivity.this, "Invalid suggestion", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
