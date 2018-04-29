package com.example.anwyr1.astronomicweatherapp.Weather.CityUtil;


import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Country implements Serializable {
    private String countryCode;

    public Country(String code) {
        countryCode = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
