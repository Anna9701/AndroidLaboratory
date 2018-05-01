package com.example.anwyr1.astronomicweatherapp.Weather.CityUtil;

import com.example.anwyr1.astronomicweatherapp.DateParser;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Sun implements Serializable {
    private String rise;
    private String set;

    public Sun(String rise, String set) {
        this.rise = rise;
        this.set = set;
    }

    public String getSet() {
        return DateParser.changeTimezoneToCurrent(set);
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getRise() {
        return DateParser.changeTimezoneToCurrent(rise);
    }

    public void setRise(String rise) {
        this.rise = rise;
    }
}
