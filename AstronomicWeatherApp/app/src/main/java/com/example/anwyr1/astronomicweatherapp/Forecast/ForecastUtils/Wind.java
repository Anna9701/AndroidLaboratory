package com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class Wind implements Serializable {
    private String directionDegrees;
    private String direction;
    private String windSpeedMps;
    private String speedName;

    public Wind(String directionDegrees, String direction, String windSpeedMps, String speedName) {
        this.directionDegrees = directionDegrees;
        this.direction = direction;
        this.windSpeedMps = windSpeedMps;
        this.speedName = speedName;
    }

    public String getDirectionDegrees() {
        return directionDegrees;
    }

    public void setDirectionDegrees(String directionDegrees) {
        this.directionDegrees = directionDegrees;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getWindSpeedMps() {
        return windSpeedMps;
    }

    public void setWindSpeedMps(String windSpeedMps) {
        this.windSpeedMps = windSpeedMps;
    }

    public String getSpeedName() {
        return speedName;
    }

    public void setSpeedName(String speedName) {
        this.speedName = speedName;
    }
}
