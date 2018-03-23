package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.ICOperator;
import com.example.anwyr1.calculatorzad1.Interfaces.Priority;

/**
 * Created by anwyr1 on 22/03/2018.
 */

public class Operator implements ICOperator {
    private char action;
    private Priority priority;

    public Operator (char c) {
        action = c;
        setPriority();
    }

    private void setPriority()  {
        switch (action) {
            case '+':
            case '-':
                priority = Priority.LOW;
                break;
            case '*':
            case '/':
                priority = Priority.NORMAL;
                break;
            case '^':
                priority = Priority.HIGH;
                break;
        }
    }

    public Priority getPriority() {
        return priority;
    }

    public char getActionSymbol() {
        return action;
    }
}
