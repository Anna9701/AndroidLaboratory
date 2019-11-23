package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.ICOperator;
import com.example.anwyr1.calculatorzad1.Interfaces.IRPNSCharacter;
import com.example.anwyr1.calculatorzad1.Interfaces.IReversePolishNotationConverter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static com.example.anwyr1.calculatorzad1.Services.MathematicalNamesUtils.*;

public class ReversePolishNotationConverter implements IReversePolishNotationConverter {
    private Queue<IRPNSCharacter> sequence;
    private Stack<Operator> operatorsStack;
    private String input;
    private boolean anyNumberConverted;
    private static final double PERCENT_VALUE;
    private static final ICOperator PERCENT;
    private static final int PERCENT_BASE_NUMBER;
    private static final String EMPTY_STRING = "";

    static
    {
        PERCENT_VALUE = 0.01;
        PERCENT_BASE_NUMBER = 1;
        PERCENT = new Operator(MathematicalNamesUtils.PERCENT_CHARACTER);
    }

    ReversePolishNotationConverter() {
        sequence = new LinkedList<>();
        operatorsStack = new Stack<>();
        this.input = EMPTY_STRING;
        anyNumberConverted = false;
    }

    void setInput(final String input) {
        this.input = input;
        sequence = new LinkedList<>();
        operatorsStack = new Stack<>();
    }

    @Override
    public void convertToReversePolishNotationSequence() {
        while(input.length() > 0) {
            char inputtedCharacter = input.charAt(0);
            if (Character.isLetter(inputtedCharacter)) {
                handleMathematicalFunction();
            }
            if (PERCENT.getAction().isEquals(inputtedCharacter)) {
                handlePercent();
            } else if (isOperator(inputtedCharacter) && anyNumberConverted) {
                handleOperator();
            } else {
                handleNumber();
            }
            anyNumberConverted = true;
        }

        while (!operatorsStack.empty()) {
            sequence.add(new RPNSCharacter(operatorsStack.pop().getAction()));
        }
    }

    @Override
    public Queue<IRPNSCharacter> getRPNSSequence() {
        return sequence;
    }

    private void handleNumber() {
        int i = 0;
        double number;
        if(input.startsWith(String.valueOf(OPENING_BRACKET))) {  //handle negative number
            number = Double.parseDouble(input.substring(1, input.indexOf(CLOSING_BRACKET)));
            input = input.substring(input.indexOf(CLOSING_BRACKET) + 1);
            sequence.add(new RPNSCharacter(number));
            return;
        }
        if (isEndNumber(input)) { // handle last number of action
            number = Double.parseDouble(input);
            input = EMPTY_STRING;
        } else {
            while (++i < input.length() && !isOperator(i)) ;
            String tmpString = input.substring(0, i);
            number = Double.parseDouble(tmpString);
            input = input.substring(i);
        }
        sequence.add(new RPNSCharacter(number));
    }

    private void handleOperator() {
        Operator operator = new Operator(takeFirstCharFromInput());
        if (operatorsStack.empty() || operator.getPriority().ordinal() > operatorsStack.peek().getPriority().ordinal()) {
            operatorsStack.push(operator);
        } else {
            Operator operatorFromStack = operatorsStack.peek();
            while (operatorFromStack.getPriority().ordinal() >= operator.getPriority().ordinal()) {
                operatorFromStack = operatorsStack.pop();
                sequence.add(new RPNSCharacter(operatorFromStack.getAction()));
                if (operatorsStack.size() == 0)
                    break;
                operatorFromStack = operatorsStack.peek();
            }
            operatorsStack.push(operator);
        }
    }

    private void handlePercent() {
        double percentsNumber = sequence.poll().getNumber();
        percentsNumber *= PERCENT_VALUE;
        if (sequence.size() > 0) {
            if (this.operatorsStack.size() <= 0 ||
                    (!operatorsStack.peek().getAction().isEquals(POWER_OPERATOR)) &&
                    !operatorsStack.peek().getAction().isEquals(DIVISION_OPERATOR)) {
                percentsNumber *= sequence.peek().getNumber();
            }
        } else {
            percentsNumber *= PERCENT_BASE_NUMBER;
        }
        sequence.add(new RPNSCharacter(percentsNumber));
        takeFirstCharFromInput();
    }

    private char takeFirstCharFromInput() {
        final int firstCharacterIndex = 0;
        final char firstCharacter = input.charAt(0);
        input = input.substring(firstCharacterIndex + 1);
        return firstCharacter;
    }

    private boolean isEndNumber(String input) {
        for (int i = 1; i < input.length(); ++i) {
            if (isOperator(input.charAt(i)))
                if (!(input.charAt(i - 1) == OPENING_BRACKET))
                    return false;
        }
        return true;
    }

    private void handleMathematicalFunction() {
        int i;
        if (!isEndNumber(input)) {
            for(i = 0; i < input.length(); ++i) {
                if (isOperator(input.charAt(i)))
                    if (!(input.charAt(i - 1) == OPENING_BRACKET))
                        break;
            }
            String number = input.substring(0, i);
            number = prepareInputToCountMathematicalFunctionValue(number);
            input = number + input.substring(i);
        } else {
            input = prepareInputToCountMathematicalFunctionValue(input);
        }
    }

    private String prepareInputToCountMathematicalFunctionValue(String number) {
        int i = 1;
        int j = 0;
        while (i < number.length() && !(Character.isDigit(number.charAt(i++))));
        String function = number.substring(0, --i);
        String functionValue = EMPTY_STRING;
        if (function.endsWith(String.valueOf(OPENING_BRACKET) + MINUS_CHARACTER)) {
            functionValue += MINUS_CHARACTER;
            function = function.substring(0, function.indexOf(OPENING_BRACKET + String.valueOf(MINUS_CHARACTER)));
            ++j;
        }
        functionValue += number.substring(i, number.length() - j);
        return countMathematicalFunctionValue(function, functionValue);
    }

    private String countMathematicalFunctionValue(String function, String functionValue) {
        double value = Double.parseDouble(functionValue);
        switch (function) {
            case SQUARE_ROOT_NAME:
                value = Math.sqrt(value);
                break;
            case LOGARITHM_NAME:
                value = Math.log10(value);
                break;
            case SINUS_NAME:
                value = Math.sin(Math.toRadians(value));
                break;
            case COSINES_NAME:
                value = Math.cos(Math.toRadians(value));
                break;
            case NATURAL_LOGARITHM_NAME:
                value = Math.log(value);
                break;
            case TANGENT_NAME:
                value = Math.tan(Math.toRadians(value));
        }
        return String.valueOf(value);
    }

    private boolean isOperator(char character) {
        return Operator.isOperator(character);
    }

    private boolean isOperator(int index) {
        return Operator.isOperator(input.charAt(index)) && !(input.charAt(index - 1) == 'E');
    }
}
