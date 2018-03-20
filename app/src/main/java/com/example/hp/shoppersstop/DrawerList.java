package com.example.hp.shoppersstop;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by hp on 10-02-2018.
 */

public class DrawerList {

    private static final String TAG = "DrawerList";


    String text;
    Drawable drawable;

   public DrawerList(String text, Drawable drawable){
        this.text = text;
        this.drawable = drawable;
    }

    public String getText(){
       return this.text;
    }

    public Drawable getDrawable() {
         //Log.i(TAG, "drawable" + drawable.toString());
        return this.drawable;
    }

}
