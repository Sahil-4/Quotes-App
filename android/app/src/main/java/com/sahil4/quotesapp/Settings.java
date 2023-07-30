package com.sahil4.quotesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // setting toolbar title
        TextView textView = findViewById(R.id.top_bar_title);
        textView.setText(R.string.settings);

        // back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        // save preferences button
        ImageButton saveButton = findViewById(R.id.save_button);
        saveButton.setVisibility(View.VISIBLE);
        saveButton.setOnClickListener(view -> Toast.makeText(this, "Functionality not implemented yet.", Toast.LENGTH_SHORT).show());

    }
}