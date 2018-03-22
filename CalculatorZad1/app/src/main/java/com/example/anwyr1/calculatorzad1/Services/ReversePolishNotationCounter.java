package com.example.anwyr1.calculatorzad1.Services;

import java.util.Queue;
import java.util.Stack;

/**
 * Created by anwyr1 on 22/03/2018.
 */

public class ReversePolishNotationCounter {

    public double countResult(Queue<RPNSCharacter> rpnsCharacters) {
        Stack<RPNSCharacter> stack = new Stack<>();
        for (RPNSCharacter symbol : rpnsCharacters) {
            if (symbol.isNumber()) {
                stack.push(symbol);
            } else {
                double a = stack.pop().getNumber();
                double b = stack.pop().getNumber();
                Double value = countValue (a, b, symbol.getOperator());
                stack.push(new RPNSCharacter(value));
            }
        }

        return stack.pop().getNumber();
    }

    private Double countValue(double a, double b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                return a / b;
            case '^':
                return Math.pow(a, b);
        }

        return 0.0;
    }
}
