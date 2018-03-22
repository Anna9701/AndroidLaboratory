package com.example.anwyr1.calculatorzad1.Interfaces;

import com.example.anwyr1.calculatorzad1.Services.InputtedOperator;
import com.example.anwyr1.calculatorzad1.Services.UnknownPriorityException;

/**
 * Created by anwyr1 on 21/03/2018.
 */

public interface ICOperator {
    ICNumber getFirstOperand();
    ICNumber getSecondOperand();
    Action getActionToBeDone();
    void setFirstOperand(ICNumber operand);
    void setSecondOperand(ICNumber operand);
    void setActionToBeDone(char action) throws UnknownPriorityException;
    Priority getOperatorPriority();
    ICNumber makeActionAndGetResultNumber();
}
