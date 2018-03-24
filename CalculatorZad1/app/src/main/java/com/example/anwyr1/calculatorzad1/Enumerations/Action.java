package com.example.anwyr1.calculatorzad1.Enumerations;

/**
 * Created by anwyr1 on 21/03/2018.
 */
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

    public boolean isEquals (char character) {
        Action temp = convertToAction(character);
        return this.equals(temp);
    }

}
