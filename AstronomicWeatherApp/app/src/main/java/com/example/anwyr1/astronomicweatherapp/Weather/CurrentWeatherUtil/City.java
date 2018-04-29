package com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil;

import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Coord;
import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Country;
import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Sun;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class City implements Serializable {
    private String id;
    private String name;
    private Coord coord;
    private Country country;
    private Sun sun;

    public City(String id, String name, Coord coord, Country country, Sun sun) {
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
        this.sun = sun;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Sun getSun() {
        return sun;
    }

    public void setSun(Sun sun) {
        this.sun = sun;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
