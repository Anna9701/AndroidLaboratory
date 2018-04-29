package com.example.anwyr1.astronomicweatherapp.Forecast;

import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Sun;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class ForecastData implements Serializable {
    private Location location;
    private Sun sun;
    private List<ThreeHoursForecast> forecastList;

    public ForecastData(Location location, Sun sun, List<ThreeHoursForecast> forecastList) {
        this.location = location;
        this.sun = sun;
        this.forecastList = forecastList;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Sun getSun() {
        return sun;
    }

    public void setSun(Sun sun) {
        this.sun = sun;
    }

    public List<ThreeHoursForecast> getForecastList() {
        return forecastList;
    }

    public void setForecastList(List<ThreeHoursForecast> forecastList) {
        this.forecastList = forecastList;
    }
}
