package com.example.anwyr1.astronomicweatherapp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by anwyr1 on 01/05/2018.
 */

public class DateParser {
    public static String changeTimezoneToCurrent(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMAN);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-2"));
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            return dateString;
        }
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return dateFormat.format(date);
    }
}
