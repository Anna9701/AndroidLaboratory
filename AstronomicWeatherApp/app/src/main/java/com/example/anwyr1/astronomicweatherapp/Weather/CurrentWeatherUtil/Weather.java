package com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */
public class Weather implements Serializable {
    private String number;
    private String value;
    private String icon;

    public Weather(String number, String value, String icon) {
        this.number = number;
        this.value = value;
        this.icon = icon;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
