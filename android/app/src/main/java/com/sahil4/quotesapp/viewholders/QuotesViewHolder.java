package com.sahil4.quotesapp.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil4.quotesapp.models.Quote;
import com.sahil4.quotesapp.R;

public class QuotesViewHolder extends RecyclerView.ViewHolder {
    final TextView quoteTextView;

    public QuotesViewHolder(@NonNull View itemView) {
        super(itemView);
        quoteTextView = itemView.findViewById(R.id.quote_textview);
    }

    public void bind(@NonNull Quote quote) {
        quoteTextView.setText(quote.getContent());
    }
}
