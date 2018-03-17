package com.example.anwyr1.calculatorzad1.Activities;

import android.os.Bundle;
import com.example.anwyr1.calculatorzad1.Services.AdvancedCalculator;
import com.example.anwyr1.calculatorzad1.R;

public class AdvancedCalculatorActivity extends BasicCalculatorActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_calculator);
        calculator = new AdvancedCalculator(findViewById(R.id.textView2), this);
    }

}
