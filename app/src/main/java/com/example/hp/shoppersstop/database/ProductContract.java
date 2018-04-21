package com.example.hp.shoppersstop.database;

import android.provider.BaseColumns;

/**
 * Created by hp on 10-03-2018.
 */

public final class ProductContract {

    private ProductContract() {}

    public static class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "product";
        public static final String Database_Name = "product_grocery.db";
        public static final String PRODUCT_NAME = "SUGGEST_COLUMN_TEXT_1";
        public static final String ID = "_id";
    }

}
