package com.sahil4.quotesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sahil4.quotesapp.adapters.MyPreferencesAdapter;
import com.sahil4.quotesapp.adapters.NotificationTimesAdapter;
import com.sahil4.quotesapp.models.MyPreference;
import com.sahil4.quotesapp.models.NotificationTime;
import com.sahil4.quotesapp.viewmodels.NotificationTimeItemViewModel;
import com.sahil4.quotesapp.viewmodels.PreferencesViewModel;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {
    TextView addTimeTextView;
    androidx.appcompat.widget.SwitchCompat enableNotificationSwitchCompat;

    RecyclerView preferencesRecyclerView;
    RecyclerView notificationTimeRecyclerView;

    MyPreferencesAdapter preferencesAdapter;
    NotificationTimesAdapter notificationTimeAdapter;

    PreferencesViewModel preferencesViewModel;
    NotificationTimeItemViewModel notificationTimeItemViewModel;

    List<MyPreference> allPreferences = new ArrayList<>();
    List<NotificationTime> allNotificationTimes = new ArrayList<>();

    public final int PREFERENCES_UPDATED = 3002;

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
        saveButton.setOnClickListener(view -> saveSettings());

        // Add new time button
        addTimeTextView = findViewById(R.id.add_button);
        addTimeTextView.setOnClickListener(view -> addNewTimeInList());

        enableNotificationSwitchCompat = findViewById(R.id.checkBox);
        enableNotificationSwitchCompat.setOnClickListener(view -> {
            // to remove all times from list if toggle is disabled
            if (!enableNotificationSwitchCompat.isChecked()) {
                int n = allNotificationTimes.size();
                notificationTimeItemViewModel.deleteAllNotificationTime();
                notificationTimeAdapter.notifyItemRangeRemoved(0, n);
            }
        });

        // initialising view model
        preferencesViewModel = new PreferencesViewModel(getApplication());
        notificationTimeItemViewModel = new NotificationTimeItemViewModel(getApplication());

        // updating preferences list
        allPreferences = preferencesViewModel.getPreferences();
        allNotificationTimes = notificationTimeItemViewModel.getNotificationTimeList();

        // show all the preferences in recycler view
        preferencesRecyclerView = findViewById(R.id.recycler_view);
        preferencesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // show all the times in recycler view
        notificationTimeRecyclerView = findViewById(R.id.recycler_view1);
        notificationTimeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // creating adapter
        preferencesAdapter = new MyPreferencesAdapter(allPreferences);
        notificationTimeAdapter = new NotificationTimesAdapter(allNotificationTimes);

        // setting adapter
        preferencesRecyclerView.setAdapter(preferencesAdapter);
        notificationTimeRecyclerView.setAdapter(notificationTimeAdapter);

        // setting on click listener to check and uncheck preferences
        preferencesAdapter.setOnPreferenceClickListener(myPreference -> {
            myPreference.setChecked(!myPreference.getChecked());
            preferencesViewModel.updatePreference(myPreference);
        });

        // delete the time from list
        notificationTimeAdapter.setOnNotificationTimeClickListener(myNotificationTime -> {
            notificationTimeAdapter.notifyItemRemoved(allNotificationTimes.indexOf(myNotificationTime));
            notificationTimeItemViewModel.deleteNotificationTime(myNotificationTime);
            toggleSwitch();
        });

        toggleSwitch();
    }

    void saveSettings() {
        int count = 0;
        for (MyPreference preference : allPreferences) {
            if (preference.getChecked()) {
                count++;
            }
        }

        if (count >= 3 || count == 0) {
            // saving time and preferences
            notificationTimeItemViewModel.updateNotificationTime();
            preferencesViewModel.savePreferences();

            // setting notifications
            for (NotificationTime notificationTime : allNotificationTimes) {
                setNotification(notificationTime);
            }

            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();

            setResult(PREFERENCES_UPDATED, new Intent());
            finish();
        } else {
            Toast.makeText(this, "Select minimum 3 preferences.", Toast.LENGTH_SHORT).show();
        }
    }

    void addNewTimeInList() {
        TimePickerDialog dialog = new TimePickerDialog(this, (timePicker, hour, minutes) -> {
            // add time in list
            notificationTimeItemViewModel.addNotificationTime(new NotificationTime(hour, minutes));
            notificationTimeAdapter.notifyItemInserted(allNotificationTimes.size() - 1);

            toggleSwitch();
        }, 8, 0, false);

        dialog.show();
    }

    void toggleSwitch() {
        enableNotificationSwitchCompat.setChecked(allNotificationTimes.size() > 0);
    }

    void setNotification(NotificationTime notificationTime) {
        // TODO - set notification
    }
}