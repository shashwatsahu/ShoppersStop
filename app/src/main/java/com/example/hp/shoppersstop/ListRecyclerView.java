package com.example.hp.shoppersstop;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

import static com.example.hp.shoppersstop.EnterList.uid;

public class ListRecyclerView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ListItem>>,RecyclerView.OnItemTouchListener, View.OnClickListener, ActionMode.Callback {

    private static final String TAG = "ListRecyclerView";

    private GestureDetectorCompat gestureDetectorCompat;
    private ActionMode actionMode;
    private Context mContext;
    private ProgressBar progressBar;
    private RecyclerView mNumbersList;

    private FirebaseRecyclerAdapter<ListItem, ListViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recycler_view);
        Toast.makeText(ListRecyclerView.this, "Welcome Again" , Toast.LENGTH_SHORT).show();

        progressBar = findViewById(R.id.progress);

        Toolbar toolbar = findViewById(R.id.recycler_view_toolbar);
        setSupportActionBar(toolbar);

        try {

            getSupportActionBar().setDisplayHomeAsUpEnabled( true);
        } catch (NullPointerException e) {
            Log.i(TAG, "Exception:" + e);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        toolbar.setTitle(R.string.product_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

       // mAdapter = new MyAdapter();
        Query query = FirebaseDatabase.getInstance().getReference().child("shopstore").child("customer").child(uid).child("productList");

        Toast.makeText(ListRecyclerView.this, "UID:" + uid + " Query:" + query.getRef().toString(), Toast.LENGTH_SHORT).show();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ListItem, ListViewHolder> (ListItem.class, R.layout.card_view_recycler_view,
                ListViewHolder.class, query){
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ListItem model, int position) {

                       try {

                           viewHolder.quantity.setText(String.valueOf(model.getQuant()));
                           viewHolder.name.setText(model.getName());
                           viewHolder.brand.setText(model.getBrand());
                           viewHolder.price.setText( String.valueOf(model.getPrice()));
                           viewHolder.weight.setText(String .valueOf(model.getWeight()));

                           Log.i(TAG, "Position:" + viewHolder.getAdapterPosition() + "Name:" + model.getName());
                       } catch (NullPointerException e) {
                           Log.i(TAG, "Exception:" + e);
                       }
                     }
            @Override
            public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_view_recycler_view, parent, false);
                    Log.i("ListRecycler", "View");
                return new ListViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }



    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }



    private void removeItemFromList(){
       // int position = ((LinearLayoutManager) mNumbersList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        //QueryUtils.removeItemFromList(position);
        //mAdapter.removeData(position);
    }


    @Override
    public Loader<List<ListItem>> onCreateLoader(int id, Bundle args) {

        Log.i(TAG, "ON CREATE LOADER"+id);
        return null;//new EarthquakeLoader(MainActivity.this, args, id);

    }

    @Override
    public void onLoadFinished(Loader<List<ListItem>> loader, List<ListItem> data) {

        final Toast mToast = null;
        Log.i(TAG, "ON LOAD FINISHED");
        progressBar.setVisibility(View.INVISIBLE);

        if( data != null && !data.isEmpty()) {
          //  mAdapter = new EarthquakeRecyclerAdapter(data);
            //mNumbersList.setAdapter(mAdapter);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
            mNumbersList.addItemDecoration(itemDecoration);
            mNumbersList.setItemAnimator(new DefaultItemAnimator());
            mNumbersList.addOnItemTouchListener(this);
            gestureDetectorCompat = new GestureDetectorCompat(this, new RecyclerViewDemoOnGestureListener());
        }

    }

    @Override
    public void onLoaderReset(Loader<List<ListItem>> loader) {

        Log.i(TAG, "ON LOADER RESET");
       // layoutManager.removeAllViews();

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_cab_recyclerviewdemoactivity, menu);
        return true;    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch(item.getItemId()) {

            case R.id.menu_delete:
                /*List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
                Log.i(TAG,"mAdapter.getSelectedItems:" + mAdapter.getSelectedItems().size());
                int currPos;
                for(int i = selectedItemPositions.size() - 1; i >= 0; i--){

                    try {
                        currPos = Integer.parseInt(selectedItemPositions.get(i).toString());
                        QueryUtils.removeItemFromList(currPos);
                        Log.i(TAG, "curPos " + currPos + "selected position :" + i);
                        mAdapter.removeData(currPos);
                    }catch (NullPointerException e){
                        Log.i(TAG,"Null value "+ e+ " val:");

                    }
                }
                mode.finish();
                return true;*/
            default:
                return false;

        }

    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        this.actionMode = null;
      //  mAdapter.clearSelection();

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

        Log.i(TAG, "onClick child position:"+v.getId()+ " and containerlistitem:" +
                R.id.container_list_item+"\n container_inner_item"+ R.id.container_inner_item);
        if(v != null) {
            ListItem model = new ListItem();
            if (v.getId() == R.id.container_list_item) {
                int idx = mNumbersList.getChildAdapterPosition(v);
             //   Log.i(TAG, "onClick child position:" + model.id);
                if (actionMode != null) {
                    Log.i(TAG, "on single click not null");
                    myToggleSelection(idx);
                }
            }
        }

    }

    private void myToggleSelection(int idx){
      /*  mAdapter.toggleSelection(idx);
        String title = getString(R.string.selected_count, ""+mAdapter.getSelectedItemCount());
        actionMode.setTitle(title);*/
    }



    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e){
            View view = mNumbersList.findChildViewUnder(e.getX(), e.getY());
            Log.i(TAG,"On single tap :"+ e.getX()+" 2:"+ e.getY());
            if(view != null)
                onClick(view);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = mNumbersList.findChildViewUnder(e.getX(), e.getY());
            Log.i(TAG, "On long tap :" + e.getX() + " 2:" + e.getY());

            if (actionMode != null) {
                return;
            }

            int idx = 0;
            if (view != null) {
                actionMode = startActionMode(ListRecyclerView.this);
                if (view != null) {
                    idx = mNumbersList.getChildAdapterPosition(view);
                    int idL = mNumbersList.getChildLayoutPosition(view);
                    Log.i(TAG, "on Long click:" + view.getId() + "idA :" + idx + " idL :" + idL);

                    myToggleSelection(idx);
                    super.onLongPress(e);
                }
            }
        }
    }


    private class ListViewHolder extends RecyclerView.ViewHolder {

        private TextView name, brand, price, quantity, weight;

        private ListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_txt);
            brand = itemView.findViewById(R.id.brand_txt);
            price = itemView.findViewById(R.id.price_txt);
            quantity = itemView.findViewById(R.id.quantity_txt);
            weight = itemView.findViewById(R.id.weight_txt);
        }

    }
}
