package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.Action;
import com.example.anwyr1.calculatorzad1.Interfaces.ICNumber;
import com.example.anwyr1.calculatorzad1.Interfaces.ICOperator;
import com.example.anwyr1.calculatorzad1.Interfaces.Priority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by anwyr1 on 21/03/2018.
 */

public class InputtedOperator implements ICOperator {
    private ICNumber firstOperand;
    private ICNumber secondOperand;
    private Action action;
    private Priority priority;

    public InputtedOperator() {
        firstOperand = new InputtedNumber();
        secondOperand = new InputtedNumber();
        action = Action.Addition;
    }

    @Override
    public ICNumber getFirstOperand() {
        return firstOperand;
    }

    @Override
    public ICNumber getSecondOperand() {
        return secondOperand;
    }

    @Override
    public Action getActionToBeDone() {
        return action;
    }

    @Override
    public void setFirstOperand(ICNumber operand) {
        firstOperand = operand;
    }

    @Override
    public void setSecondOperand(ICNumber operand) {
        secondOperand = operand;
    }

    public ICNumber makeActionAndGetResultNumber() {
        firstOperand.setValue(InputtedNumber.countResult(firstOperand, secondOperand, action));
        return firstOperand;
    }

    @Override
    public void setActionToBeDone(char action) throws UnknownPriorityException {
        switch (action) {
            case '+':
                this.action = Action.Addition;
                break;
            case '-':
                this.action = Action.Subtraction;
                break;
            case '*':
                this.action = Action.Multiplication;
                break;
            case '/':
                this.action = Action.Division;
                break;
            case '^':
                this.action = Action.Power;
                break;
        }
        setPriority();
    }

    @Override
    public Priority getOperatorPriority() {
        return priority;
    }

    private void setPriority() throws UnknownPriorityException {
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
            default:
                System.err.print("Unknown priority of operator: " + action);
                throw new UnknownPriorityException();
        }
    }
}
