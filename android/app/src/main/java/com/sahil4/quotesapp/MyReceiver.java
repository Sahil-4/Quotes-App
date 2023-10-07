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
import android.widget.Toast;

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

        getNewQuote();
    }

    @Override
    public void updateQuote(Quote qt) {
        showNotification(context, intent, "Quote update", qt.getContent(), qt.getAuthor());
    }

    public void getNewQuote() {
        networkHelper = new NetworkHelper((Application) this.context.getApplicationContext());
        networkHelper.setOnQuoteReceiveListener(this);
        networkHelper.getNewQuote();
    }

    public void showNotification(Context context, Intent intent, String title, String body, String author) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Unable to show notification", Toast.LENGTH_SHORT).show();
            return;
        }

        // creating pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

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
        PendingIntent sharePendingIntent = PendingIntent.getActivity(context, 1, Intent.createChooser(shareIntent, "Share quote"), PendingIntent.FLAG_UPDATE_CURRENT);

        // share action
        NotificationCompat.Action shareAction = new NotificationCompat.Action(R.drawable.share, "Share", sharePendingIntent);

        // building notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.launcher_icon_main
        ).setContentTitle(title).setContentText(body).setStyle(new NotificationCompat.BigTextStyle().bigText(body)).setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).addAction(shareAction);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        // show notification
        notificationManagerCompat.notify(intent.getIntExtra("ID", 0), builder.build());
    }
}
