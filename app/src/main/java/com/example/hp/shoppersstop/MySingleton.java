package com.example.hp.shoppersstop;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by pc on 2/28/2018.
 */

public class MySingleton {

    private static MySingleton instance;
    private RequestQueue mRequestQueue;
    private Context mCtx;


    private MySingleton(Context context)
    {
        this.mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static  synchronized  MySingleton getInstance(Context context)
    {
        if(instance == null)
        {
           instance = new MySingleton(context);
        }

        return instance;
    }



    public RequestQueue getRequestQueue()
    {

        if(mRequestQueue==null)
        {

            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> request)
    {
        getRequestQueue().add(request);

    }




}
