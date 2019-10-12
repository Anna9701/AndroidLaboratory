package com.example.anwyr1.calculatorzad1.Services;

import android.app.Activity;
import android.view.View;

import static com.example.anwyr1.calculatorzad1.Services.MathematicalNamesUtils.*;


public class AdvancedCalculator extends BasicCalculator {

    public AdvancedCalculator(View v, Activity activity) {
        super(v, activity);
    }

    public void handleSquarePower() {
        if (!(textView.getText().length() > 0)) {
            showAlert(ERROR_ALERT_TITLE, ERROR_ALERT_DEFAULT_CONTENT);
            return;
        }
        handleOperator(String.valueOf(POWER_OPERATOR));
        textView.append(SQUARE);
    }

    public void handleUnaryOperator(String operator) {
        if (resultPrinted) {
            clearInput();
            resultPrinted = false;
        }
        String input = textView.getText().toString();
        if (input.length() > 0 && Character.isDigit(input.charAt(input.length() - 1)) &&
                input.charAt(input.length() - 1) != CLOSING_BRACKET || endsWithUnaryOperator(input)) {
            showAlert(ERROR_ALERT_TITLE, ERROR_ALERT_DEFAULT_CONTENT);
            return;
        }
        textView.append(operator);
    }

    private boolean endsWithUnaryOperator(String string) {
        return string.endsWith(SINUS_NAME) || string.endsWith(COSINES_NAME) || string.endsWith(TANGENT_NAME) ||
                string.endsWith(NATURAL_LOGARITHM_NAME) || string.endsWith(SQUARE_ROOT_NAME) || string.endsWith(LOGARITHM_NAME);
    }

    @Override
    public void handleBackspace() {
        final String mathematicalFunctionName = ".*[a-z]+";
        String input = textView.getText().toString();
        if(input.matches(mathematicalFunctionName)) {
            while(input.length() > 0 && Character.isLetter(input.charAt(input.length() - 1))) {
                input = input.substring(0, input.length() - 1);
            }
            textView.setText(input);
        } else
            super.handleBackspace();
    }
}
