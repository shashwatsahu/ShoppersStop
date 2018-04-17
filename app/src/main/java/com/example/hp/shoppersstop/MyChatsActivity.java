package com.example.hp.shoppersstop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyChatsActivity extends AppCompatActivity {


    private DatabaseReference mRootRef;
    private RecyclerView chat_list;
    private Toolbar chats_toolbar;
    private List<DataSnapshot> mChats;
    private MyChatsCustomAdapter adapter;
    private FirebaseAuth mAuth;
    private String currentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chats);

        chats_toolbar = findViewById(R.id.mychats_toolbar);
        setSupportActionBar(chats_toolbar);
        getSupportActionBar().setTitle("Chats");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Log.d(NearbyShopActivity.APP_NAME,currentUserId);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        chat_list = findViewById(R.id.chatList);
        chat_list.setLayoutManager(new LinearLayoutManager(this));
          mChats = new ArrayList<>();
        adapter = new MyChatsCustomAdapter(this,mChats);
        chat_list.setAdapter(adapter);

        loadChats();


    }

    public void loadChats(){

        DatabaseReference mChatRef = mRootRef.child("Chat").child(currentUserId);
        mChatRef.keepSynced(true);
        mRootRef.child("Chat").child(currentUserId).addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               mChats.add(dataSnapshot);
               adapter.notifyDataSetChanged();
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

           }
       });

    }
}
