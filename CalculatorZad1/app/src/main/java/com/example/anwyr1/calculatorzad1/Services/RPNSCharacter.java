package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Enumerations.Action;
import com.example.anwyr1.calculatorzad1.Interfaces.IRPNSCharacter;

/**
 * Created by anwyr1 on 22/03/2018.
 */

public class RPNSCharacter implements IRPNSCharacter {
    private double number;
    private Action operator;
    private boolean isNumber = false;

    RPNSCharacter(double value) {
        this.number = value;
        isNumber = true;
    }

    RPNSCharacter(Action operator) {
        this.operator = operator;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public double getNumber() {
        return number;
    }

    public Action getOperator() {
        return operator;
    }
}
