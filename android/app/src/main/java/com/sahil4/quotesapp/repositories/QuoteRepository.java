package com.sahil4.quotesapp.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sahil4.quotesapp.models.Quote;
import com.sahil4.quotesapp.networkhelper.NetworkHelper;
import com.sahil4.quotesapp.roomdb.QuoteDAO;
import com.sahil4.quotesapp.roomdb.QuoteDatabase;

import java.util.List;

public class QuoteRepository {
    private final QuoteDAO quoteDAO;
    final NetworkHelper networkHelper;

    public QuoteRepository(Application application) {
        QuoteDatabase quoteDatabase = QuoteDatabase.getInstance(application.getApplicationContext());
        networkHelper = new NetworkHelper(application);
        quoteDAO = quoteDatabase.quoteDAO();
    }

    public void newQuote() {
        networkHelper.getNewQuote();
    }

    public void delete(Quote quote) {
        QuoteDatabase.databaseWriteExecutor.execute(() -> quoteDAO.deleteQuote(quote));
    }

    public LiveData<List<Quote>> getAllQuotes() {
        return quoteDAO.getAllQuotes();
    }
}
