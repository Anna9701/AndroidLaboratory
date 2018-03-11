package com.example.anwyr1.calculatorzad1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UnknownFormatConversionException;

public class AdvancedCalculatorActivity extends AppCompatActivity {
    private BasicCalculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_calculator);
        calculator = new BasicCalculator(findViewById(R.id.textView2));
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
}
