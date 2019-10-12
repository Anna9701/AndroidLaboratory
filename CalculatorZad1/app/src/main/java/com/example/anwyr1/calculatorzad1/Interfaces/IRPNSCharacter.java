package com.example.anwyr1.calculatorzad1.Interfaces;

import com.example.anwyr1.calculatorzad1.Enumerations.Action;

public interface IRPNSCharacter {
    boolean isNumber();
    double getNumber();
    Action getOperator();
}
