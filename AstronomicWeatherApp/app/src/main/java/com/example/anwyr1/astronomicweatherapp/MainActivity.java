package com.example.anwyr1.astronomicweatherapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements
        SunFragment.OnFragmentInteractionListener, MoonFragment.OnFragmentInteractionListener {
    private static final int MillisecondsInMinute = 1000;
    private static AstroCalculator astroCalculator;
    private static AstroCalculator.Location location;
    private static AstroDateTime astroDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupCurrentTimeAndLocalization();
        setupAstronomicData();
    }

    private void setupCurrentTimeAndLocalization() {
        final TextView textView = findViewById(R.id.dataHeader);
        String tmp = SettingsActivity.getFromSettings("latitude", this);
        if (tmp == null) {
            tmp = getString(R.string.pref_default_display_latitude);
        }
        final String latitude = tmp;
        tmp = SettingsActivity.getFromSettings("longitude", this);
        if (tmp == null) {
            tmp = getString(R.string.pref_default_display_longitude);
        }
        final String longitude = tmp;
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
        String latitude = SettingsActivity.getFromSettings("latitude", this);
        if (latitude == null) {
            latitude = getString(R.string.pref_default_display_latitude);
        }
        String longitude = SettingsActivity.getFromSettings("longitude", this);
        if (longitude == null) {
            longitude = getString(R.string.pref_default_display_longitude);
        }
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        int timezoneOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET);
        astroDateTime = new AstroDateTime(year, month, day, hour, minute, second, timezoneOffset, false);
        location = new AstroCalculator.Location(Double.parseDouble(latitude),
                Double.parseDouble(longitude));
        astroCalculator = new AstroCalculator(astroDateTime, location);
    }

    private void setupAstronomicData() {
        String syncTimeString = SettingsActivity.getFromSettings("sync_frequency", this);
        if (syncTimeString == null) {
            syncTimeString = getString(R.string.pref_default_display_sync_time_value);
        }
        int time = Integer.parseInt(syncTimeString);
        time *= MillisecondsInMinute;
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
        final TextView moonSetDate = findViewById(R.id.moonSet);
        final TextView nextNewMoon = findViewById(R.id.nextNewMoon);
        final TextView nextFullMoon = findViewById(R.id.nextFullMoon);
        final TextView illumination = findViewById(R.id.illumination);
        final TextView monthAge = findViewById(R.id.monthAge);
        try {
            moonriseDate.setText(moonInfo.getMoonrise().toString());
            moonSetDate.setText(moonInfo.getMoonset().toString());
            nextNewMoon.setText(moonInfo.getNextNewMoon().toString());
            nextFullMoon.setText(moonInfo.getNextFullMoon().toString());
            illumination.setText(String.valueOf(moonInfo.getIllumination()));
            monthAge.setText(String.valueOf(moonInfo.getAge()));
        } catch (NullPointerException ex) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sync error");
            builder.setMessage("Something's gone wrong...");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.show();
        }
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
