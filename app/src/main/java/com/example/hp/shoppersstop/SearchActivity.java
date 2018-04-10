package com.example.hp.shoppersstop;

import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 10-02-2018.
 */

public class SearchActivity extends FragmentActivity/*implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener*/{

    private static final String TAG = "SearchActivity";
    private static final int SEARCH_QUERY_THRESHOLD = 2;
    // private static final String TAG = "SearchActivity";
        private Spinner spinner, spinnerBrand, spinnerDepartment, spinnerPrice, spinnerWeight;

        private static final int PRODUCT = 0;
        private static final int SHOP = 1;

      //  private ArrayList<String> arrayListBrand;

        private Toolbar toolbarProduct;
        //private ListView listView;
        //private SimpleCursorAdapter simpleCursorAdapter;

      //  private SQLiteDatabase sqLiteDatabase;
    //    private ProductDbHelper dbHelper;
   // public SearchView searchView;

    ArrayList<String> arrayListBrand;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        //searchView = findViewById(R.id.search_view);

        Toolbar toolbar = findViewById(R.id.search_toolbar);

        arrayListBrand = new ArrayList<>();

        DatabaseReference databaseReferenceBrand = FirebaseDatabase.getInstance().getReference().child("shopstore").child("product").child("grocery").child("brand");
        databaseReferenceBrand.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                arrayListBrand.add((String)dataSnapshot.getValue());
                Toast.makeText(SearchActivity.this, "Brand: " +  dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                arrayListBrand.add((String) dataSnapshot.getValue());
                Toast.makeText(SearchActivity.this, "List:" + dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Size:" + arrayListBrand.size());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Brand name cancel!");
            }
        });

        if(arrayListBrand.size() != 0) {

            String[] arrays = (String[]) arrayListBrand.toArray();

            final ArrayAdapter listArrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, arrays);

            final AutoCompleteTextView textView =
                    toolbar.findViewById(R.id.autocompletetextview);

            textView.setAdapter(listArrayAdapter);

            textView.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // no need to do anything
                }

                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (((AutoCompleteTextView) textView).isPerformingCompletion()) {
                        return;
                    }
                    if (charSequence.length() < 2) {
                        return;
                    }
                    String query = charSequence.toString();
                    listArrayAdapter.clear();

                    List<String> data = new ArrayList<String>();
                    data.add(query);


                    if (arrayListBrand.size() != 0) {
                        // transform from json to your object
                        listArrayAdapter.add(arrayListBrand);
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
        /*dbHelper = new ProductDbHelper(this);

        sqLiteDatabase = dbHelper.getReadableDatabase();*/

        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView.setSuggestionsAdapter(new android.support.v4.widget.SimpleCursorAdapter(
                SearchActivity.this, android.R.layout.simple_list_item_1, null,
                new String[] { SearchManager.SUGGEST_COLUMN_TEXT_1 },
                new int[] { android.R.id.text1 }, 0));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String query) {

                if (query.length() >= SEARCH_QUERY_THRESHOLD) {
                    new FetchSearchSuggestion().execute(query);
                } else {
                    searchView.getSuggestionsAdapter().changeCursor(null);
                }

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                // if user presses enter, do default search, ex:
                if (query.length() >= SEARCH_QUERY_THRESHOLD) {

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query);
                    startActivity(intent);

                    searchView.getSuggestionsAdapter().changeCursor(null);
                    return true;
                }
                return false;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {

            @Override
            public boolean onSuggestionSelect(int position) {

                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                String term = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
                cursor.close();

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEARCH);
                intent.putExtra(SearchManager.QUERY, term);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {

                return onSuggestionSelect(position);
            }
        });
        // Assumes current activity is the searchable activity
      /*  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               Toast.makeText(SearchActivity.this, "Submit" + query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(SearchActivity.this, "Submit" + newText, Toast.LENGTH_SHORT).show();

                return true;
            }
        });*/

       /* listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ListActivity.class);

                // Creating a uri to fetch country details corresponding to selected listview item
                Uri data = Uri.withAppendedPath(ProductContentProvider.CONTENT_URI, String.valueOf(id));

                // Setting uri to the data on the intent
                intent.setData(data);

                // Open the activity
                startActivity(intent);
            }
        });*/

       /* String[] projection = {ProductContract.ProductEntry._ID, ProductContract.ProductEntry.PRODUCT_NAME};
        String selection = ProductContract.ProductEntry.PRODUCT_NAME + " LIKE ?";


        Log.i(TAG, "1Loader");

        //Cursor cursor = getContentResolver().query(ProductContentProvider.CONTENT_URI, projection, selection, null, null);

        Cursor cursor = sqLiteDatabase.query(ProductContract.ProductEntry.TABLE_NAME, projection, selection, null, null, null, null);

          Toast.makeText(this, "row:" + cursor.getCount(), Toast.LENGTH_SHORT).show();
          Log.i(TAG, "cursor:" +  cursor.getCount());
        // Defining CursorAdapter for the ListView

        cursor.moveToFirst();
        Log.i(TAG, "1moveToFirst()");

        simpleCursorAdapter = new SimpleCursorAdapter(getBaseContext(),
                android.R.layout.simple_list_item_1, cursor,
                new String[] {ProductContract.ProductEntry._ID, ProductContract.ProductEntry.PRODUCT_NAME},
                new int[] { android.R.id.text1, android.R.id.text2}, 0);

        // Setting the cursor adapter for the country listview
        listView.setAdapter(simpleCursorAdapter);

        handleIntent(getIntent());

        Toolbar toolbar = findViewById(R.id.search_toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        toolbar.setTitle(R.string.product_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DatabaseReference databaseReferenceBrand;
        DatabaseReference databaseReferencePrice;
        DatabaseReference databaseReferenceWeight;
        DatabaseReference databaseReferenceDepartment;

        arrayListBrand = new ArrayList<String>();

       */ databaseReferenceBrand = FirebaseDatabase.getInstance().getReference().child("shopstore").child("product").child("grocery").child("brand");
        /*databaseReferenceDepartment = FirebaseDatabase.getInstance().getReference().child("shopstore").child("product").child("department");
        databaseReferencePrice = FirebaseDatabase.getInstance().getReference().child("shopstore").child("product");
        databaseReferenceWeight = FirebaseDatabase.getInstance().getReference().child("shopstore").child("product");

        databaseReferenceBrand.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                arrayListBrand.add((String)dataSnapshot.getValue());
               // Toast.makeText(SearchActivity.this, "Brand: " +  dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                arrayListBrand.add((String) dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i(TAG, "Brand name cancel!");
            }
        });

        toolbarProduct = findViewById(R.id.toolbar_product);
        toolbarProduct.setVisibility(View.INVISIBLE);

        spinner = findViewById(R.id.spinner);*/

        // TODO: 10-02-2018
      /*  ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.spinner_list, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
           */
       // handleIntent(getIntent());
    }
        //search suggestion...

    /*private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
                doSearch(searchQuery);
            |*CustomSearchAdapter adapter = new CustomSearchAdapter(this,
                    android.R.layout.simple_dropdown_item_1line, );
           * |listView.setAdapter(adapter);

        }else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
            String selectedSuggestionRowId =  intent.getDataString();
            //execution comes here when an item is selected from search suggestions
            //you can continue from here with user selected search item
            Toast.makeText(this, "selected search suggestion "+selectedSuggestionRowId,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void doSearch(String query) {
        Bundle data = new Bundle();
        data.putString("query", query);

        //Invoking onCreateLoader() in non-ui thread
        getSupportLoaderManager().initLoader(1, data, (android.support.v4.app.LoaderManager.LoaderCallbacks) this);

    }

       |*
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri detailUri = intent.getData();
            String id = detailUri.getLastPathSegment();
            Intent detailsIntent = new Intent(getApplicationContext(), ListItem.class);
            detailsIntent.putExtra("ID", id);
            startActivity(detailsIntent);
            finish();
        }
    }*|

    |*private void doSearch(String query) {
        CustomSearchAdapter adapter = new CustomSearchAdapter(this,
                android.R.layout.simple_dropdown_item_1line,
                StoresData.filterData(searchQuery));
        listView.setAdapter(adapter);
    }*|

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] spinnerItems = getResources().getStringArray(R.array.spinner_list);

        switch ((int)parent.getItemIdAtPosition(position)) {
            case  PRODUCT:
                toolbarProduct.setVisibility(View.VISIBLE);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("shopstore")
                        .child("product");

                spinnerBrand = findViewById(R.id.spinner_brand);
                ArrayAdapter<String> arrayAdapterBrand = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayListBrand);
                arrayAdapterBrand.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerBrand.setAdapter(arrayAdapterBrand);
                Toast.makeText(SearchActivity.this, "Choice:" + spinnerItems[position], Toast.LENGTH_SHORT).show();
                break;

            case SHOP :
                toolbarProduct.setVisibility(View.INVISIBLE);
                Query query = FirebaseDatabase.getInstance().getReference().child("shopstore").child("shop").child("seller");
                Toast.makeText(SearchActivity.this, "Choice:" + spinnerItems[position], Toast.LENGTH_SHORT).show();
                break;

                default:
                Toast.makeText(SearchActivity.this, "Choice" + spinnerItems[position], Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(SHOP);

        Toast.makeText(SearchActivity.this, "Nothing Selected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ProductContentProvider.CONTENT_URI;
        String[] projection = {ProductContract.ProductEntry.ID, ProductContract.ProductEntry.PRODUCT_NAME};
        Log.i(TAG, "Loader");
        return new CursorLoader(getBaseContext(), uri, projection, null, new String[]{ProductContract.ProductEntry._ID}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }*/

   /* private static class FetchSearchSuggestion extends AsyncTask<String, Void, Cursor> {

        private static final String TAG = "FetchSearchSuggestion";

        private static final String[] sAutocompleteColNames = new String[] {
                BaseColumns._ID,                         // necessary for adapter
                SearchManager.SUGGEST_COLUMN_TEXT_1      // the full search term
        };

        @Override
        protected Cursor doInBackground(String... params) {

            final MatrixCursor cursor = new MatrixCursor(sAutocompleteColNames);

            // get your search terms from the server here, ex:
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("shopstore")
                    .child("product").child("brand");

            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    for(int i=0; i < dataSnapshot.getChildrenCount(); i++){
                        String term = (String) dataSnapshot.getValue();

                        Object[] row = new Object[] { i, term };
                        cursor.addRow(row);
                        Log.i(TAG, "child:" + term);
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

            // parse your search terms into the MatrixCursor

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor result) {

            SearchActivity searchActivity = new SearchActivity();

            searchActivity.searchView.getSuggestionsAdapter().changeCursor(result);
        }
    }*/

}
