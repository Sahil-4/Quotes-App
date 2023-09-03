package com.sahil4.quotesapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil4.quotesapp.models.Quote;
import com.sahil4.quotesapp.R;
import com.sahil4.quotesapp.utility.HistoryItemOnClickListener;
import com.sahil4.quotesapp.viewholders.QuotesViewHolder;

import java.util.List;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesViewHolder> {
    List<Quote> allQuotes;
    HistoryItemOnClickListener historyItemOnClickListener = null;

    public QuotesAdapter() {
    }

    public void setOnQuoteClickListener(HistoryItemOnClickListener listener) {
        this.historyItemOnClickListener = listener;
    }

    @NonNull
    @Override
    public QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new QuotesViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesViewHolder holder, int position) {
        holder.bind(allQuotes.get(position));
        holder.itemView.setOnClickListener(view -> {
            if (this.historyItemOnClickListener != null) {
                this.historyItemOnClickListener.showThisQuote(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allQuotes != null ? allQuotes.size() : 0;
    }

    public void setAllQuotes(List<Quote> allQuotes) {
        this.allQuotes = allQuotes;
        notifyItemRangeChanged(0, allQuotes.size() - 1);
    }
}
