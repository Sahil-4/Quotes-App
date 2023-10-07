package com.sahil4.quotesapp.utility;

import com.sahil4.quotesapp.models.Quote;

public interface OnQuoteReceiveListener {
    void updateQuote(Quote quote);
}
