package com.example.anwyr1.astronomicweatherapp.Weather.WindUtil;

import java.io.Serializable;

/**
 * Created by anwyr1 on 29/04/2018.
 */
public class Direction implements Serializable {
    private String value;
    private String code;
    private String name;

    public Direction(String value, String code, String name) {
        this.value = value;
        this.code = code;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
