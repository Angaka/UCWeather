package com.projects.tan.ucweather.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class UCWeatherManager {

    private static final String TAG = "UCWeatherManager";
    private static UCWeatherManager mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private UCWeatherManager(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized UCWeatherManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new UCWeatherManager(context);
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
