package com.example.anwyr1.astronomicweatherapp.Weather.CityUtil;


import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Coord implements Serializable {
    private String longitude;
    private String latitude;

    public Coord(String lon, String lat) {
        longitude = lon;
        latitude = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
