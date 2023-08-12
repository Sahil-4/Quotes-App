package com.sahil4.quotesapp.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sahil4.quotesapp.models.Quote;

import java.util.List;

@Dao
public interface QuoteDAO {
    @Query("SELECT * FROM qoutes order by _id DESC")
    LiveData<List<Quote>> getAllQuotes();

    @Insert
    void insertQuote(Quote quote);

    @Delete
    void deleteQuote(Quote quote);
}
