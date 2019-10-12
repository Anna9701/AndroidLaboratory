package com.example.anwyr1.calculatorzad1.Enumerations;

import static com.example.anwyr1.calculatorzad1.Services.MathematicalNamesUtils.*;

public enum Action {
    Addition,
    Subtraction,
    Multiplication,
    Division,
    Power,
    Percentage;

    public static Action convertToAction(char operatorCharacter) {
        switch (operatorCharacter) {
            case PLUS_CHARACTER:
                return Addition;
            case MINUS_CHARACTER:
                return Subtraction;
            case DIVISION_OPERATOR:
                return Division;
            case POWER_OPERATOR:
                return Power;
            case MULTIPLICATION_OPERATOR:
                return Multiplication;
            case PERCENT_CHARACTER:
                return Percentage;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case Addition:
                return String.valueOf(PLUS_CHARACTER);
            case Subtraction:
                return String.valueOf(MINUS_CHARACTER);
            case Division:
                return String.valueOf(DIVISION_OPERATOR);
            case Power:
                return String.valueOf(POWER_OPERATOR);
            case Multiplication:
                return String.valueOf(MULTIPLICATION_OPERATOR);
            case Percentage:
                return String.valueOf(PERCENT_CHARACTER);
            default:
                return null;
        }
    }

    public boolean isEquals (char character) {
        Action temp = convertToAction(character);
        return this.equals(temp);
    }

}
