package com.example.anwyr1.calculatorzad1.Interfaces;

import java.util.Comparator;

/**
 * Created by anwyr1 on 21/03/2018.
 */

public enum Action implements Comparable<Action> {
    Addition,
    Subtraction,
    Multiplication,
    Division,
    Power,
    Percentage;

    public static Action convertToAction(char c) {
        switch (c) {
            case '+':
                return Addition;
            case '-':
                return Subtraction;
            case '/':
                return Division;
            case '^':
                return Power;
            case '*':
                return Multiplication;
            case '%':
                return Percentage;
            default:
                return null;
        }
    }

    public boolean isEquals (char operator) {
        Action temp = convertToAction(operator);
        return this.equals(temp);
    }

}
