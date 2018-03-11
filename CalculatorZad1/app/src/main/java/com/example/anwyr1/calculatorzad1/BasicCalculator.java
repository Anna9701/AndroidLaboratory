package com.example.anwyr1.calculatorzad1;

import android.view.View;
import android.widget.TextView;

import java.util.UnknownFormatConversionException;

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

    void handleNumber(String inputted) {
        if (resultPrinted) {
            clearInput();
            resultPrinted = false;
        }
        try {
            int number = Integer.parseInt(inputted);
            textView.append(String.valueOf(number));
        } catch (NumberFormatException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void clearInput() {
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
            return inputted.split("[^0-9.]");
        }
        return null;
    }

    private double getResult(double number, double secondNumber, char action) {
        switch (action) {
            case '+':
                return number + secondNumber;
            case '-':
                return number - secondNumber;
            case '*':
                return number * secondNumber;
            case '/':
                return number / secondNumber;
        }

        throw new UnknownFormatConversionException("Wrong operator " + action);
    }

    public void summarize() {
        String inputs[] = getInputTextSplitted();
        String operators = getInputOperators();
        if (inputs == null || inputs.length < 2 || operators.length() == 0) {
            return;
        }
        double result = 0;
        for (int i = 0, j = 0; j < operators.length(); i += 2, ++j) {
            double number = Double.parseDouble(inputs[i]);
            double secondNumber = Double.parseDouble(inputs[i + 1]);
            char action = operators.charAt(j);
            result += getResult(number, secondNumber, action);
        }
        textView.setText(String.valueOf(result));
        resultPrinted = true;
    }

    private String getInputOperators() {
        CharSequence inputted = textView.getText();
        String operators = "";
        for (int i = 0; i < inputted.length(); ++i) {
            switch (inputted.charAt(i)) {
                case '-':
                case '+':
                case '*':
                case '/':
                    operators += inputted.charAt(i);
                    break;
            }
        }
        return operators;
    }

    public void handleOperator(String operator) {
        if (resultPrinted)
            resultPrinted = false;
        String input = textView.getText().toString();
        if (Character.isDigit(input.charAt(input.length() - 1)))
            textView.append(operator);
    }
}
