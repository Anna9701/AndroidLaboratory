package com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Humidity implements Serializable {
    private String value;
    private String unit;

    public Humidity(String value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
