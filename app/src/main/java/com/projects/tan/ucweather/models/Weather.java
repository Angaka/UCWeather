package com.projects.tan.ucweather.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable {

    @SerializedName("month")
    private String mMonth;
    @SerializedName("city")
    private String mCity;
    @SerializedName("days")
    private List<Day> mDays;

    public Weather() {
    }

    public Weather(String month, String city, List<Day> days) {
        mMonth = month;
        mCity = city;
        mDays = days;
    }

    public String getMonth() {
        return mMonth;
    }

    public void setMonth(String month) {
        mMonth = month;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public List<Day> getDays() {
        return mDays;
    }

    public void setDays(List<Day> days) {
        mDays = days;
    }
}
