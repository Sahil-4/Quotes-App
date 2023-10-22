package com.sahil4.quotesapp;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sahil4.quotesapp.models.Quote;
import com.sahil4.quotesapp.networkhelper.NetworkHelper;
import com.sahil4.quotesapp.utility.OnQuoteReceiveListener;

public class MyReceiver extends BroadcastReceiver implements OnQuoteReceiveListener {
    protected final String CHANNEL_ID = "SEND_QUOTE_NOTIFICATION_CHANNEL";
    NetworkHelper networkHelper;
    Context context;
    Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;

        Log.d("onReceive", "we are in onReceive");
        getNewQuote();
    }

    @Override
    public void updateQuote(Quote qt) {
        Log.d("updateQuote", "about to update quote,");
        showNotification(context, intent, "Quote update", qt.getContent(), qt.getAuthor(), (int) qt.get_id());
    }

    public void getNewQuote() {
        networkHelper = new NetworkHelper((Application) this.context.getApplicationContext());
        networkHelper.setOnQuoteReceiveListener(this);
        networkHelper.getNewQuote();
        Log.d("getNewQuote", "called getNewQuote");
    }

    public void showNotification(Context context, Intent intent, String title, String body, String author, int _id) {
        Log.d("showNotification", "will show notification now.");

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("showNotification", "No permission.");
            return;
        }

        Log.d("showNotification", "Possible show notification.");
        // creating pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, _id, new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);

        // creating notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "QUOTES_APP_NOTIFICATION_CHANNEL", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notification channel created to show Quote notification");

            // creating notification manager class
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);

            // Check if the channel already exists
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                // and register the channel with the system if it doesn't exist
                notificationManager.createNotificationChannel(channel);
            }
        }

        // share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, body.concat("\n\nQuote By ").concat(author));

        // share pending intent
        PendingIntent sharePendingIntent = PendingIntent.getActivity(context, _id + 100, Intent.createChooser(shareIntent, "Share quote"), PendingIntent.FLAG_IMMUTABLE);

        // share action
        NotificationCompat.Action shareAction = new NotificationCompat.Action(R.drawable.share, "Share", sharePendingIntent);

        // building notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.launcher_icon_main
        ).setContentTitle(title).setContentText(body).setStyle(new NotificationCompat.BigTextStyle().bigText(body)).setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).addAction(shareAction);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        // show notification
        notificationManagerCompat.notify(intent.getIntExtra("ID", 0), builder.build());

        Log.d("showNotification", "notification done.");
    }
}
