package com.sahil4.quotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflating options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // making options menu actionable
        if (item.getItemId() == R.id.show_history) {
            startActivity(new Intent(this, History.class));
        } else if (item.getItemId() == R.id.show_settings) {
            startActivity(new Intent(this, Settings.class));
        } else if (item.getItemId() == R.id.show_about) {
            startActivity(new Intent(this, About.class));
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
