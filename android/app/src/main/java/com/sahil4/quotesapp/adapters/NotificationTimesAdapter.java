package com.sahil4.quotesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil4.quotesapp.R;
import com.sahil4.quotesapp.models.NotificationTime;
import com.sahil4.quotesapp.utility.OnNotificationTimeClickListener;
import com.sahil4.quotesapp.utility.OnPreferenceClickListener;
import com.sahil4.quotesapp.viewholders.NotificationTimeViewHolder;

import java.util.List;

public class NotificationTimesAdapter extends RecyclerView.Adapter<NotificationTimeViewHolder> {
    public final List<NotificationTime> notificationTimeList;
    OnNotificationTimeClickListener onClickListener = null;

    public NotificationTimesAdapter(List<NotificationTime> notificationTimeList) {
        this.notificationTimeList = notificationTimeList;
    }

    public void setOnNotificationTimeClickListener(OnNotificationTimeClickListener listener) {
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public NotificationTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_item, parent, false);
        return new NotificationTimeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationTimeViewHolder holder, int position) {
        holder.bind(notificationTimeList.get(position));

        holder.itemView.findViewById(R.id.delete_time_imageButton).setOnClickListener(v -> {
            if (this.onClickListener != null) {
                this.onClickListener.deleteNotificationTime(notificationTimeList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationTimeList != null ? notificationTimeList.size() : 0;
    }
}
