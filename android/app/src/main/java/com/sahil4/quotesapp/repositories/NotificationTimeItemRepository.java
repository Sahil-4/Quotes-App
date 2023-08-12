package com.sahil4.quotesapp.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sahil4.quotesapp.models.NotificationTime;

import java.lang.reflect.Type;
import java.util.List;

import kotlin.collections.ArrayDeque;

public class NotificationTimeItemRepository {
    private final SharedPreferences sharedPreferences;
    private List<NotificationTime> notificationTimeList = new ArrayDeque<>();

    public NotificationTimeItemRepository(Context context) {
        sharedPreferences = context.getSharedPreferences("user_notification_time_quotes_app", Context.MODE_PRIVATE);
    }

    public List<NotificationTime> getNotificationTimeList() {
        // get list of times from shared preferences
        String json = sharedPreferences.getString("notification_time", null);

        if (json == null) {
            return notificationTimeList;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<NotificationTime>>() {
        }.getType();
        notificationTimeList = gson.fromJson(json, type);

        return notificationTimeList;
    }

    public void addNotificationTime(NotificationTime notificationTime) {
        notificationTimeList.add(notificationTime);
    }

    public void deleteNotificationTime(NotificationTime notificationTime) {
        notificationTimeList.remove(notificationTime);
    }

    public void deleteAllNotificationTime() {
        notificationTimeList.clear();
    }

    public void updateNotificationTime() {
        Gson gson = new Gson();
        String json = gson.toJson(notificationTimeList);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("notification_time", json);
        editor.apply();
    }
}
