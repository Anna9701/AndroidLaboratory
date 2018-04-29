package com.example.anwyr1.astronomicweatherapp.Forecast;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Location implements Serializable {
    private String name;
    private String country;
    private String latitude;
    private String longitude;

    public Location(String name, String country, String latitude, String longitude) {
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
