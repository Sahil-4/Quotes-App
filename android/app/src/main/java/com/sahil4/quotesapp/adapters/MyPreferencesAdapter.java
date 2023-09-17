package com.sahil4.quotesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil4.quotesapp.models.MyPreference;
import com.sahil4.quotesapp.R;
import com.sahil4.quotesapp.utility.OnPreferenceClickListener;
import com.sahil4.quotesapp.viewholders.MyPreferencesViewHolder;

import java.util.List;

public class MyPreferencesAdapter extends RecyclerView.Adapter<MyPreferencesViewHolder> {
    public final List<MyPreference> allPreferences;
    OnPreferenceClickListener onClickListener = null;

    public MyPreferencesAdapter(List<MyPreference> preferences) {
        this.allPreferences = preferences;
    }

    public void setOnPreferenceClickListener(OnPreferenceClickListener listener) {
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public MyPreferencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preference_item, parent, false);
        return new MyPreferencesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPreferencesViewHolder holder, int position) {
        holder.bind(allPreferences.get(position));

        holder.itemView.findViewById(R.id.checkBox).setOnClickListener(v -> {
            if (this.onClickListener != null) {
                this.onClickListener.updateState(allPreferences.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return allPreferences != null ? allPreferences.size() : 0;
    }
}
