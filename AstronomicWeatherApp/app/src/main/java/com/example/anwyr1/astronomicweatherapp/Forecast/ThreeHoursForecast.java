package com.example.anwyr1.astronomicweatherapp.Forecast;

import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Clouds;
import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Precipitation;
import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Symbol;
import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Time;
import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Wind;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Humidity;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Pressure;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Temperature;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class ThreeHoursForecast implements Serializable{
    private Time time;
    private Symbol symbol;
    private Wind wind;
    private Temperature temperature;
    private Pressure pressure;
    private Humidity humidity;
    private Clouds clouds;
    private Precipitation precipitation;
    private String weatherCondition;

    public ThreeHoursForecast(Time time, Symbol symbol, Wind wind, Temperature temperature, Pressure pressure, Humidity humidity, Clouds clouds, Precipitation precipitation, String weatherCondition) {
        this.time = time;
        this.symbol = symbol;
        this.wind = wind;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        this.precipitation = precipitation;
        this.weatherCondition = weatherCondition;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
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

    public Humidity getHumidity() {
        return humidity;
    }

    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
}
