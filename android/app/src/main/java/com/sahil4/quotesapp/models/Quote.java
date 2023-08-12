package com.sahil4.quotesapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "qoutes")
public class Quote {
    @PrimaryKey(autoGenerate = true)
    private final long _id;
    private final String Author;
    private final String Content;

    public Quote(String Author, String Content) {
        this._id = Calendar.getInstance().getTimeInMillis();
        this.Author = Author;
        this.Content = Content;
    }

    public String getAuthor() {
        return this.Author;
    }

    public String getContent() {
        return this.Content;
    }

    public long get_id() {
        return this._id;
    }
}
