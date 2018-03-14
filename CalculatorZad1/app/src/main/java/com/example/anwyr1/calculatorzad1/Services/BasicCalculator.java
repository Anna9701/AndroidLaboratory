package com.example.anwyr1.calculatorzad1.Services;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.anwyr1.calculatorzad1.Interfaces.ICNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anwyr1 on 10/03/2018.
 */

public class BasicCalculator {
    private final TextView textView;
    private boolean resultPrinted;

    public BasicCalculator(View v) {
        textView = (TextView) v;
        resultPrinted = false;
    }

    public void handleNumber(String inputted) {
        if (resultPrinted) {
            clearInput();
            resultPrinted = false;
        }
        textView.append(inputted);
    }

    public void clearInput() {
        textView.setText("");
    }

    public void handleDotInput() {
        String inputs[] = getInputTextSplitted();
        if (inputs == null) {
            return;
        }
        String lastNumber = inputs[inputs.length - 1];
        if (!lastNumber.contains(".")) {
            if (Character.isDigit(lastNumber.charAt(lastNumber.length() - 1)))
                textView.append(".");
        }
    }

    private String[] getInputTextSplitted() {
        String inputted = textView.getText().toString();
        if(inputted.length() > 0) {
            return inputted.split("[^0-9.%]"); //TODO check if works
        }
        return null;
    }

    private ArrayList<ICNumber> getInputNumbersSplitted() {
        String inputted = textView.getText().toString();
        String splitted[] = {""};
        ArrayList<ICNumber> numbers = new ArrayList<>();
        if(inputted.length() > 0) {
            splitted = inputted.split("[^0-9.%]");
        }
        for (String s : splitted) {
            numbers.add(new InputtedNumber(s));
        }
        return numbers;
    }

    //TODO check if works
    public void summarize() {
        final double PERCENT_VALUE = 0.1;
        List<ICNumber> numbers = getInputNumbersSplitted();
        String operators = getInputOperators();
        if (numbers.size() == 0 || operators.length() == 0 && !numbers.get(0).isPercent()) {
            return;
        } else if (operators.length() == 0 && numbers.get(0).isPercent()) {
            textView.setText(String.valueOf(numbers.get(0).getValue() * PERCENT_VALUE));
            resultPrinted = true;
            return;

        }

        double result = 0;
        for (int i = 0, j = 0; j < operators.length(); ++i, ++j) {
            char action = operators.charAt(j);
            numbers.get(i).setValue(InputtedNumber.countResult(numbers.get(i), numbers.get(i + 1), action));
            result += numbers.get(i).getValue();
        }

        textView.setText(String.valueOf(result));
        resultPrinted = true;
    }

    @NonNull
    private String getInputOperators() {
        CharSequence inputted = textView.getText();
        StringBuilder operators = new StringBuilder();
        for (int i = 0; i < inputted.length(); ++i) {
            switch (inputted.charAt(i)) {
                case '-':
                case '+':
                case '*':
                case '/':
                    operators.append(inputted.charAt(i));
                    break;
            }
        }
        return operators.toString();
    }

    public void handleOperator(String operator) {
        if (resultPrinted)
            resultPrinted = false;
        String input = textView.getText().toString();
        if (Character.isDigit(input.charAt(input.length() - 1)) || input.charAt(input.length() - 1) == '%')
            textView.append(operator);
    }
}
