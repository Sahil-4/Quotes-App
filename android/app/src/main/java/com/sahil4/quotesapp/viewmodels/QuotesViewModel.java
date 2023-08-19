package com.sahil4.quotesapp.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sahil4.quotesapp.models.Quote;
import com.sahil4.quotesapp.repositories.QuoteRepository;

import java.util.List;

public class QuotesViewModel extends AndroidViewModel {
    private final QuoteRepository repository;
    private final LiveData<List<Quote>> allQuote;

    public QuotesViewModel(Application application) {
        super(application);
        repository = new QuoteRepository(application);
        allQuote = repository.getAllQuotes();
    }

    public void newQuote() {
        repository.newQuote();
    }

    public void deleteQuote(Quote quote) {
        repository.delete(quote);
    }

    public LiveData<List<Quote>> getAllQuotes() {
        return allQuote;
    }
}
