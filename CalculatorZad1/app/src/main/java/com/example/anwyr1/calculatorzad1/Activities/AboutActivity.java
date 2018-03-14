package com.example.anwyr1.calculatorzad1.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.anwyr1.calculatorzad1.R;

public class AboutActivity extends AppCompatActivity {

    private final static String name = "Anna Wyrwal";
    private final static String about = "Simple application which provide possibility of calculations.";
    private final static String university = "Lodz University of Technology";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView nameView = findViewById(R.id.textViewName);
        TextView aboutView = findViewById(R.id.textViewAbout);
        TextView universityView = findViewById(R.id.textViewUniversity);
        nameView.setText(name);
        aboutView.setText(about);
        universityView.setText(university);
    }

}
