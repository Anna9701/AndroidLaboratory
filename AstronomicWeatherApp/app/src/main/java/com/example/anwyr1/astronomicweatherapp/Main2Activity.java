package com.example.anwyr1.astronomicweatherapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.anwyr1.astronomicweatherapp.Fragments.ActualWeatherFragment;
import com.example.anwyr1.astronomicweatherapp.Fragments.ForecastFragment;
import com.example.anwyr1.astronomicweatherapp.Fragments.MoonFragment;
import com.example.anwyr1.astronomicweatherapp.Fragments.SunFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MoonFragment.OnFragmentInteractionListener, SunFragment.OnFragmentInteractionListener,
        ForecastFragment.OnFragmentInteractionListener,
        ActualWeatherFragment.OnFragmentInteractionListener {

    private ActualWeatherFragment actualWeatherFragment;
    private ForecastFragment forecastFragment;
    private final static String calendarMode = "calendar";
    private final static String weatherMode = "weather";
    private static String mode = calendarMode;
    private static final DateFormat dateFormatOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.GERMAN);

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.drawer_layout)DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.dataHeader)TextView dataHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        setupCurrentTimeAndLocalization();
        actualWeatherFragment = ((ActualWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4b));
        forecastFragment = ((ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment3b));
        if (mode.equals(weatherMode))
            setWeatherMode();
    }

    private void setupCurrentTimeAndLocalization() {
        final Context context = this;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        String latitude = SettingsActivity.getFromSettings("latitude",
                                getString(R.string.pref_default_display_latitude), context);
                        String longitude = SettingsActivity.getFromSettings("longitude",
                                getString(R.string.pref_default_display_longitude), context);
                        Date currentTime = Calendar.getInstance().getTime();
                        String time = dateFormatOut.format(currentTime);
                        dataHeaderView.setText(String.format("%s, %s \n%s", latitude, longitude, time));
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.action_updateWeatherData) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        refreshWeather();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshWeather() {
        actualWeatherFragment.refreshCurrentWeather();
        forecastFragment.refreshCurrentWeather();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_weather) {
            mode = weatherMode;
            setWeatherMode();
        } else if (id == R.id.nav_calendar) {
            mode = calendarMode;
            setCalendarMode();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setWeatherMode() {
        findViewById(R.id.main_activity).setVisibility(View.GONE);
        findViewById(R.id.weather_activity).setVisibility(View.VISIBLE);
    }

    private void setCalendarMode() {
        findViewById(R.id.main_activity).setVisibility(View.VISIBLE);
        findViewById(R.id.weather_activity).setVisibility(View.GONE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
