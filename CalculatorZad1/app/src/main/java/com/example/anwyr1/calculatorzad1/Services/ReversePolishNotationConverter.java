package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.IReversePolishNotationConverter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by anwyr1 on 22/03/2018.
 */



public class ReversePolishNotationConverter implements IReversePolishNotationConverter {
    @Override
    public Queue<RPNSCharacter> convertToReversePolishNotationSequence(String input) {
        Queue<RPNSCharacter> sequence = new LinkedList<>();
        Stack<Operator> stack = new Stack<>();
        boolean fistNumber = true;
        while(input.length() > 0) {
            if (Character.isLetter(input.charAt(0))) { //handle sqrt, log, ln, etc. and convert to it's value
                input = handleFunction(input);
            }
            if (isOperator(input.charAt(0)) && !fistNumber) { // handle operator input
                Operator operator = new Operator(input.charAt(0));
                if (stack.empty() || operator.getPriority().ordinal() > stack.peek().getPriority().ordinal()) {
                    stack.push(operator);
                } else {
                    Operator operatorFromStack = stack.peek();
                    while (operatorFromStack.getPriority().ordinal() >= operator.getPriority().ordinal()) {
                        operatorFromStack = stack.pop();
                        sequence.add(new RPNSCharacter(operatorFromStack.getActionSymbol()));
                        if (stack.size() == 0)
                            break;
                        operatorFromStack = stack.peek();
                    }
                    stack.push(operator);
                }
                input = input.substring(1, input.length());
            } else { // handle number
                int i = 0;
                Double number;

                if(input.startsWith("(")) {
                    number = Double.parseDouble(input.substring(1, input.indexOf(")")));
                    input = input.substring(input.indexOf(")") + 1, input.length());
                    sequence.add(new RPNSCharacter(number));
                    continue;
                }
                if (isEndNumber(input)) {
                    if (input.endsWith("%"))
                        number = Double.parseDouble(input.substring(0, input.length() - 1)) / 100;
                    else
                        number = Double.parseDouble(input);
                    input = "";
                } else {
                    while (!isOperator(input.charAt(++i))) ;
                    String tmpString = input.substring(0, i);
                    if (tmpString.endsWith("%"))
                        number = Double.parseDouble(tmpString.substring(0, tmpString.length() - 1)) / 100;
                    else
                        number = Double.parseDouble(tmpString);
                    input = input.substring(i, input.length());
                }
                sequence.add(new RPNSCharacter(number));
            }
            fistNumber = false;
        }

        while (!stack.empty()) {
            sequence.add(new RPNSCharacter(stack.pop().getActionSymbol()));
        }

        return sequence;
    }

    private boolean isEndNumber(String input) {
        for (int i = 1; i < input.length(); ++i) {
            if (isOperator(input.charAt(i)))
                if (!(input.charAt(i - 1) == '('))
                return false;
        }
        return true;
    }

    private String handleFunction(String input) {
        int i = 0;
        if (!isEndNumber(input)) {
            for(i = 0; i < input.length(); ++i) {
                if (isOperator(input.charAt(i)))
                    if (!(input.charAt(i - 1) == '('))
                        break;
            }
            String number = input.substring(0, i);
            number = countValue(number);
            input = number + input.substring(i, input.length());
        } else {
            input = countValue(input);
        }
        return input;
    }

    private String countValue(String number) {
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
        return countFunction(function, functionValue);
    }

    private String countFunction(String function, String functionValue) {
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

    private boolean isOperator(char c) {
        switch (c) {
            case '+':
            case '*':
            case '^':
            case '/':
            case '-':
                return true;
        }

        return false;
    }
}
