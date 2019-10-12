package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Enumerations.Action;
import com.example.anwyr1.calculatorzad1.Interfaces.IRPNSCharacter;

import java.util.Queue;
import java.util.Stack;

class ReversePolishNotationCounter {
    static Action lastOperator;
    static double lastNumber;

    double countResult(Queue<IRPNSCharacter> rpnsCharacters) {
        Stack<IRPNSCharacter> stack = new Stack<>();

        for (IRPNSCharacter symbol : rpnsCharacters) {
            if (symbol.isNumber()) {
                stack.push(symbol);
            } else {
                double a = stack.pop().getNumber();
                double b = stack.pop().getNumber();
                double value = countValue (a, b, symbol.getOperator());
                lastOperator = symbol.getOperator();
                lastNumber = a;
                stack.push(new RPNSCharacter(value));
            }
        }

        double resultNumber = stack.pop().getNumber();
        return (double) Math.round(resultNumber * 100d) / 100d;
    }

    private double countValue(double a, double b, Action operator) {
        switch (operator) {
            case Addition:
                return a + b;
            case Subtraction:
                return b - a;
            case Multiplication:
                return a * b;
            case Division:
                return b / a;
            case Power:
                return Math.pow(b, a);
        }

        return 0.0;
    }
}
