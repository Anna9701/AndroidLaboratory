package com.example.anwyr1.calculatorzad1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button exitButton = (Button) findViewById(R.id.button4);
        Button aboutButton = (Button) findViewById(R.id.button3);
        Button advancedButton = (Button) findViewById(R.id.button2);
        Button simpleButton = (Button) findViewById(R.id.button);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Main2Activity.this, AboutActivity.class);
                startActivity(myIntent);
            }
        });

        simpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Main2Activity.this, BasicCalculatorActivity.class);
                startActivity(myIntent);
            }
        });

        advancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Main2Activity.this, AdvancedCalculatorActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
