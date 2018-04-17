package com.example.hp.shoppersstop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.NORMAL;
import static com.example.hp.shoppersstop.CustomAdapter.SHOP_ID;
import static com.example.hp.shoppersstop.NearbyShopActivity.APP_NAME;
import static com.example.hp.shoppersstop.NearbyShopActivity.SHOP_DISPLAY_URL;
import static com.example.hp.shoppersstop.NearbyShopActivity.SHOP_NAME;

public class MyChatsCustomAdapter extends RecyclerView.Adapter<MyChatsCustomAdapter.MyChatsViewHolder>{

    private List<DataSnapshot> mChats;
    private Context ctx;
    private FirebaseDatabase mDatabase;
    private DatabaseReference shopInfoRef;
    public MyChatsCustomAdapter(Context ctx, List<DataSnapshot> chats) {
        mChats = chats;
        this.ctx = ctx;
        mDatabase = FirebaseDatabase.getInstance();
        shopInfoRef = mDatabase.getReference().child("Shops_database").child("Shops_info");
    }

    @Override
    public MyChatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(ctx).inflate(R.layout.chat_list_item_layout,parent,false);
        return new MyChatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyChatsViewHolder holder, int position) {

        DataSnapshot ds = mChats.get(position);
       final  String key = ds.getKey();
        Log.d(APP_NAME,key);
        final Chat chat = ds.getValue(Chat.class);
        long value = chat.getTimeStamp();
        String  time = DateUtils.formatDateTime(ctx,value,DateUtils.FORMAT_SHOW_TIME);
        holder.mLastMessageTime.setText(time);


        shopInfoRef.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String shopId = dataSnapshot.getKey();
                ShopModel sm= dataSnapshot.getValue(ShopModel.class);
                holder.mChatUser.setText(sm.getShopName());
                holder.url = sm.getDisplayPhotoUrl();
                holder.chat_user = shopId;
                Glide.with(ctx).load(sm.getDisplayPhotoUrl()).into(holder.mProfilePhoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.setLastMessage(chat);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.getReference().child("Chat").child(key).child(holder.chat_user).child("seen").setValue(true);
                Intent intent = new Intent(ctx,ChatActivity.class);
                intent.putExtra(SHOP_ID,key);
                intent.putExtra(SHOP_NAME,holder.mChatUser.getText().toString());

                intent.putExtra(SHOP_DISPLAY_URL,holder.url);
                ctx.startActivity(intent);

            }
        });

        Glide.with(ctx).load(holder.url).into(holder.mProfilePhoto);

         }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public static class MyChatsViewHolder extends RecyclerView.ViewHolder{

        private View view;
        private CircleImageView mProfilePhoto;
        private TextView mChatUser;
        private  TextView mLastMessage;
        private TextView mLastMessageTime;
        private String url;
        private String chat_user;

        public MyChatsViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            mProfilePhoto = itemView.findViewById(R.id.chats_profile_photo);
            mChatUser = itemView.findViewById(R.id.chats_profile_name);
            mLastMessage = itemView.findViewById(R.id.chats_last_message);
            mLastMessageTime = itemView.findViewById(R.id.chats_last_message_time);

        }


        public void setLastMessage(Chat c)
        {
            mLastMessage.setText(c.getLastMessage());
            if(!c.isSeen())
            {
                mLastMessage.setTypeface(mLastMessage.getTypeface(),BOLD);
            }else{
                mLastMessage.setTypeface(mLastMessage.getTypeface(),NORMAL);
            }

        }
    }

}
