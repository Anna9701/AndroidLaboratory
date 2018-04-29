package com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil;

import com.example.anwyr1.astronomicweatherapp.Weather.WindUtil.Direction;
import com.example.anwyr1.astronomicweatherapp.Weather.WindUtil.Gusts;
import com.example.anwyr1.astronomicweatherapp.Weather.WindUtil.Speed;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */
public class Wind implements Serializable {
    private Speed speed;
    private Gusts gusts;
    private Direction direction;

    public Wind(Speed speed, Gusts gusts, Direction direction) {
        this.speed = speed;
        this.gusts = gusts;
        this.direction = direction;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public Gusts getGusts() {
        return gusts;
    }

    public void setGusts(Gusts gusts) {
        this.gusts = gusts;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
