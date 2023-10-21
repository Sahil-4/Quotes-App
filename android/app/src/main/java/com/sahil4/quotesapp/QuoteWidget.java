package com.sahil4.quotesapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.sahil4.quotesapp.models.Quote;
import com.sahil4.quotesapp.networkhelper.NetworkHelper;
import com.sahil4.quotesapp.utility.OnQuoteReceiveListener;

public class QuoteWidget extends AppWidgetProvider implements OnQuoteReceiveListener {
    Context context;
    NetworkHelper networkHelper;
    final int REFRESH_REQUEST_CODE = 3003;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.context = context;

        for (int appWidgetId : appWidgetIds) {
            // Create a RemoteViews object to update the widget's layout
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote_widget);

            // Update the widget content here with your data
            getNewQuote();

            // After updating the content, call the updateAppWidget method
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        // schedule the next update
        scheduleRefresh();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        this.context = context;

        if (intent.getAction() != null && intent.getAction().equals("REFRESH_QUOTE_ACTION")) {
            // Handle the Refresh button click action here
            getNewQuote();
        } else if (intent.getAction() != null && intent.getAction().equals("SHARE_QUOTE_ACTION")) {
            // Handle the Share button click action here
            String quoteContent = intent.getStringExtra("QUOTE_CONTENT");
            shareQuote(quoteContent);
        }
    }

    @Override
    public void updateQuote(Quote quote) {
        // Update the widget with the new quote
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, QuoteWidget.class));
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.quote_widget);
            updateWidget(views, quote);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public void getNewQuote() {
        networkHelper = new NetworkHelper((Application) this.context.getApplicationContext());
        networkHelper.setOnQuoteReceiveListener(this);
        networkHelper.getNewQuote();
    }

    public void scheduleRefresh() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, QuoteWidget.class);
        intent.setAction("REFRESH_QUOTE_ACTION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REFRESH_REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
    }

    public void shareQuote(String quoteContent) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, quoteContent);

        Intent chooser = Intent.createChooser(shareIntent, "Share Quote");
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(chooser);
    }

    private void updateWidget(RemoteViews views, Quote quote) {
        if (quote != null) {
            // quote
            views.setTextViewText(R.id.quote_author, quote.getAuthor());
            views.setTextViewText(R.id.quote_content, quote.getContent());

            // new quote button
            Intent refreshIntent = new Intent(context, QuoteWidget.class);
            refreshIntent.setAction("REFRESH_QUOTE_ACTION");
            PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, (int) quote.get_id(), refreshIntent, PendingIntent.FLAG_MUTABLE);
            views.setOnClickPendingIntent(R.id.refresh_button, refreshPendingIntent);

            // share button
            Intent shareIntent = new Intent(context, QuoteWidget.class);
            shareIntent.setAction("SHARE_QUOTE_ACTION");
            shareIntent.putExtra("QUOTE_CONTENT", quote.getContent().concat("\n\nQuote by ").concat(quote.getAuthor()));
            PendingIntent sharePendingIntent = PendingIntent.getBroadcast(context, (int) quote.get_id(), shareIntent, PendingIntent.FLAG_MUTABLE);
            views.setOnClickPendingIntent(R.id.share_button, sharePendingIntent);
        }
    }
}