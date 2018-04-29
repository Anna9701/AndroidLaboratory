package com.example.anwyr1.astronomicweatherapp.Weather;

import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.City;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Clouds;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Humidity;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.LastUpdate;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Precipitation;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Pressure;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Temperature;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Visibility;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Weather;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Wind;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */


public class CurrentWeather implements Serializable{
    private City city;
    private Temperature temperature;
    private Humidity humidity;
    private Pressure pressure;
    private Wind wind;
    private Clouds clouds;
    private Visibility visibility;
    private Precipitation precipitation;
    private Weather weather;
    private LastUpdate lastUpdate;

    public CurrentWeather(City city, Temperature temperature, Humidity humidity, Pressure pressure,
                          Wind wind, Clouds clouds, Visibility visibility,
                          Precipitation precipitation, Weather weather, LastUpdate lastUpdate) {
        this.city = city;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.wind = wind;
        this.clouds = clouds;
        this.visibility = visibility;
        this.precipitation = precipitation;
        this.weather = weather;
        this.lastUpdate = lastUpdate;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public LastUpdate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LastUpdate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
