package com.example.hp.shoppersstop;

import android.content.Context;
import android.net.sip.SipSession;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatCustomAdapter extends RecyclerView.Adapter<ChatCustomAdapter.MessageViewHolder>{


    private List<Message> mMessageList;
    private Context ctx;
    private String mCurretUserId;

    public ChatCustomAdapter(Context ctx, List<Message> messageList) {
        mMessageList = messageList;
        this.ctx = ctx;
        mCurretUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View v;
        if(viewType==1)
        {
            v= layoutInflater.inflate(R.layout.chat_item_single_layout_me,parent,false);

        }else{
            v = layoutInflater.inflate(R.layout.chat_item_single_layout_other,parent,false);
        }


        return new MessageViewHolder(v,viewType);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        Message m = mMessageList.get(position);
        holder.messageTextView.setText(m.getMessage());
        String time = DateUtils.formatDateTime(ctx,m.getTime(),DateUtils.FORMAT_SHOW_TIME);
        holder.timeTextView.setText(time);




    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    @Override
    public int getItemViewType(int position) {

        if(mMessageList.get(position).getFrom().equals(mCurretUserId))
        {
            return 1;
        }else{
            return 0;
        }

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder
        {
            TextView messageTextView;
            TextView timeTextView;
            View view;

            public MessageViewHolder(View itemView,int viewType) {
                super(itemView);
                view = itemView;
                if(viewType==1)
                {
                    messageTextView = view.findViewById(R.id.message_box_textview_me);
                    timeTextView = view.findViewById(R.id.msg_time_me);
                }else{
                    messageTextView = view.findViewById(R.id.message_box_textview);
                    timeTextView = view.findViewById(R.id.message_time);
                }


            }
        }

}
