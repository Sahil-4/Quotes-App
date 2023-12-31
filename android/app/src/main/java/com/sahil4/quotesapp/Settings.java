package com.sahil4.quotesapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import java.util.Calendar;
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
            cancelNotification(myNotificationTime);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!requestNotificationPermission()) {
                return;
            }
        }
        enableNotificationSwitchCompat.setChecked(allNotificationTimes.size() > 0);
    }

    void setNotification(NotificationTime notificationTime) {
        // Set Alarm and Notification
        // Adding time in calendar for alarm
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, notificationTime.getPM() ? notificationTime.getHour() + 12 : notificationTime.getHour());
        c.set(Calendar.MINUTE, notificationTime.getMinutes());
        c.set(Calendar.SECOND, 0);

        // Creating intent
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("ID", (int) notificationTime.get_id());

        // create pending intent using above intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) notificationTime.get_id(), intent, PendingIntent.FLAG_IMMUTABLE);

        // creating instance of alarm manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // setting alarm
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    void cancelNotification(NotificationTime notificationTime) {
        // cancelling the notification
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("ID", (int) notificationTime.get_id());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) notificationTime.get_id(), intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel((int) notificationTime.get_id());
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public boolean requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            return true;
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            return false;
        }
    }

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
        }
    });
}