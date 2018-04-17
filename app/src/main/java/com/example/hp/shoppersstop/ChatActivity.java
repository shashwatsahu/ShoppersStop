package com.example.hp.shoppersstop;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.hp.shoppersstop.NearbyShopActivity.APP_NAME;
import static com.example.hp.shoppersstop.NearbyShopActivity.SHOP_DISPLAY_URL;
import static com.example.hp.shoppersstop.NearbyShopActivity.SHOP_NAME;

public class ChatActivity extends AppCompatActivity {


    Toolbar chat_toolbar;
    ImageButton addBtn;
    ImageButton sendBtn;
    TextView mDisplayName;
    TextView mLastSeen;
    CircleImageView mDisplayPhoto;
    private EditText mMessageTextview;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRootRef;
    private RecyclerView mMessageRecyclerView;
    private List<Message> mMessageList;
    private ChatCustomAdapter mAdapter;
    String chat_user_name;
    String mCurrentUserId;
    String chat_user_id;
    String profilePhotoUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        chat_user_id = getIntent().getStringExtra(CustomAdapter.SHOP_ID);
        chat_user_name = getIntent().getStringExtra(SHOP_NAME);
        profilePhotoUrl = getIntent().getStringExtra(SHOP_DISPLAY_URL);
        chat_toolbar = findViewById(R.id.chat_toolBar);
        setSupportActionBar(chat_toolbar);
        ActionBar actionBar = getSupportActionBar();
        addBtn = findViewById(R.id.addBtn);
        sendBtn = findViewById(R.id.sendBtn);
        mMessageTextview = findViewById(R.id.message_edit_text);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.chat_app_bar_layout,null);

       actionBar.setCustomView(view);
       mDisplayName = view.findViewById(R.id.chat_user_display_name);
       mDisplayName.setText(chat_user_name);
       mLastSeen = view.findViewById(R.id.last_seen);
       mDisplayPhoto = view.findViewById(R.id.chat_user_profile_photo);

       mFirebaseDatabase = FirebaseDatabase.getInstance();
       mRootRef = mFirebaseDatabase.getReference();
       Glide.with(this).load(profilePhotoUrl).into(mDisplayPhoto);
       mAuth = FirebaseAuth.getInstance();
       mCurrentUserId = mAuth.getCurrentUser().getUid();
       mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
       mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
       mMessageList = new ArrayList<>();
       mAdapter = new ChatCustomAdapter(this,mMessageList);
       mMessageRecyclerView.setAdapter(mAdapter);
       if(mCurrentUserId!=null)
       {
           sendBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   sendMessage();
               }
           });
            loadMessages();
       }else{
           //send to login activity
           finish();
       }

       mRootRef.child("Shops_database").child("Shops_info").child(chat_user_id).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

                ShopModel sm = dataSnapshot.getValue(ShopModel.class);
                Long online = sm.getOnline();
                String image = dataSnapshot.child("displayPhotoUrl").getValue().toString();

                if(online==233)
                {
                    mLastSeen.setText("online");
                }else{

                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String lastTime = GetTimeAgo.getTimeAgo(online,ChatActivity.this);
                    mLastSeen.setText(getResources().getString(R.string.last_seen_at,lastTime));



                }


           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });






       }

    private void sendMessage() {

        String msg = mMessageTextview.getText().toString();
        if(!TextUtils.isEmpty(msg))
        {
            String current_user_ref = "messages/"+mCurrentUserId+"/"+chat_user_id;
            String chat_user_ref = "messages/"+chat_user_id+"/"+mCurrentUserId;

            DatabaseReference user_msg_push = mRootRef.child(current_user_ref).push();

            String push_id = user_msg_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",msg);
            messageMap.put("seen",false);
            messageMap.put("type","text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from",mCurrentUserId);


            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref+"/"+push_id,messageMap);
            messageUserMap.put(chat_user_ref+"/"+push_id,messageMap);

            mMessageTextview.setText("");
           Map chatMap = new HashMap();
           chatMap.put("seen",false);
           chatMap.put("timeStamp",ServerValue.TIMESTAMP);
           chatMap.put("lastMessage",msg);

           Map chatUserMap = new HashMap();
           chatUserMap.put( "Chat"+"/"+mCurrentUserId+"/"+chat_user_id,chatMap);
           chatUserMap.put("Chat"+"/"+chat_user_id+"/"+mCurrentUserId,chatMap);


           mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!=null)
                    {
                        Log.d(NearbyShopActivity.APP_NAME,databaseError.getMessage());
                    }


                }
            });

           mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
               @Override
               public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                   if(databaseError!=null)
                   {
                       Log.d(APP_NAME,databaseError.getMessage());
                   }
               }
           });

           mRootRef.child("Chat").child(mCurrentUserId).child(chat_user_id).child("seen").setValue(true);

        }

    }

    public void loadMessages(){

       DatabaseReference messageRef =  mRootRef.child("messages").child(mCurrentUserId).child(chat_user_id);

       messageRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {


               Message message = dataSnapshot.getValue(Message.class);
               mMessageList.add(message);
               mAdapter.notifyDataSetChanged();
               mMessageRecyclerView.scrollToPosition(mMessageList.size()-1);


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
