package com.example.hp.shoppersstop;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.SoftReference;

/**
 * Created by hp on 25-02-2018.
 */

public class ShopCategoryItem {
    private Drawable drawable;
    private String textView;

    public ShopCategoryItem(Drawable drawable, String textView) {
        this.drawable = drawable;
        this.textView = textView;
    }

    public Drawable getImageView() {
        return drawable;
    }

    public String  getTextView() {
        return textView;
    }

}
