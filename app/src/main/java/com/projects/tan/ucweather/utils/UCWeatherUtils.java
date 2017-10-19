package com.projects.tan.ucweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;

import com.projects.tan.ucweather.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UCWeatherUtils {

    public static final String ENDPOINT = "https://ucgroup-android.herokuapp.com/api/get-weather";
    public static final String DAY = "day";

    public static boolean hasNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static List<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            result.add(child);
            result.addAll(getAllChildren(child));
        }
        return result;
    }

    public static int getIconByWeather(String meteo) {
        switch (meteo) {
            case "cloudy":
                return R.drawable.ic_cloudy;
            case "sunny":
                return R.drawable.ic_sunny;
            case "snowy":
                return R.drawable.ic_snowy;
        }
        return 0;
    }

    public static String formatTemperature(double temperature) {
        return String.format(Locale.getDefault(), "%.1f C°", temperature);
    }
}
