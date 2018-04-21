package com.example.anwyr1.astronomicweatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Timer timer;
    private TimerTask timerTask;
    private static final int MilisecondsInMinute = 1000;
    private static AstroCalculator astroCalculator;
    private static AstroCalculator.Location location;
    private static AstroDateTime astroDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupCurrentTimeAndLocalization();
        setupAstronimicData();
    }

    private void setupCurrentTimeAndLocalization() {
        final TextView textView = findViewById(R.id.dataHeader);
        final String latitude = SettingsActivity.getFromSettings("latitude", this);
        final String longitude = SettingsActivity.getFromSettings("longitude", this);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        Date currentTime = Calendar.getInstance().getTime();
                        textView.setText(String.format("%s, %s \n%s", latitude, longitude, currentTime.toString()));
                    }
                });
            }
        }, 0, 1000);
    }

    private void updateAstroCalendar() {
        final String latitude = SettingsActivity.getFromSettings("latitude", this);
        final String longitude = SettingsActivity.getFromSettings("longitude", this);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        int timezeoneOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET);
        astroDateTime = new AstroDateTime(year, month, day, hour, minute, second, timezeoneOffset, false);
        location = new AstroCalculator.Location(Double.parseDouble(latitude),
                Double.parseDouble(longitude));
        astroCalculator = new AstroCalculator(astroDateTime, location);
    }

    private void setupAstronimicData() {
        updateAstroCalendar();
        int time = Integer.parseInt(SettingsActivity.getFromSettings("sync_frequency", this));
        time *= MilisecondsInMinute;
        final Context context = this;
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        updateAstroCalendar();
                        loadSunRelatedData();
                        loadMoonRelatedData();
                    }
                });
            }
        }, 0, time);
    }

    private void loadSunRelatedData() {
        AstroCalculator.SunInfo sunInfo = astroCalculator.getSunInfo();
        final TextView sunriseDate = findViewById(R.id.sunriseTime);
        final TextView sunriseAzimuth = findViewById(R.id.sunriseAzimuth);
        final TextView sunsetDate = findViewById(R.id.sunsetTime);
        final TextView sunsetAzimuth = findViewById(R.id.sunsetAzimuth);
        final TextView civilSunrise = findViewById(R.id.civilSunrise);
        final TextView civilSunset = findViewById(R.id.civilSunset);
        sunriseDate.setText(sunInfo.getSunrise().toString());
        sunriseAzimuth.setText(String.valueOf(sunInfo.getAzimuthRise()));
        sunsetDate.setText(sunInfo.getSunset().toString());
        sunsetAzimuth.setText(String.valueOf(sunInfo.getAzimuthSet()));
        civilSunrise.setText(sunInfo.getTwilightMorning().toString());
        civilSunset.setText(sunInfo.getTwilightEvening().toString());
    }

    private void loadMoonRelatedData() {
        AstroCalculator.MoonInfo moonInfo = astroCalculator.getMoonInfo();
        final TextView moonriseDate = findViewById(R.id.moonRise);
        final TextView moonsetDate = findViewById(R.id.moonSet);
        final TextView nextNewMoon = findViewById(R.id.nextNewMoon);
        final TextView nextFullMoon = findViewById(R.id.nextFullMoon);
        final TextView illumination = findViewById(R.id.illumination);
        final TextView monthAge = findViewById(R.id.monthAge);
        moonriseDate.setText(moonInfo.getMoonrise().toString());
        moonsetDate.setText(moonInfo.getMoonset().toString());
        nextNewMoon.setText(moonInfo.getNextNewMoon().toString());
        nextFullMoon.setText(moonInfo.getNextFullMoon().toString());
        illumination.setText(String.valueOf(moonInfo.getIllumination()));
        monthAge.setText(String.valueOf(moonInfo.getAge()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
