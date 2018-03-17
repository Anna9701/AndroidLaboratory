package com.example.anwyr1.calculatorzad1.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.anwyr1.calculatorzad1.Activities.AdvancedCalculatorActivity;
import com.example.anwyr1.calculatorzad1.Activities.BasicCalculatorActivity;
import com.example.anwyr1.calculatorzad1.Activities.Main2Activity;
import com.example.anwyr1.calculatorzad1.Interfaces.ICNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anwyr1 on 10/03/2018.
 */

public class BasicCalculator {
    private final TextView textView;
    private boolean resultPrinted;
    private Activity activity;

    public BasicCalculator(View v, Activity activity) {
        textView = (TextView) v;
        resultPrinted = false;
        this.activity = activity;
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
            return inputted.split("[^0-9.%]");
        }
        return null;
    }

    private ArrayList<ICNumber> getInputNumbersSplitted() {
        String inputted = textView.getText().toString();
        String splitted[] = {""};
        ArrayList<ICNumber> numbers = new ArrayList<>();
        if(inputted.length() > 0) {
            inputted = inputted.replace(")-", ") -");
            splitted = inputted.split("[^(\\-0-9.%)]");
        }
        for (String s : splitted) {
            numbers.add(new InputtedNumber(s));
        }
        return numbers;
    }

    //TODO 5+(-5)-5 -> now result is 0, fix it.
    public void summarize() {
        final double PERCENT_BASE = 100;
        List<ICNumber> numbers = getInputNumbersSplitted();
        String operators = getInputOperators();
        if (numbers.size() == 0 || operators.length() == 0 && !numbers.get(0).isPercent()) {
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

    @NonNull
    private String getInputOperators() {
        CharSequence inputted = textView.getText();
        StringBuilder operators = new StringBuilder();
        for (int i = 1; i < inputted.length(); ++i) {
            switch (inputted.charAt(i)) {
                case '+':
                case '*':
                case '/':
                    operators.append(inputted.charAt(i));
                    break;
                case '-':
                    if (Character.isDigit(inputted.charAt(i - 1) ))
                        operators.append(inputted.charAt(i));
                        break;
            }
        }
        return operators.toString();
    }

    public void handleChangSignOperator() {
        String input = textView.getText().toString();
        input = input.replace(")-", ") -");
        String[] splitted = input.split("[^(\\-0-9.%)]");
        int index = splitted.length;
        if (index-- == 0 || !Character.isDigit(input.charAt(input.length() - 1)) &&
                input.charAt(input.length() - 1) != ')') {
            showAlert("Error", "Wrong format");
            return; // Wrong format
        }

        String number = splitted[index];
        if(number.contains("-"))
            changeIntoPositive(input, number);
        else
            changeIntoNegative(input, number);


    }

    private void changeIntoNegative(String input, String splitted) {
        textView.setText(input.subSequence(0, input.length() - splitted.length()));
        splitted = "(-" + splitted + ")";
        textView.append(splitted);
    }

    //TODO (-6) 
    private void changeIntoPositive(String input, String splitted) {
        if (splitted.contains("(-")) {
            textView.setText(input.subSequence(0, input.length() - splitted.length() - 1));
            splitted = splitted.substring("(-".length(), splitted.length() - ")".length());
            textView.append(splitted);
        } else {
            textView.setText(input.subSequence(0, input.length() - splitted.length()));
            splitted = "+" + splitted.substring(1);
            textView.append(splitted);
        }
    }


    public void handleOperator(String operator) {
        if (resultPrinted)
            resultPrinted = false;
        String input = textView.getText().toString();
        if (input.length() == 0) return;
        char lastCharacter = input.charAt(input.length() - 1);
        if (Character.isDigit(lastCharacter) || lastCharacter == '%' && !operator.equals("%") || lastCharacter == ')')
            textView.append(operator);
    }

    public void handleBackspace() {
        CharSequence inputtedText = textView.getText();
        int length = inputtedText.length();
        if (length > 0)
            textView.setText(inputtedText.subSequence(0, length - 1));
    }

    private void showAlert(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(content)
                .setTitle(title);

        AlertDialog dialog = builder.create();
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
