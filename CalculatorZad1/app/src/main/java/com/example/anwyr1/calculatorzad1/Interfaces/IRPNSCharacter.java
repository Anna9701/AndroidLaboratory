package com.example.anwyr1.calculatorzad1.Interfaces;

import com.example.anwyr1.calculatorzad1.Enumerations.Action;

/**
 * Created by anwyr1 on 13/03/2018.
 */

public interface IRPNSCharacter {
    boolean isNumber();
    double getNumber();
    Action getOperator();
}
