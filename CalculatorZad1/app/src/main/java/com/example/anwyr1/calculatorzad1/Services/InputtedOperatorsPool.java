package com.example.anwyr1.calculatorzad1.Services;

import com.example.anwyr1.calculatorzad1.Interfaces.ICNumber;
import com.example.anwyr1.calculatorzad1.Interfaces.ICOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anwyr1 on 21/03/2018.
 */

public class InputtedOperatorsPool {
    private List<ICOperator> operators;

    public InputtedOperatorsPool() {
        operators = new ArrayList<>();
    }

    public void addOperatorsAndNumbersToPool(String operatorsString, List<ICNumber> numbers) throws UnknownPriorityException {
        char[] operatorsArray = operatorsString.toCharArray();
        InputtedOperator inputtedOperator = new InputtedOperator();
        if (!(operatorsArray.length > 0)) {
            inputtedOperator.setFirstOperand(numbers.get(0));
            operators.add(inputtedOperator);
        }
        for (int i = 0; i < operatorsArray.length; ++i) {
            inputtedOperator.setActionToBeDone(operatorsArray[i]);
            inputtedOperator.setFirstOperand(numbers.get(i));
            inputtedOperator.setSecondOperand(numbers.get(i + 1));
            operators.add(inputtedOperator);
        }
    }

    public ICOperator getOperator (int index) {
        return operators.get(index);
    }

    public double summarize() {
        ICOperator resultOperator = operators.get(0);
        for (int i = 1; i <= operators.size(); ++i) {
            ICNumber number1 = resultOperator.makeActionAndGetResultNumber();
            ICNumber number2 = operators.get(i+1).makeActionAndGetResultNumber();
            resultOperator = new InputtedOperator();
            resultOperator.setFirstOperand(number1);
            resultOperator.setSecondOperand(number2);
        }

        return 0;
    }

}

//para liczb na ktorych dzialamy, oprocz tego kolejka pozostalych