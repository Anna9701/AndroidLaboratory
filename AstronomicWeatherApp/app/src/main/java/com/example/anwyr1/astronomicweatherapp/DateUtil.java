package com.example.anwyr1.astronomicweatherapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by anwyr1 on 01/05/2018.
 */

public class DateUtil {
    public static String changeTimezoneToCurrent(final String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMAN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-0"));
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            return dateString;
        }
        dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.GERMAN);
        return dateFormat.format(date);
    }

    public static String convertAstronomicData(final String dateString) {
        final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            return dateString;
        }
        return dateFormat.format(date);
    }

    public static String getCurrentDate() {
        final DateFormat dateFormatOut = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN);
        final Date currentTime = Calendar.getInstance().getTime();
        return dateFormatOut.format(currentTime);
    }
}