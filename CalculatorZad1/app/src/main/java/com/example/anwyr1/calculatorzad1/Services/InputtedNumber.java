package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.ICNumber;

import java.util.UnknownFormatConversionException;

/**
 * Created by anwyr1 on 13/03/2018.
 */

public class InputtedNumber implements ICNumber {
    private double value;
    private boolean isMinus = false;
    private boolean isPercent = false;
    final static int PERCENT_DIVIDER = 100;

    public InputtedNumber (String input) {
        if (input.contains("%")) {
            isPercent = true;
            value = Double.parseDouble(input.substring(0, input.length() - 1));
        } else {
            value = Double.parseDouble(input);
        }
    }

    @Override
    public double getValue() {
        return value;
    }
/*TODO
    in case of  changing sign of number please implement some characters pattern (e.g. rounds brackets),
    change them (if there are pattern exist (e.g. negation of negated value), remove pattern.
    Process that pattern in getter and setter of that class?
    Maybe static method returning valid pattern due to current state (check it in some method?)


*/
    @Override
    public boolean isMinus() {
        return isMinus;
    }

    @Override
    public void setMinus(boolean minus) {
        isMinus = minus;
    }

    @Override
    public boolean isPercent() {
        return isPercent;
    }

    static public double countResult(final ICNumber first, final ICNumber second, final char action) {
        double firstValue = first.getValue();
        double secondValue = second.getValue();
        if (first.isPercent()) {
            firstValue = first.getValue() / PERCENT_DIVIDER;
        }
        if (second.isPercent()) {
            secondValue = firstValue * secondValue / PERCENT_DIVIDER;
        }
        if (first.isMinus())
            firstValue = -(firstValue);
        if (second.isMinus())
            secondValue = -(secondValue);
        return count(firstValue, secondValue, action);
    }

    /*
    TODO
    please consider input like: 6 / 3 + 2
    It is necessary to count first numbers first and then make addition of third value
    Consider also the order of mathematics operations
     */


    static private double count(final double first, final double second, final char action) {
        switch (action) {
            case '+':
                return first + second;
            case '-':
                return first - second;
            case '*':
                return first * second;
            case '/':
                return first / second;
        }
        throw new UnknownFormatConversionException("Wrong operator " + action);
    }
}
