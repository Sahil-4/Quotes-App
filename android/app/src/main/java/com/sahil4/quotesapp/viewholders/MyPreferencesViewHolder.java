package com.sahil4.quotesapp.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil4.quotesapp.models.MyPreference;
import com.sahil4.quotesapp.R;

public class MyPreferencesViewHolder extends RecyclerView.ViewHolder {
    final TextView titleTextView;
    final CheckBox checkBox;

    public MyPreferencesViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.title);
        checkBox = itemView.findViewById(R.id.checkBox);
    }

    public void bind(MyPreference preference) {
        titleTextView.setText(preference.getTitle());
        checkBox.setChecked(preference.getChecked());
    }
}
