package com.example.hp.shoppersstop;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * Created by hp on 09-04-2018.
 */

public class SearchAdapter extends ArrayAdapter<String> implements Filterable {
    public SearchAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                filterResults.count = getCount();
            }

            // do some other stuff

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            }
        }
    };
}