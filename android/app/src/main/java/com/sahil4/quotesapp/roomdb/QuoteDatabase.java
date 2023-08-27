package com.sahil4.quotesapp.roomdb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sahil4.quotesapp.models.Quote;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Quote.class}, version = 1)
public abstract class QuoteDatabase extends RoomDatabase {
    public abstract QuoteDAO quoteDAO();

    public static volatile QuoteDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static QuoteDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuoteDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, QuoteDatabase.class, "quotes_database").addCallback(sRoomDatabaseCallback).build();
            }
        }

        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
            });
        }
    };
}
