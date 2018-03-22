package com.example.anwyr1.calculatorzad1.Services;

/**
 * Created by anwyr1 on 22/03/2018.
 */

public class RPNSCharacter {
    private Double number;
    private char operator;
    private boolean isOperator = false;
    private boolean isNumber = false;

    public RPNSCharacter(Double value) {
        this.number = value;
        isNumber = true;
    }

    public RPNSCharacter(char operator) {
        this.operator = operator;
        isOperator = true;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public Double getNumber() {
        return number;
    }

    public boolean isOperator() {
        return isOperator;
    }

    public char getOperator() {
        return operator;
    }
}
