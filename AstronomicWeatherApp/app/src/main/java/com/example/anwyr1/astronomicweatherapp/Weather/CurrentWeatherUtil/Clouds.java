package com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Clouds implements Serializable {
    private String value;
    private String name;

    public Clouds(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
