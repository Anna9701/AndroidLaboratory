package com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils;

/**
 * Created by anwyr1 on 01/05/2018.
 */

public class Precipitation {
    private String value;
    private String type;

    public Precipitation(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
