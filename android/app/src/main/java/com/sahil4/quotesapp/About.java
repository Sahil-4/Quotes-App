package com.sahil4.quotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // setting title
        TextView textView = findViewById(R.id.top_bar_title);
        textView.setText(R.string.about);

        // back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());
    }
}