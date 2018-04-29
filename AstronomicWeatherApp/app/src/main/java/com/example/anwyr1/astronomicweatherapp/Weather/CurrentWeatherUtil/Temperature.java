package com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Temperature implements Serializable {
    private String value;
    private String min;
    private String max;
    private String unit;


    public Temperature(String value, String min, String max, String unit) {
        this.value = value;
        this.min = min;
        this.max = max;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
