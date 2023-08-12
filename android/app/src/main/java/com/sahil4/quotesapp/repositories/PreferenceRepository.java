package com.sahil4.quotesapp.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sahil4.quotesapp.models.MyPreference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PreferenceRepository {
    private final SharedPreferences sharedPreferences;
    private List<MyPreference> allPreferences = new ArrayList<>();

    public PreferenceRepository(Context context) {
        sharedPreferences = context.getSharedPreferences("user_preferences_quotes_app", Context.MODE_PRIVATE);
    }

    public void setDefault() {
        // feed default data with help of arr[]

        String[] arr = {"Athletics", "Business", "Change", "Character", "Competition", "Conservative", "Courage", "Education", "Faith", "Family", "Famous quotes", "Film", "Freedom", "Friendship", "Future", "Happiness", "History", "Honor", "Humor", "Humorous", "Inspirational", "Leadership", "Life", "Literature", "Love", "Motivational", "Nature", "Pain", "Philosophy", "Politics", "Power quotes", "Proverb", "Religion", "Science", "Self", "Self help", "Social justice", "Spirituality", "Sports", "Success", "Technology", "Time", "Truth", "Virtue", "War", "Wisdom"};

        for (String s : arr) {
            MyPreference preference = new MyPreference(s, false);
            allPreferences.add(preference);
        }

        savePreferences();
    }

    public List<MyPreference> getPreferences() {
        // get data from shared preferences
        String json = sharedPreferences.getString("user_preferences", null);

        // set default data if its not present
        if (json == null) {
            setDefault();
            return allPreferences;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<MyPreference>>() {
        }.getType();
        allPreferences = gson.fromJson(json, type);

        return allPreferences;
    }

    public void updatePreferences(MyPreference preference) {
        // update preferences
        for (MyPreference pref : allPreferences) {
            if (Objects.equals(pref.getTitle(), preference.getTitle())) {
                pref.setChecked(preference.getChecked());
            }
        }
    }

    public void savePreferences() {
        // save myPreferences into shared preferences
        Gson gson = new Gson();
        String json = gson.toJson(allPreferences);

        // saving
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_preferences", json);
        editor.apply();
    }
}
