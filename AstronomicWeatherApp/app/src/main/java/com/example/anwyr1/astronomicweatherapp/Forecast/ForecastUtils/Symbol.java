package com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils;

import java.io.Serializable;

/**
 * Created by anwyr1 on 18/05/2018.
 */

public class Symbol implements Serializable {
    private String number;
    private String name;
    private String var;

    public Symbol(String number, String name, String var) {
        this.number = number;
        this.name = name;
        this.var = var;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
