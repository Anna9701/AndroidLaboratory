package com.example.anwyr1.astronomicweatherapp;

import android.content.Context;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;

/**
 * Created by anwyr1 on 28/04/2018.
 */

public class AstronomicCalculator {
    private AstroCalculator astroCalculator;
    private static AstronomicCalculator astronomicCalculator;

    private AstronomicCalculator() {}

    public static AstronomicCalculator getInstance(Context context) {
        if (astronomicCalculator == null) {
            astronomicCalculator = new AstronomicCalculator();
            astronomicCalculator.updateAstroCalendar(context);
        }
        return astronomicCalculator;
    }

    public void updateAstroCalendar(Context context) {
        String latitude = SettingsActivity.getFromSettings("latitude",
                context.getString(R.string.pref_default_display_latitude), context);
        String longitude = SettingsActivity.getFromSettings("longitude",
                context.getString(R.string.pref_default_display_longitude), context);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int second = Calendar.getInstance().get(Calendar.SECOND);

        int timezoneOffset =  Calendar.getInstance().get(Calendar.ZONE_OFFSET) / 3600000;
        AstroDateTime astroDateTime = new AstroDateTime(year, month, day, hour, minute, second, timezoneOffset, true);
        AstroCalculator.Location location = new AstroCalculator.Location(Double.parseDouble(latitude),
                Double.parseDouble(longitude));
        astroCalculator = new AstroCalculator(astroDateTime, location);
    }

    public AstroCalculator getAstroCalculator() {
        return astroCalculator;
    }
}
