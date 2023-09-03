package com.sahil4.quotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sahil4.quotesapp.adapters.QuotesAdapter;
import com.sahil4.quotesapp.viewmodels.QuotesViewModel;

public class History extends AppCompatActivity {
    RecyclerView quoteHistoryRecyclerView;

    QuotesViewModel viewModel;
    QuotesAdapter quotesAdapter;

    public final int UPDATE_QUOTE = 3001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // setting title
        TextView textView = findViewById(R.id.top_bar_title);
        textView.setText(R.string.history);

        // back button functionality
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        // initialise recycler view
        quoteHistoryRecyclerView = findViewById(R.id.quote_history);
        quoteHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // initialising view model
        viewModel = new QuotesViewModel(getApplication());

        // adapter
        quotesAdapter = new QuotesAdapter();
        quoteHistoryRecyclerView.setAdapter(quotesAdapter);

        // observing changes and updating the history
        viewModel.getAllQuotes().observe(this, quotes -> quotesAdapter.setAllQuotes(quotes));

        // setting onclick listener on each history item
        quotesAdapter.setOnQuoteClickListener(position -> {
            setResult(UPDATE_QUOTE, new Intent().putExtra("position", position));
            finish();
        });
    }
}