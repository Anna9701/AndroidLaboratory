package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.ICOperator;
import com.example.anwyr1.calculatorzad1.Interfaces.IRPNSCharacter;
import com.example.anwyr1.calculatorzad1.Interfaces.IReversePolishNotationConverter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by anwyr1 on 22/03/2018.
 */



public class ReversePolishNotationConverter implements IReversePolishNotationConverter {
    private Queue<IRPNSCharacter> sequence;
    private Stack<Operator> operatorsStack;
    private String input;
    private boolean anyNumberConverted;
    private static final double PERCENT_VALUE;
    private static final ICOperator PERCENT;

    static
    {
        PERCENT_VALUE = 0.01;
        PERCENT = new Operator('%');
    }

    public ReversePolishNotationConverter(String input) {
        sequence = new LinkedList<>();
        operatorsStack = new Stack<>();
        this.input = input;
        anyNumberConverted = false;
    }

    @Override
    public void convertToReversePolishNotationSequence() {
        while(input.length() > 0) {
            char inputtedCharacter = input.charAt(0);
            if (Character.isLetter(inputtedCharacter)) {
                handleMathematicalFunction();
            }
            if (inputtedCharacter == PERCENT.getActionSymbol()) {
                handlePercent();
            } else if (isOperator(inputtedCharacter) && anyNumberConverted) {
                handleOperator();
            } else {
                handleNumber();
            }
            anyNumberConverted = true;
        }

        while (!operatorsStack.empty()) {
            sequence.add(new RPNSCharacter(operatorsStack.pop().getActionSymbol()));
        }
    }

    @Override
    public Queue<IRPNSCharacter> getRPNSSequence() {
        return sequence;
    }

    private void handleNumber() {
        int i = 0;
        double number;
        if(input.startsWith("(")) {  //handle negative number
            number = Double.parseDouble(input.substring(1, input.indexOf(")")));
            input = input.substring(input.indexOf(")") + 1, input.length());
            sequence.add(new RPNSCharacter(number));
            return;
        }
        if (isEndNumber(input)) { // handle last number of action
            number = Double.parseDouble(input);
            input = "";
        } else {
            while (!isOperator(input.charAt(++i))) ;
            String tmpString = input.substring(0, i);
            number = Double.parseDouble(tmpString);
            input = input.substring(i, input.length());
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
                sequence.add(new RPNSCharacter(operatorFromStack.getActionSymbol()));
                if (operatorsStack.size() == 0)
                    break;
                operatorFromStack = operatorsStack.peek();
            }
            operatorsStack.push(operator);
        }
    }

    private void handlePercent() {
        double percentsNumber = sequence.poll().getNumber();
        percentsNumber *= PERCENT_VALUE;// * sequence.peek().getNumber();
        if (sequence.size() > 0) {
            if (this.operatorsStack.size() <= 0 || (operatorsStack.peek().getActionSymbol() != '*' &&
                    operatorsStack.peek().getActionSymbol() != '/')) {
                percentsNumber *= sequence.peek().getNumber();
            }
        } else {
            percentsNumber *= 1;
        }
        sequence.add(new RPNSCharacter(percentsNumber));
        takeFirstCharFromInput();
    }

    private char takeFirstCharFromInput() {
        final int firstCharacterIndex = 0;
        final char firstCharacter = input.charAt(0);
        input = input.substring(firstCharacterIndex + 1, input.length());
        return firstCharacter;
    }

    private boolean isEndNumber(String input) {
        for (int i = 1; i < input.length(); ++i) {
            if (isOperator(input.charAt(i)))
                if (!(input.charAt(i - 1) == '('))
                return false;
        }
        return true;
    }

    private void handleMathematicalFunction() {
        int i;
        if (!isEndNumber(input)) {
            for(i = 0; i < input.length(); ++i) {
                if (isOperator(input.charAt(i)))
                    if (!(input.charAt(i - 1) == '('))
                        break;
            }
            String number = input.substring(0, i);
            number = prepareInputToCountMathematicalFunctionValue(number);
            input = number + input.substring(i, input.length());
        } else {
            input = prepareInputToCountMathematicalFunctionValue(input);
        }
    }

    private String prepareInputToCountMathematicalFunctionValue(String number) {
        int i = 1;
        int j = 0;
        while (i < number.length() && !(Character.isDigit(number.charAt(i++))));
        String function = number.substring(0, --i);
        String functionValue = "";
        if (function.endsWith("(-")) {
            functionValue += '-';
            function = function.substring(0, function.indexOf("(-"));
            ++j;
        }
        functionValue += number.substring(i, number.length() - j);
        return countMathematicalFunctionValue(function, functionValue);
    }

    private String countMathematicalFunctionValue(String function, String functionValue) {
        double value = Double.parseDouble(functionValue);
        switch (function) {
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
        return String.valueOf(value);
    }

    private boolean isOperator(char character) {
        switch (character) {
            case '+':
            case '*':
            case '^':
            case '/':
            case '-':
            case '%':
                return true;
        }

        return false;
    }
}
