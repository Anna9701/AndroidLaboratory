package com.example.anwyr1.calculatorzad1.Interfaces;

/**
 * Created by anwyr1 on 13/03/2018.
 */

public interface ICNumber {
    double getValue();

    boolean isMinus();

    void setMinus(boolean minus);

    boolean isPercent();

    void setValue(double value);

    boolean hasUnaryOperator();
}
