package com.sahil4.quotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sahil4.quotesapp.models.Quote;
import com.sahil4.quotesapp.networkhelper.NetworkHelper;
import com.sahil4.quotesapp.viewmodels.QuotesViewModel;

public class MainActivity extends AppCompatActivity {
    TextView quoteContentTextView, quoteAuthorTextView;
    Button shareButton;
    ImageButton newQuoteButton;

    QuotesViewModel quotesViewModel = null;

    final int MAX_QUOTES_LENGTH = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TextViews for showing quote
        quoteContentTextView = findViewById(R.id.quote_content);
        quoteAuthorTextView = findViewById(R.id.quote_author);

        // buttons for sharing and new quote
        shareButton = findViewById(R.id.share_button);
        newQuoteButton = findViewById(R.id.refresh_button);

        // making buttons functional
        shareButton.setOnClickListener(view -> shareQuote());
        newQuoteButton.setOnClickListener(view -> getNewQuote());

        // initialising view model
        quotesViewModel = new QuotesViewModel(getApplication());

        // observing quotes state and updating quote on UI
        quotesViewModel.getAllQuotes().observe(this, quotes -> {
            if (quotes.isEmpty()) {
                return;
            }

            // change quote in text view
            Quote quote = quotes.get(0);
            quoteContentTextView.setText(quote.getContent());
            quoteAuthorTextView.setText(quote.getAuthor());

            // delete last/oldest quote if there are more then limit
            if (quotes.size() > MAX_QUOTES_LENGTH) {
                quotesViewModel.deleteQuote(quotes.get(quotes.size() - 1));
            }
        });
    }

    public void shareQuote() {
        // quote to share
        String content = quoteContentTextView.getText() + "\nby " + quoteAuthorTextView.getText();

        // intent to handle share action
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        // setting content and content type
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);

        // starting chooser to select app to which quote has to be shared
        Intent chooserIntent = Intent.createChooser(shareIntent, "Share quote");
        startActivity(chooserIntent);
    }

    public void getNewQuote() {
        quotesViewModel.newQuote();
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

    @Override
    public void onDestroy() {
        // releasing all the resources on app closed
        NetworkHelper.shutdownCronetEngine();
        super.onDestroy();
    }
}
