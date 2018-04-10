package com.example.hp.shoppersstop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Paths.get;

/**
 * Created by hp on 11-02-2018.
 */

public class EnterList extends AppCompatActivity {

    private static final String TAG = "EnterListClass";
    DatabaseReference myRef;
    public static String uid;
    public String listKey;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_edit);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        toolbar.setTitle(R.string.enter_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        MainActivity mainActivity = new MainActivity();

        uid = intent.getStringExtra(MainActivity.UID);

        Log.w(TAG, "oncreate:" + uid);

        // Read from the database

        ImageButton checkList = findViewById(R.id.check_list_btn);
        checkList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EnterList.this, ListRecyclerView.class);
                startActivity(intent1);
            }
        });

    }

    public void getInputList(View view) {
        final EditText name = findViewById(R.id.nam_edt);
        final EditText brand = findViewById(R.id.brnd_edt);
        final EditText price = findViewById(R.id.prc_edt);
        final EditText quant = findViewById(R.id.quant_edt);
        final EditText weight = findViewById(R.id.wt_edt);

        String name1 = name.getText().toString().trim();
        String brand1 = brand.getText().toString().trim();
        String price1 = price.getText().toString().trim();
        String quant1 = quant.getText().toString().trim();
        String weight1 = weight.getText().toString().trim();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();


        if ((name1.length() > 0) && (brand1.length() > 0) && (price1.length() > 0)
                && (quant1.length() > 0) && (weight1.length() > 0)) {

            Toast.makeText(EnterList.this, " Not Null! ", Toast.LENGTH_SHORT).show();

            ListItem listItem = new ListItem(name1, brand1, Integer.parseInt(quant1),
                    Double.parseDouble(price1), Double.parseDouble(weight1));

            final Map<String, Object> listValues = listItem.toMap();

            Map<String, Object> sendList = new HashMap<>();

            Log.i(TAG, "path1" + myRef.getRef().toString());
            sendList.put("/" + name1, listValues);
            Log.i(TAG, "path2" + myRef.getRef().toString());

            myRef.child("shopstore").child("customer")
                    .child(uid).child("productList").updateChildren(sendList);
            Log.i(TAG, "path3" + myRef.getRef().toString());

            myRef.child("shopstore").child("customer").child(uid)
                    .child("productList").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ListItem listItem1 = dataSnapshot.getValue(ListItem.class);
                    if (listItem1 != null) {
                        Log.i(TAG, "path" + myRef.getRef().toString());

                    }
                    name.setText("");
                    weight.setText("");
                    price.setText("");
                    quant.setText("");
                    brand.setText("");

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(EnterList.this, "Cancel...", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "path" + myRef.getRef().toString());

                }
            });
        }
        else
        Toast.makeText(EnterList.this, "Please Insert!", Toast.LENGTH_SHORT).show();
    }

    }

