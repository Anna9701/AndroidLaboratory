package com.example.anwyr1.calculatorzad1.Interfaces;

/**
 * Created by anwyr1 on 13/03/2018.
 */

public interface IRPNSCharacter {
    boolean isNumber();
    double getNumber();
    boolean isOperator();
    Action getOperator();
}
