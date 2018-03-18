package com.example.anwyr1.calculatorzad1.Services;

import android.app.Activity;
import android.view.View;

import com.example.anwyr1.calculatorzad1.Interfaces.ICNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anwyr1 on 13/03/2018.
 */

public class AdvancedCalculator extends BasicCalculator {
    public AdvancedCalculator(View v, Activity activity) {
        super(v, activity);
    }

    public void handleSquarePower() {
        if (!(textView.getText().length() > 0)) {
            showAlert("Error", "Faulty operation requested.");
            return;
        }
        handleOperator("^");
        textView.append("2");
    }

    public void handleUnaryOperator(String operator) {
        if (resultPrinted) {
            clearInput();
            resultPrinted = false;
        }
        String input = textView.getText().toString();
        if (input.length() > 0 && Character.isDigit(input.charAt(input.length() - 1)) &&
                input.charAt(input.length() - 1) != ')') {
            showAlert("Error", "Faulty operation requested.");
            return;
        }
        textView.append(operator);
    }

    @Override
    protected ArrayList<ICNumber> getInputNumbersSplitted() {
        String inputted = textView.getText().toString();
        String splitted[] = {""};
        ArrayList<ICNumber> numbers = new ArrayList<>();
        if (inputted.length() > 0) {
            inputted = inputted.replace("-", " -");
            inputted = inputted.replace("( ", "(");
            splitted = inputted.split("[^(\\-0-9a-z.%)]");
        }
        for (String s : splitted) {
            if (s.length() > 0)
                numbers.add(new InputtedNumber(s));
        }
        return numbers;
    }

    @Override
    public void summarize() {
        if (textView.getText().length() == 0) {
            showAlert("Error", "Faulty operation requested.");
            return;
        }
        final double PERCENT_BASE = 100;
        List<ICNumber> numbers = getInputNumbersSplitted();
        String operators = getInputOperators();
        if (numbers.size() == 0 || operators.length() == 0 && !numbers.get(0).isPercent() && !numbers.get(0).hasUnaryOperator()) {
            showAlert("Error", "Faulty operation requested.");
            return;
        } else if (operators.length() == 0 && numbers.get(0).isPercent()) {
            textView.setText(String.valueOf(numbers.get(0).getValue() / PERCENT_BASE));
            resultPrinted = true;
            return;
        }

        ICNumber resultNumber = numbers.get(0);
        for (int i = 1; i <= operators.length(); ++i) {
            char action = operators.charAt(i - 1);
            resultNumber.setValue(InputtedNumber.countResult(resultNumber, numbers.get(i), action));
        }

        textView.setText(String.valueOf(resultNumber.getValue()));
        resultPrinted = true;
    }

    @Override
    protected String getInputOperators() {
        CharSequence inputted = textView.getText();
        StringBuilder operators = new StringBuilder();
        for (int i = 1; i < inputted.length(); ++i) {
            char inputtedChar = inputted.charAt(i);
            switch (inputtedChar) {
                case '+':
                case '*':
                case '/':
                case '^':
                    operators.append(inputtedChar);
                    break;
                case '-':
                    if (Character.isDigit(inputted.charAt(i - 1)) || inputted.charAt(i - 1) == ')')
                        operators.append(inputtedChar);
                    break;
            }
        }
        return operators.toString();
    }

    @Override
    public void handleBackspace() {
        String input = textView.getText().toString();
        if(input.matches(".*[a-z]+")) {
            while(input.length() > 0 && Character.isLetter(input.charAt(input.length() - 1))) {
                input = input.substring(0, input.length() - 1);
            }
            textView.setText(input);
        } else
            super.handleBackspace();
    }
}
