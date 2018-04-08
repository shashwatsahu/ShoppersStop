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

public class ShopsActivity extends AppCompatActivity {

    FirebaseRecyclerAdapter<ShopListItem, ShopListViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        Intent intent = getIntent();
        String shopCategoryName = intent.getStringExtra(Constants.SHOP_KEY);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.shop_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(R.string.shopList);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView mRecyclerView = findViewById(R.id.shop_activity_recycler_view);
        RecyclerView.LayoutManager layoutManager;

        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        Query query = FirebaseDatabase.getInstance().getReference().child("shopstore").child("seller")
                                              .child(shopCategoryName).child("userID");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShopListItem, ShopListViewHolder>(ShopListItem.class, R.layout.shop_card_view
        , ShopListViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ShopListViewHolder viewHolder, ShopListItem model, int position) {

                    if(model != null) {

                    viewHolder.name.setText(model.getShopName());
                    viewHolder.address.setText(model.getAddress());
                    viewHolder.rating.setText(String.valueOf(model.getRating()));
                    Toast.makeText(ShopsActivity.this, "Name:" + model.getName(), Toast.LENGTH_SHORT).show();
                    if(model.getStatus()) {

                        viewHolder.status.setImageDrawable(getDrawable(R.drawable.ic_green_mark));
                    }
                    else
                        viewHolder.status.setVisibility(View.INVISIBLE);

                }
                else
                    Toast.makeText(ShopsActivity.this, "List:" + model , Toast.LENGTH_SHORT).show();
            }

            @Override
            public ShopListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_card_view, parent, false);

                Log.i("ListRecycler", "View");

                return new ShopListViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        Toast.makeText(this, "Shop Category:" + shopCategoryName, Toast.LENGTH_SHORT).show();
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
