package com.example.anwyr1.calculatorzad1.Services;

import android.app.Activity;
import android.view.View;

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
                input.charAt(input.length() - 1) != ')' || endsWithUnaryOperator(input)) {
            showAlert("Error", "Faulty operation requested.");
            return;
        }
        textView.append(operator);
    }

    private boolean endsWithUnaryOperator(String string) {
        return string.endsWith("sin") || string.endsWith("cos") || string.endsWith("tan") ||
                string.endsWith("ln") || string.endsWith("sqrt") || string.endsWith("log");
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
