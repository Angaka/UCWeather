package com.projects.tan.ucweather.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marcohc.robotocalendar.RobotoCalendarView;
import com.projects.tan.ucweather.R;
import com.projects.tan.ucweather.models.Day;
import com.projects.tan.ucweather.models.Weather;
import com.projects.tan.ucweather.network.UCWeatherManager;
import com.projects.tan.ucweather.utils.UCWeatherUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener
        , RobotoCalendarView.RobotoCalendarListener
        , Response.Listener<String>
        , Response.ErrorListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.cardViewCalendar)
    CardView mCardViewCalendar;
    @BindView(R.id.calendarView)
    RobotoCalendarView mCalendarView;

    @BindView(R.id.textViewNoNetwork)
    TextView mTextViewNoNetwork;
    @BindView(R.id.buttonRefresh)
    Button mButtonRefresh;

    private ProgressDialog mProgressDialog;

    private Weather mWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mCalendarView.setRobotoCalendarListener(this);
        mButtonRefresh.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.progress_dialog_loader));
        mProgressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);

        executeRequest();
    }

    private void executeRequest() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
        if (UCWeatherUtils.hasNetworkConnection(this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, UCWeatherUtils.ENDPOINT, this, this);
            UCWeatherManager.getInstance(this).addToRequestQueue(stringRequest);
        } else {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mCardViewCalendar.setVisibility(View.GONE);
            mTextViewNoNetwork.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onDayClick(Calendar calendar) {
        Day dayClicked = new Day();
        for (Day day : mWeather.getDays()) {
            if (Integer.valueOf(day.getDay()) == calendar.get(Calendar.DAY_OF_MONTH)) {
                dayClicked = day;
                break;
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault());
        FancyAlertDialog.Builder mDetailDialog = new FancyAlertDialog.Builder(this)
                .setImageDrawable(AppCompatResources.getDrawable(this, UCWeatherUtils.getIconByWeather(dayClicked.getMeteo())))
                .setTextTitle(UCWeatherUtils.formatTemperature(dayClicked.getTemperature()))
                .setTextSubTitle(dateFormat.format(calendar.getTime()))
                .setBody(mWeather.getCity())
                .setPositiveButtonText(R.string.close)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                .setCancelable(true)
                .build();
        mDetailDialog.show();
    }

    @Override
    public void onDayLongClick(Calendar calendar) {
    }

    @Override
    public void onRightButtonClick() {
    }

    @Override
    public void onLeftButtonClick() {
    }

    private void updateWeatherInfo(Weather weather) {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        try {
            date = sdf.parse(weather.getMonth());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        mCalendarView.setCalendar(calendar);

        View calendarRootView = mCalendarView.getRootView();
        for (View view : UCWeatherUtils.getAllChildren(calendarRootView)) {
            switch (view.getId()) {
                case R.id.leftButton:
                case R.id.rightButton:
                    view.setVisibility(View.GONE);
                    break;
            }
            if (view.getTag() != null) {
                String tagView = view.getTag().toString();
                if (tagView.contains("dayOfTheMonthText")) {
                    TextView textView = (TextView) view;
                    for (Day day : weather.getDays()) {
                        if (!textView.getText().toString().trim().isEmpty()) {
                            if (Integer.valueOf(textView.getText().toString()).intValue()
                                    == Integer.valueOf(day.getDay()).intValue()) {
                                textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                                textView.setBackground(ContextCompat.getDrawable(this, UCWeatherUtils.getIconByWeather(day.getMeteo())));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        mCardViewCalendar.setVisibility(View.GONE);
        mTextViewNoNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResponse(String response) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        mCardViewCalendar.setVisibility(View.VISIBLE);
        mTextViewNoNetwork.setVisibility(View.GONE);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        JsonObject responseObject = new JsonParser().parse(response).getAsJsonObject();
        JsonObject weatherObject = responseObject.getAsJsonObject("weather");

        mWeather = gson.fromJson(weatherObject, Weather.class);
        updateWeatherInfo(mWeather);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRefresh:
                executeRequest();
                break;
        }
    }
}
