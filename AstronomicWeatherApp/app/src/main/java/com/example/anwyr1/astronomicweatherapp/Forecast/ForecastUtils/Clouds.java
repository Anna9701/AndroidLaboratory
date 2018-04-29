package com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Clouds implements Serializable {
    private String value;
    private String all;
    private String unit;

    public Clouds(String value, String all, String unit) {
        this.value = value;
        this.all = all;
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
