package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.Action;
import com.example.anwyr1.calculatorzad1.Interfaces.ICOperator;
import com.example.anwyr1.calculatorzad1.Interfaces.Priority;

/**
 * Created by anwyr1 on 22/03/2018.
 */

public class Operator implements ICOperator {
    private Action action;
    private Priority priority;

    public Operator (char c) {
        action = Action.convertToAction(c);
        setPriority();
    }

    private void setPriority()  {
        switch (action) {
            case Addition:
            case Subtraction:
                priority = Priority.LOW;
                break;
            case Multiplication:
            case Division:
                priority = Priority.NORMAL;
                break;
            case Power:
                priority = Priority.HIGH;
                break;
            case Percentage:
                priority = Priority.VERY_HIGH;
        }
    }

    public Priority getPriority() {
        return priority;
    }

    public Action getAction() {
        return action;
    }
}
