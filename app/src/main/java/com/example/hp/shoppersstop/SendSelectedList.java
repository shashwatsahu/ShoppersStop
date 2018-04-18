package com.example.hp.shoppersstop;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.shoppersstop.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SendSelectedList extends AppCompatActivity implements View.OnClickListener
        , RecyclerView.OnItemTouchListener{

    private static final String TAG = "SendSelectedList";
    private GestureDetectorCompat gestureDetectorCompat;
    private SparseBooleanArray selectedItems;
    private RecyclerView mRecyclerView;
    private int key = -1;
    private ArrayList<ListItem> itemSelectedArray;
    private DatabaseReference databaseReference;

    private String nodeKey;

    FirebaseRecyclerAdapter<ShopListItem, SendSelectedList.ShopListViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_selected_list);

        Intent intent = getIntent();
        try {
            itemSelectedArray =  intent.getParcelableArrayListExtra(Constants.MULTISELECT);

        } catch (NullPointerException e) {
            Log.i(TAG, e.getMessage());
        }

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.list_selection_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(R.string.sendlist);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        selectedItems = new SparseBooleanArray();

        mRecyclerView = findViewById(R.id.vendor_rv);
        RecyclerView.LayoutManager layoutManager;

        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        Query query = FirebaseDatabase.getInstance().getReference().child("shopstore").child("seller")
                .child("grocery").child("userInfo");

        Log.i(TAG, "Ref:" + FirebaseDatabase.getInstance().getReference());
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShopListItem,
                SendSelectedList.ShopListViewHolder>(ShopListItem.class, R.layout.shop_card_view
                , SendSelectedList.ShopListViewHolder.class, query) {
            @Override
            protected void populateViewHolder(SendSelectedList.ShopListViewHolder viewHolder,
                                              ShopListItem model, int position) {

                if(model != null) {

                    viewHolder.name.setText(model.getShopName());
                    viewHolder.address.setText(model.getAddress());
                    viewHolder.rating.setText(String.valueOf(model.getRating()));

                    viewHolder.cardView1.setCardBackgroundColor(selectedItems.get(position)? 0x9934B5E4 : Color.TRANSPARENT);

                   if(model.getStatus()) {

                        viewHolder.status.setImageDrawable(getDrawable(R.drawable.ic_green_mark));
                    }
                    else
                        viewHolder.status.setVisibility(View.INVISIBLE);

                }
                else
                    Toast.makeText(SendSelectedList.this, "List:" + model,
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public ShopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shop_card_view, parent, false);

                Log.i("ListRecycler", "View");

                return new ShopListViewHolder(view);
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        final RecyclerView.ItemDecoration itemDecoration = new android.support.v7.widget.DividerItemDecoration(this,
                android.support.v7.widget.DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnItemTouchListener(this);
        gestureDetectorCompat = new GestureDetectorCompat(this, new RecyclerViewDemoOnGestureListener());

        Toast.makeText(this, "Grocery", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public void onClick(View v) {
        if(v != null) {
                if(selectedItems.size() > 0) {
                    selectedItems.delete(key);
                    Log.i(TAG, "if1" + selectedItems.size());
                    firebaseRecyclerAdapter.notifyItemChanged(key);
                }
                int idx = mRecyclerView.getChildAdapterPosition(v);
                myToggleSelection(idx);
        }
    }

    private void myToggleSelection(int idx){

        toggleSelection(idx);
    }

    public void toggleSelection(int pos){
        Log.i(TAG, "toggleSelection:" + pos);
        key = pos;
        if(selectedItems.get(pos, false)){
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
            databaseReference = firebaseRecyclerAdapter.getRef(pos);
            nodeKey = databaseReference.getKey();
            Log.i(TAG, "key: " + nodeKey);
        }
        firebaseRecyclerAdapter.notifyItemChanged(pos);
    }

    public void sendList(View view) {

        if(nodeKey != null){
            databaseReference = FirebaseDatabase.getInstance().getReference().child("shopstore")
                    .child("seller").child("grocery").child(nodeKey).child("/pendingList");
            try {
                Map<String, Object> sendList = new HashMap<>();

                for(int i = 0; i < itemSelectedArray.size(); i++) {
                    ListItem listItem = itemSelectedArray.get(i);

                    Map<String, Object> listMap = listItem.toMap();

                    sendList.put(listItem.getName(), listMap);

                    databaseReference.updateChildren(sendList, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError == null){
                                Toast.makeText(SendSelectedList.this, "Successfully Send!", Toast.LENGTH_SHORT).show();
                                Log.i(TAG, "Success ref:" + databaseReference);
                            }
                            else
                                Toast.makeText(SendSelectedList.this, "Failed!", Toast.LENGTH_SHORT).show();


                        }
                    });

                    Log.i(TAG, "Name:" + listItem.getName() + " size:" + itemSelectedArray.size());
                }
            } catch (NullPointerException e) {
                Log.i(TAG, e.getMessage());
            }



        }

    }

    private class ShopListViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, rating;
        ImageView status;
        CardView cardView1;
        private ShopListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shop_name_txt_id);
            address = itemView.findViewById(R.id.shop_address_txt_id);
            rating = itemView.findViewById(R.id.rating_id);
            status = itemView.findViewById(R.id.shop_status_id);
            cardView1 = itemView.findViewById(R.id.card_view_shop_layout);
        }
    }

    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e){
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            Log.i(TAG,"On single tap :"+ e.getX()+" 2:"+ e.getY());
            if(view != null)
                onClick(view);
            return super.onSingleTapConfirmed(e);
        }

    }

}
