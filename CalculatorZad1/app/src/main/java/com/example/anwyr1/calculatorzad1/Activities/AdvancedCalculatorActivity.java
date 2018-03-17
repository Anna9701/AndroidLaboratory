package com.example.anwyr1.calculatorzad1.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.anwyr1.calculatorzad1.Services.AdvancedCalculator;
import com.example.anwyr1.calculatorzad1.R;

public class AdvancedCalculatorActivity extends AppCompatActivity {
    private AdvancedCalculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_calculator);
        calculator = new AdvancedCalculator(findViewById(R.id.textView2), this);
    }

    public void onClickNumber(View v) {
        Button b = (Button) v;
        String number = b.getText().toString();
        calculator.handleNumber(number);
    }

    public void onClickDot(View v) {
        calculator.handleDotInput();
    }

    public void onClickOperator(View v) {
        Button b = (Button) v;
        String operator = b.getText().toString();
        calculator.handleOperator(operator);
    }

    public void onSummarizeClick(View v) {
        calculator.summarize();
    }

    public void onBackspaceClick(View v) {
        calculator.handleBackspace();
    }

    public void onChangeSignClick(View v) {
        calculator.handleChangSignOperator();
    }

    public void onClearClick(View v) {
        calculator.clearInput();
    }

    public void onSquarePowerClick(View v) {
        calculator.handleSquarePower();
    }
    
}
