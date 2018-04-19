package com.example.hp.shoppersstop.search;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.shoppersstop.Constants;
import com.example.hp.shoppersstop.R;
import com.example.hp.shoppersstop.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hp on 10-02-2018.
 */

public class SearchActivity extends FragmentActivity/*implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener*/ {

    private static final String TAG = "SearchActivity";
    private static final int SEARCH_QUERY_THRESHOLD = 2;
    // private static final String TAG = "SearchActivity";
    private Spinner spinner, spinnerBrand, spinnerDepartment, spinnerPrice, spinnerWeight;

    private static final int PRODUCT = 0;
    private static final int SHOP = 1;

    private AutoCompleteTextView autoCompleteTextView;

    private DatabaseReference databaseReference;
    private RecyclerView searchRecyclerView;

    private Toolbar toolbarProduct;

    ArrayList<String> arrayListBrand;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchRecyclerView = findViewById(R.id.search_rv);
          autoCompleteTextView = findViewById(R.id.search_auto_edt);
        ImageButton searchButton = findViewById(R.id.search_image_btn);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("shopstore").child("product")
        .child("grocery");

        Toolbar toolbar = findViewById(R.id.search_toolbar);


        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final HashSet<String> hashSet = new HashSet<>();
        autoCompleteTextView.setDropDownBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(R.color.colorPrimaryText)));
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "BeforeTextChanged Chars:" + s.toString().trim() + " Start:" + start + " Count:" + count + " After: " + after);
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 2) {
                    Query firebaseQuery =  databaseReference.orderByChild("name").startAt(s.toString().trim())
                            .endAt(s.toString().trim() + "\uf8ff");

                    firebaseQuery.addChildEventListener(new ChildEventListener() {

                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Product model = dataSnapshot.getValue(Product.class);
                               try {
                                   if (model.getName() != null) {
                                       hashSet.add(model.getName());

                                       Log.i(TAG, model.getName());
                                   }
                               } catch (NullPointerException e) {
                                   Log.i(TAG, "Exception:" + e.getMessage());
                               }
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

                    Log.i(TAG, "OnTextChanged Chars:" + s.toString().trim() + " Start:" + start + " Count:" + count + " After: " + before);

                }
                String [] str = new String[hashSet.size()];
                Iterator<String> iterator = hashSet.iterator();
                for(int i=0; iterator.hasNext() ; i++){
                    str[i] = iterator.next();
                    Log.i(TAG, "Name:" + str[i]);
                }
                ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_dropdown_item_1line, str);

                autoCompleteTextView.setAdapter(listArrayAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "AfterTextChanged Chars:" + s.toString().trim() );

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseUserSearch(autoCompleteTextView.getText().toString().trim());
            }
        });


    }


    private void firebaseUserSearch(String searchText) {

        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = databaseReference.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Product, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, UsersViewHolder>(

                Product.class,
                R.layout.product_item,
                UsersViewHolder.class,
                firebaseSearchQuery

        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Product model, int position) {


                viewHolder.setDetails(getApplicationContext(), model.getName(), model.getBrand(), model.getPrice(), model.getWeight());

            }
        };

        searchRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDetails(Context context, String name, String brand, double price, double weight){

            TextView productName =  mView.findViewById(R.id.product_name_txt);
            TextView productBrand =  mView.findViewById(R.id.product_brand_txt);
            TextView productPrice = mView.findViewById(R.id.product_price);
            TextView productWeight = mView.findViewById(R.id.product_weight_txt);

            productName.setText(name);
            productBrand.setText(brand);
            productPrice.setText(String.valueOf(price));
            productWeight.setText(String.valueOf(weight));

        }

    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder{

        TextView dropDownText;
        public SuggestionViewHolder(View itemView) {
            super(itemView);
            dropDownText = itemView.findViewById(R.id.drop_down_txtv);
        }

    }

}



