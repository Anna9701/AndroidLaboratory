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
        if (!(textView.getText().length() > 0)) return;
        handleOperator("^");
        textView.append("2");
    }

    //TODO not working!!!!!!!!!!!!!!!
    public void handleUnaryOperator(String operator) {
        if (resultPrinted) {
            clearInput();
            resultPrinted = false;
        }
        String input = textView.getText().toString();
        if (input.length() > 0 && Character.isDigit(input.charAt(input.length() - 1)) &&
                input.charAt(input.length() - 1) != ')') return;
        textView.append(operator);
    }
}
