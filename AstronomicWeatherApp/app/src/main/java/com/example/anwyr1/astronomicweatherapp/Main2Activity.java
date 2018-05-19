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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.drawer_layout)DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.dataHeader)TextView dataHeaderView;
    @BindView(R.id.main_activity)View astroCalendarView;
    @BindView(R.id.weather_activity)View weatherView;

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
        setupCurrentTimeAndLocalizationObservable();
        actualWeatherFragment = ((ActualWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.fragment4b));
        forecastFragment = ((ForecastFragment) getSupportFragmentManager().findFragmentById(R.id.fragment3b));
        if (mode.equals(weatherMode))
            setWeatherMode();
    }

    private void setupCurrentTimeAndLocalizationObservable() {
        final Context context = this;
        final Observable<String> timeAndLocalizationSubscription = Observable.create(emitter -> {
            String latitude = SettingsActivity.getFromSettings("latitude",
                    getString(R.string.pref_default_display_latitude), context);
            String longitude = SettingsActivity.getFromSettings("longitude",
                    getString(R.string.pref_default_display_longitude), context);
            String datetime = DateUtil.getCurrentDate();
            emitter.onNext(String.format("%s, %s \n%s", latitude, longitude, datetime));
            emitter.onComplete();
        });
        timeAndLocalizationSubscription
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(textToDisplay -> dataHeaderView.setText(textToDisplay));
        timeAndLocalizationSubscription
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .delay(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .repeat()
                .subscribe(textToDisplay -> dataHeaderView.setText(textToDisplay));
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
        Single<Integer> observer = Single.just(item.getItemId());
        observer
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(itemId -> {
                    if (itemId == R.id.action_settings)
                        startActivity(new Intent(this, SettingsActivity.class));
                    else if (itemId == R.id.action_updateWeatherData)
                        refreshWeather();
                }, System.err::println);

        return super.onOptionsItemSelected(item);
    }

    public void refreshWeather() {
        try {
            actualWeatherFragment.refreshCurrentWeather();
            forecastFragment.refreshCurrentWeather();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
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
        astroCalendarView.setVisibility(View.GONE);
        weatherView.setVisibility(View.VISIBLE);
    }

    private void setCalendarMode() {
        astroCalendarView.setVisibility(View.VISIBLE);
        weatherView.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
