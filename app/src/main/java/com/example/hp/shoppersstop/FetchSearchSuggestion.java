package com.example.hp.shoppersstop;

import android.app.SearchManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by hp on 09-04-2018.
 */

public class FetchSearchSuggestion extends AsyncTask<String, Void, Cursor> {

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
}
