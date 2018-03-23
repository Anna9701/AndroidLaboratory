package com.example.anwyr1.calculatorzad1.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import java.util.Queue;

/**
 * Created by anwyr1 on 10/03/2018.
 */

public class BasicCalculator {
    protected final TextView textView;
    protected boolean resultPrinted;
    protected Activity activity;

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
            showAlert("Error", "Faulty operation requested.");
            return;
        }
        String lastNumber = inputs[inputs.length - 1];
        if (!lastNumber.contains(".")) {
            if (Character.isDigit(lastNumber.charAt(lastNumber.length() - 1)))
                textView.append(".");
            else
                showAlert("Error", "Faulty operation requested.");
        } else {
            showAlert("Error", "Faulty operation requested.");
        }
    }

    protected String[] getInputTextSplitted() {
        String inputted = textView.getText().toString();
        if(inputted.length() > 0) {
            return inputted.split("[^0-9.%]");
        }
        return null;
    }

    public void summarize() {
        if (textView.getText().length() == 0) {
            showAlert("Error", "Faulty operation requested.");
            return;
        }

        ReversePolishNotationConverter reversePolishNotationConverter = new ReversePolishNotationConverter();
        Queue<RPNSCharacter> characters = reversePolishNotationConverter.convertToReversePolishNotationSequence(textView.getText().toString());
        ReversePolishNotationCounter reversePolishNotationCounter = new ReversePolishNotationCounter();
        Double result = reversePolishNotationCounter.countResult(characters);
        textView.setText(String.valueOf(result));

        resultPrinted = true;
    }

    public void handleChangSignOperator() {
        String input = textView.getText().toString();
        if (input.length() == 0) {
            showAlert("Error", "Faulty operation requested.");
            return;
        }
        input = input.replace("-", " -");
        input = input.replace("( ", "(");
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

    protected void changeIntoNegative(String input, String splitted) {
        textView.setText(input.subSequence(0, input.length() - splitted.length()));
        splitted = "(-" + splitted + ")";
        textView.append(splitted);
    }

    protected void changeIntoPositive(String input, String splitted) {
        if (splitted.contains("(-")) {
            textView.setText(input.subSequence(0, input.length() - splitted.length()));
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
        if (input.length() == 0) {
            showAlert("Error", "Faulty operation requested.");
            return;
        }
        char lastCharacter = input.charAt(input.length() - 1);
        if (Character.isDigit(lastCharacter) || lastCharacter == '%' && !operator.equals("%") || lastCharacter == ')')
            textView.append(operator);
    }

    public void handleBackspace() {
        String inputtedText = textView.getText().toString();
        int length = inputtedText.length();
        if (length > 0 && inputtedText.endsWith(")")) {
            textView.setText(inputtedText.subSequence(0, inputtedText.lastIndexOf("(")));
        } else {
            textView.setText(inputtedText.subSequence(0, length - 1));
        }
    }

    protected void showAlert(String title, String content) {
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
