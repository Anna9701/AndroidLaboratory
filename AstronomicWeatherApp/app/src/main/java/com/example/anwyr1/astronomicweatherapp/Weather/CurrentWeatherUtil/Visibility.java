package com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */
public class Visibility implements Serializable {
    private String value;

    public Visibility(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
