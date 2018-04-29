package com.example.anwyr1.astronomicweatherapp.Weather.CityUtil;

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
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }
}
