package com.example.hp.shoppersstop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SendSelectedList extends AppCompatActivity {

    FirebaseRecyclerAdapter<ShopListItem, SendSelectedList.ShopListViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_selected_list);

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

        RecyclerView mRecyclerView = findViewById(R.id.vendor_rv);
        RecyclerView.LayoutManager layoutManager;

        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        Query query = FirebaseDatabase.getInstance().getReference().child("shopstore").child("seller")
                .child("grocery").child("userID");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShopListItem, SendSelectedList.ShopListViewHolder>(ShopListItem.class, R.layout.shop_card_view
                , SendSelectedList.ShopListViewHolder.class, query) {
            @Override
            protected void populateViewHolder(SendSelectedList.ShopListViewHolder viewHolder, ShopListItem model, int position) {

                if(model != null) {

                    viewHolder.name.setText(model.getShopName());
                    viewHolder.address.setText(model.getAddress());
                    viewHolder.rating.setText(String.valueOf(model.getRating()));
                    Toast.makeText(SendSelectedList.this, "Name:" + model.getShopName(), Toast.LENGTH_SHORT).show();
                    if(model.getStatus()) {

                        viewHolder.status.setImageDrawable(getDrawable(R.drawable.ic_green_mark));
                    }
                    else
                        viewHolder.status.setVisibility(View.INVISIBLE);

                }
                else
                    Toast.makeText(SendSelectedList.this, "List:" + model , Toast.LENGTH_SHORT).show();
            }

            @Override
            public SendSelectedList.ShopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_card_view, parent, false);

                Log.i("ListRecycler", "View");

                return new SendSelectedList.ShopListViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        Toast.makeText(this, "Grocery", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    private class ShopListViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, rating;
        ImageView status;

        private ShopListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shop_name_txt_id);
            address = itemView.findViewById(R.id.shop_address_txt_id);
            rating = itemView.findViewById(R.id.rating_id);
            status = itemView.findViewById(R.id.shop_status_id);
        }
    }
}
