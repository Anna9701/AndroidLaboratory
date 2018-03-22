package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.IReversePolishNotationConverter;
import com.example.anwyr1.calculatorzad1.Interfaces.Priority;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by anwyr1 on 22/03/2018.
 */



public class ReversePolishNotationConverter implements IReversePolishNotationConverter {
    @Override
    public Queue<RPNSCharacter> convertToReversePolishNotationSequence(String input) {
        Queue<RPNSCharacter> sequence = new LinkedList<>();
        Stack<Operator> stack = new Stack<>();
        while(input.length() > 0) {
            if (Character.isLetter(input.charAt(0))) { //handle sqrt, log, ln, etc. and convert to it's value
                input = handleFunction(input);
            }
            if (checkIfOperator(input.charAt(0))) { // handle operator input
                Operator operator = new Operator(input.charAt(0));
                if (stack.empty() || operator.getPriority().ordinal() > stack.peek().getPriority().ordinal()) {
                    stack.push(operator);
                } else {
                    Operator operatorFromStack = stack.peek();
                    while (operatorFromStack.getPriority().ordinal() >= operator.getPriority().ordinal()) {
                        operatorFromStack = stack.pop();
                        sequence.add(new RPNSCharacter(operatorFromStack.getActionSymbol()));
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
                if (checkIfEndNumber(input)) {
                    number = Double.parseDouble(input);
                    input = "";
                } else {
                    while (!checkIfOperator(input.charAt(++i))) ;
                    number = Double.parseDouble(input.substring(0, i));
                    input = input.substring(i, input.length());
                }
                sequence.add(new RPNSCharacter(number));
            }
        }

        while (!stack.empty()) {
            sequence.add(new RPNSCharacter(stack.pop().getActionSymbol()));
        }

        return sequence;
    }

    private boolean checkIfEndNumber (String input) {
        for (int i = 0; i < input.length(); ++i) {
            if (!Character.isDigit(input.charAt(i)))
                return false;
        }
        return true;
    }

    private String handleFunction(String input) {
        int i = 0;
        while (!(checkIfOperator(input.charAt(++i)))) ;
        String number = input.substring(0, i);
        number = countValue(number);
        input = number + input.substring(i, input.length());
        return input;
    }

    private String countValue(String number) {
        int i = 0;
        while (!(Character.isDigit(++i)));
        String function = number.substring(0, i);
        String functionValue = number.substring(i, number.length());
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

    private boolean checkIfOperator(char c) {
        switch (c) {
            case '+':
            case '-':
            case '*':
            case '^':
            case '/':
                return true;
        }

        return false;
    }
}
