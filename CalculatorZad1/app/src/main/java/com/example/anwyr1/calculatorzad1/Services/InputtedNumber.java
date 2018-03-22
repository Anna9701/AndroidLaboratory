package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Activities.Main2Activity;
import com.example.anwyr1.calculatorzad1.Interfaces.Action;
import com.example.anwyr1.calculatorzad1.Interfaces.ICNumber;

import java.util.UnknownFormatConversionException;

/**
 * Created by anwyr1 on 13/03/2018.
 */

public class InputtedNumber implements ICNumber {
    private double value;
    private boolean isMinus = false;
    private boolean isPercent = false;
    private boolean hasUnaryOperator = false;
    final static int PERCENT_DIVIDER = 100;

    public InputtedNumber() {
        value = 0;
    }

    public InputtedNumber (String input) {
        if (input.contains("%")) {
            isPercent = true;
            input = input.substring(0, input.length()-1);
        }
        if (input.contains("(-")) {
            String part1 = input.substring(0, input.indexOf("(-"));
            String part2 = input.substring(input.indexOf("(-") + 2, input.indexOf(")"));
            String part3 = input.substring(input.indexOf(")"), input.length()-1);
            isMinus = true;
            input = part1 + part2 + part3;
        }

        if (input.matches("[a-z]+[0-9]+")) {
            setUnaryOperatorValue(input);
        } else
            setNakedValue(input);

    }

    private void setUnaryOperatorValue (String input) {
        String[] inputSplitted = input.split("(?<=\\D)(?=\\d)");
        String operator = inputSplitted[0];
        value = Double.parseDouble(inputSplitted[1]);

        hasUnaryOperator = true;
        switch (operator) {
            case "sqrt":
                value = Math.sqrt(value);
                break;
            case "log":
                value = Math.log10(value);
                break;
            case "sin":
                value = Math.sin(Math.toRadians(value));
                break;
            case "cos":
                value = Math.cos(Math.toRadians(value));
                break;
            case "ln":
                value = Math.log(value);
                break;
            case "tan":
                value = Math.tan(Math.toRadians(value));
        }
        if (isMinus) {
            value = -value;
            isMinus = false;
        }
    }

    @Override
    public boolean hasUnaryOperator() {
        return hasUnaryOperator;
    }

    private void setNakedValue(String input) {
        value = Double.parseDouble(input);
        if (!isMinus)
            isMinus = value < 0;
    }

    @Override
    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public double getValue() {
        if (isMinus)
            value = -value;
        if (isPercent)
            value /= PERCENT_DIVIDER;
        return value;
    }

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

    static public double countResult(final ICNumber first, final ICNumber second, final Action action) {
        double firstValue = first.getValue();
        double secondValue = second.getValue();
        return count(firstValue, secondValue, action);
    }

    static private double count(final double first, final double second, final Action action) {
        switch (action) {
            case Addition:
                return first + second;
            case Subtraction:
                return first - second;
            case Multiplication:
                return first * second;
            case Division:
                return first / second;
            case Power:
                return Math.pow(first, second);
        }
        throw new UnknownFormatConversionException("Wrong operator " + action);
    }
}
