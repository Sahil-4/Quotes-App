package com.sahil4.quotesapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.sahil4.quotesapp.models.NotificationTime;
import com.sahil4.quotesapp.repositories.NotificationTimeItemRepository;

import java.util.List;

public class NotificationTimeItemViewModel extends AndroidViewModel {
    private final NotificationTimeItemRepository notificationTimeItemRepository;

    public NotificationTimeItemViewModel(@NonNull Application application) {
        super(application);
        notificationTimeItemRepository = new NotificationTimeItemRepository(application);
    }

    public List<NotificationTime> getNotificationTimeList() {
        return notificationTimeItemRepository.getNotificationTimeList();
    }

    public void addNotificationTime(NotificationTime notificationTime) {
        notificationTimeItemRepository.addNotificationTime(notificationTime);
    }

    public void deleteNotificationTime(NotificationTime notificationTime) {
        notificationTimeItemRepository.deleteNotificationTime(notificationTime);
    }

    public void deleteAllNotificationTime() {
        notificationTimeItemRepository.deleteAllNotificationTime();
    }

    public void updateNotificationTime() {
        notificationTimeItemRepository.updateNotificationTime();
    }
}
