package com.example.hp.shoppersstop.database;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.hp.shoppersstop.SearchActivity;

/**
 * Created by hp on 13-03-2018.
 */

public class ProductContentProvider extends ContentProvider {

    private static final int PRODUCT = 1;
    private static final int PRODUCT_ID = 2;
    private static final int PRODUCT_SUGGESTION = 3;

    public static final String PROVIDER_NAME = "com.example.hp.shoppersstop";

    public static final String URL = "content://" + PROVIDER_NAME + "/product_grocery";
    public static final String URL_ID ="content://" + PROVIDER_NAME + "/product_grocery/#";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    public static final Uri CONTENT_URI_ID = Uri.parse(URL_ID);

    private static final String TAG = "ProductContentPorvider";
    private ProductDbHelper productDbHelper;

    private static final UriMatcher sUriMatcher= new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PROVIDER_NAME, SearchManager.SUGGEST_URI_PATH_QUERY, PRODUCT_SUGGESTION);
        sUriMatcher.addURI(PROVIDER_NAME,"product_grocery",PRODUCT);
        sUriMatcher.addURI(PROVIDER_NAME,"product_grocery/#",PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
         productDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase sqLiteDatabase = productDbHelper.getReadableDatabase();
        Cursor cursor = null;


        switch(sUriMatcher.match(uri)){
            case PRODUCT_SUGGESTION :
                Log.i(TAG, "11Loader");

                cursor = productDbHelper.getProduct(selectionArgs[0]);
                break;
            case PRODUCT :
                Log.i(TAG, "22Loader");

                cursor = productDbHelper.getProducts(selectionArgs);
                break;
            case PRODUCT_ID :
                Log.i(TAG, "33Loader");

                String id = uri.getLastPathSegment();

                cursor = productDbHelper.getProduct(selectionArgs[0]);
        }

        if (selectionArgs != null && selectionArgs.length > 0 && selectionArgs[0].length() > 0) {
            // the entered text can be found in selectionArgs[0]
            // return a cursor with appropriate data
            Log.i(TAG, "1 selection Args");

            cursor = sqLiteDatabase.query(ProductContract.ProductEntry.TABLE_NAME, new String[] {ProductContract.ProductEntry.ID, ProductContract.ProductEntry.PRODUCT_NAME}, ProductContract.ProductEntry.PRODUCT_NAME + " LIKE ?", selectionArgs, null, null, null);

            cursor.moveToFirst();

            Log.i(TAG, "Cursor position:" + cursor.getPosition());

            String query = uri.getLastPathSegment();
            Log.i(TAG, "selection:" + selection + " query:" + query + " " + uri.getQuery());

            Toast.makeText(getContext(), "Product:" + uri.getLastPathSegment() + " sel:" + selection + " selArg:" + selectionArgs[0] +
                    " Value: " + cursor.getString(1), Toast.LENGTH_SHORT).show();

        }
        else {
            // user hasn't entered anything
            // thus return a default cursor
            Log.i(TAG, "empty");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
