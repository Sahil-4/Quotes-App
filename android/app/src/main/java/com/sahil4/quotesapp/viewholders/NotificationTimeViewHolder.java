package com.sahil4.quotesapp.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil4.quotesapp.R;
import com.sahil4.quotesapp.models.NotificationTime;

public class NotificationTimeViewHolder extends RecyclerView.ViewHolder {
    TextView timeTextView;
    ImageButton deleteButton;

    public NotificationTimeViewHolder(@NonNull View itemView) {
        super(itemView);
        timeTextView = itemView.findViewById(R.id.time_textView);
        deleteButton = itemView.findViewById(R.id.delete_time_imageButton);
    }

    public void bind(NotificationTime notificationTime) {
        timeTextView.setText(notificationTime.getTime());
    }
}
