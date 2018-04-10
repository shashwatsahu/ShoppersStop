package com.example.hp.shoppersstop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.transition.ChangeTransform;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.shoppersstop.Utils.RecyclerItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.hp.shoppersstop.EnterList.uid;

public class ListRecyclerView extends AppCompatActivity implements View.OnClickListener, RecyclerView.OnItemTouchListener{

    private static final String TAG = "ListRecyclerView";

    private GestureDetectorCompat gestureDetectorCompat;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;

    private ActionMode actionMode;
    private Toolbar toolbar;

    private DatabaseReference databaseReference;

    private FirebaseRecyclerAdapter<ListItem, ListViewHolder> firebaseRecyclerAdapter;

    private SparseBooleanArray selectedItems;

    private ArrayList<ListItem> user_list = new ArrayList<>();
    private ArrayList<ListItem> multiselect_list = new ArrayList<>();

    private boolean isMultiSelect = false;
    private ArrayList<ListItem> itemArrayList = new ArrayList<ListItem>();
    private static List<ListItem> demoModel;
    private static SparseArray<ListItem> demoMap;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            toolbar.setVisibility(View.GONE);
            MenuInflater inflater = mode.getMenuInflater();

            inflater.inflate(R.menu.menu_cab_recyclerviewdemoactivity, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_select:

                    Toast.makeText(ListRecyclerView.this, "Select", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_remove:
                    List<Integer> selectedItemPositions = getSelectedItems();
                    Log.i(TAG,"mAdapter.getSelectedItems:" + getSelectedItems().size());
                    int currPos;
                    for(int i = selectedItemPositions.size() - 1; i >= 0; i--){

                        try {
                            currPos = Integer.parseInt(selectedItemPositions.get(i).toString());
                            removeItemFromListQuery(currPos);
                            Log.i(TAG, "curPos " + currPos + "selected position :" + i);
                            removeData(currPos);

                            firebaseRecyclerAdapter.notifyItemRemoved(currPos);
                        }catch (NullPointerException e){
                            Log.i(TAG,"Null value "+ e+ " val:");

                        }
                    }
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            clearSelection();
            Log.i(TAG, "onDestroy!");
            isMultiSelect = false;
            toolbar.setVisibility(View.VISIBLE);
        }
    };

    private void removeItemFromList(){
        int position = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
       removeItemFromListQuery(position);
        firebaseRecyclerAdapter.notifyItemRemoved(position);
    }

    private void removeItemFromListQuery(int position) {
        try {
            Log.i(TAG, "position:"+position);
            ListItem listItem = demoModel.get(position);
            Log.i(TAG, "Name:" + listItem.getName());
            demoModel.remove(position);
            demoMap.remove(demoModel.get(position).id);

            Task<Void> task = databaseReference.child(listItem.getName()).removeValue();

            firebaseRecyclerAdapter.notifyItemRemoved(position);
            Log.i(TAG, "Is Successfull:" + task.isSuccessful());



        } catch (IndexOutOfBoundsException e){
            Log.i(TAG, "Index out of bounds:"+e+ "Position :"+position+"total Demo Model size:"+demoModel.size()+" total size DemoMap:"+demoMap.size());
        }
        catch(NullPointerException e){
            Log.i(TAG, "Null Pointer Exception:" + e);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setAllowReturnTransitionOverlap(true);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSharedElementExitTransition(new ChangeTransform());

        setContentView(R.layout.activity_list_recycler_view);
        Toast.makeText(ListRecyclerView.this, "Welcome Again" , Toast.LENGTH_SHORT).show();


        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);

        demoModel = new ArrayList<>();
        demoMap = new SparseArray<>();
        selectedItems = new SparseBooleanArray();

        toolbar = findViewById(R.id.recycler_view_toolbar);
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


        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = findViewById
                (R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

       // mAdapter = new MyAdapter();
        Query query = FirebaseDatabase.getInstance().getReference().child("shopstore")
                .child("customer").child(uid).child("productList");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("shopstore")
                .child("customer").child(uid).child("productList");

        Toast.makeText(ListRecyclerView.this, "UID:" + uid + " Query:" + query.getRef().toString(), Toast.LENGTH_SHORT).show();



        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ListItem, ListViewHolder> (ListItem.class, R.layout.item_demo_01,
                ListViewHolder.class, query){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void populateViewHolder(ListViewHolder viewHolder, ListItem model, int position) {

                       try {
                            user_list.add(model);
                           viewHolder.quantity.setText(String.valueOf(model.getQuant()));
                           viewHolder.name.setText(model.getName());
                           viewHolder.brand.setText(model.getBrand());
                           viewHolder.price.setText( String.valueOf(model.getPrice()));
                           viewHolder.weight.setText(String .valueOf(model.getWeight()));

                           viewHolder.itemView.setBackgroundColor(selectedItems.get(position)? 0x9934B5E4
                                   : Color.TRANSPARENT);

                           Log.i(TAG, "Position:" + viewHolder.getAdapterPosition() + "Name:" + model.getName());


                           itemArrayList.add(new ListItem(model.getName(),model.getBrand(),
                                   model.getQuant(), model.getPrice() ,model.getWeight()));

                           demoModel.add(new ListItem(model.getName(),model.getBrand(),
                                   model.getQuant(), model.getPrice() ,model.getWeight()));

                           demoMap.put(model.id, new ListItem(model.getName(),model.getBrand(),
                                   model.getQuant(), model.getPrice() ,model.getWeight()));

                           Log.i(TAG, model.getName());

                       } catch (NullPointerException e) {
                           Log.i(TAG, "Exception:" + e);
                       }
                     }

            @Override
            public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_demo_01, parent, false);

                return new ListViewHolder(view);
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        final RecyclerView.ItemDecoration itemDecoration = new android.support.v7.widget.DividerItemDecoration(this,
                android.support.v7.widget.DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

       mRecyclerView.addOnItemTouchListener(this);
        //RecyclerView onItemTouchListener is implemented below...
       /* mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) {
                    multiSelect(position, view);
                }
                else
                    Toast.makeText(getApplicationContext(), "Details Page:" + mRecyclerView.getChildAdapterPosition(view), Toast.LENGTH_SHORT).show();
                  int pos = mRecyclerView.getChildAdapterPosition(view);

            }

            @Override
            public void onItemLongClick(View view, int position) {

                Log.i(TAG, "actionMode:" + actionMode);

                if(actionMode != null) {
                    isMultiSelect = true;

                }

                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<ListItem>();
                    actionMode = startActionMode(mActionModeCallback);
                    isMultiSelect = true;

                    Log.i(TAG, "Position long click:" + view.getId());
                }
                multiSelect(position, view);
            }
        }));*/
        gestureDetectorCompat = new GestureDetectorCompat(this, new RecyclerViewDemoOnGestureListener());

        Log.i(TAG, "Touch?" + mRecyclerView.performClick());

    }


    public void multiSelect(int position, View view) {
    Log.i(TAG, "multiselect");

       if (actionMode != null) {
            if (multiselect_list.contains(user_list.get(position))){
                multiselect_list.remove(user_list.get(position));
            Log.i(TAG, "remove");
            }
            else{
                multiselect_list.add(user_list.get(position));
                Log.i(TAG, "add");

            }

            if (multiselect_list.size() > 0)
                actionMode.setTitle("" + multiselect_list.size());
            else
                actionMode.setTitle("");

            refreshAdapter(position);
        }
    }


    public void refreshAdapter(int position)
    {
       /* multiSelectAdapter.selected_usersList=multiselect_list;
        multiSelectAdapter.usersList=user_list;
        multiSelectAdapter.notifyDataSetChanged();*/

       firebaseRecyclerAdapter.notifyItemRemoved(position);
       firebaseRecyclerAdapter.notifyDataSetChanged();
       Log.i(TAG, "refreshAdapter");
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick child position:"+v.getId()+ " and containerlistitem:" + R.id.container_list_item
                +"\n container_inner_item"+ R.id.container_inner_item + " action Mode:" + actionMode);
        if(v != null) {
            ListItem model = new ListItem();
            if (v.getId() == v.getId()) {
                int idx = mRecyclerView.getChildAdapterPosition(v);

                if (actionMode != null) {
                    Log.i(TAG, "on single click not null" + model.id);
                    myToggleSelection(idx);

                }
            }
        }
    }

   private void myToggleSelection(int idx){

        toggleSelection(idx);
        String title = getString(R.string.selected_counts, getSelectedItemCount());
        actionMode.setTitle(title);
        Log.i(TAG, "myToggleSelection:" + idx);
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

    //Extended part of RecyclerAdapter....

    private List<ListItem> list;



    public void toggleSelection(int pos){

        Log.i(TAG, "toggleSelection:" + pos);
        if(selectedItems.get(pos, false)){
            selectedItems.delete(pos);
        }
        else {

            selectedItems.put(pos, true);
        }
        firebaseRecyclerAdapter.notifyItemChanged(pos);
    }

    public static ListItem findById(int id) {
        ListItem earthquake = demoMap.get(id);
        ListItem earthquake1 = demoMap.valueAt(id);
        Log.i(TAG, "earthquake Map:"+earthquake.getName() + " earthquake map" + earthquake1.getBrand());
        return demoMap.get(id);
    }

    public void clearSelection(){
        selectedItems.clear();
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public int getSelectedItemCount(){
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems(){
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for(int i = 0; i < selectedItems.size(); i++){
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }


    public void addData(ListItem listItem, int position){
        list.add(position, listItem);
        firebaseRecyclerAdapter.notifyItemInserted(position);
    }

    public void removeData(int position){
        Log.i(TAG, "position:"+ position);
        try {
            list.remove(position);

        } catch (NullPointerException e){
            Log.i(TAG, "null value :" + e + "value:" + position);
        }
        firebaseRecyclerAdapter.notifyItemRemoved(position);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener{

        private TextView name, brand, price, quantity, weight;

        private ListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_txt);
            brand = itemView.findViewById(R.id.brand_txt);
            price = itemView.findViewById(R.id.price_txt);
            quantity = itemView.findViewById(R.id.quantity_txt);
            weight = itemView.findViewById(R.id.weight_txt);

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(ListRecyclerView.this, "Onclick me" + position , Toast.LENGTH_SHORT).show();
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

        public void onLongPress(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            Log.i(TAG, "On long tap :" + e.getX() + " 2:" + e.getY());

            if (actionMode != null) {
                return;
            }

            int idx;
            if (view != null) {
                actionMode = startActionMode(mActionModeCallback);
                if (view != null) {
                    idx = mRecyclerView.getChildAdapterPosition(view);
                    int idL = mRecyclerView.getChildLayoutPosition(view);
                    Log.i(TAG, "on Long click:" + view.getId() + "idA :" + idx + " idL :" + idL);

                    myToggleSelection(idx);
                    super.onLongPress(e);
                }
            }
        }
    }

}
