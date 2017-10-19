package com.projects.tan.ucweather.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Day implements Serializable {

    @SerializedName("day")
    private String mDay;
    @SerializedName("meteo")
    private String mMeteo;
    @SerializedName("temperature")
    private Double mTemperature;

    public Day() {

    }

    public Day(String day, String meteo, Double temperature) {
        mDay = day;
        mMeteo = meteo;
        mTemperature = temperature;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public String getMeteo() {
        return mMeteo;
    }

    public void setMeteo(String meteo) {
        mMeteo = meteo;
    }

    public Double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(Double temperature) {
        mTemperature = temperature;
    }
}
