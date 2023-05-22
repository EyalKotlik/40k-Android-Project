package com.example.wh40kapp;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.wh40kapp.MainActivity;
import com.example.wh40kapp.R;

import java.util.Date;

public class AppUsageNotifications extends Service {

    private Handler handler;
    private Runnable runnable;
    private static final long SECONDS_IN_A_DAY = 24 * 60 * 60 * 1000;
    private static final String CHANNEL_ID = "AppUsageNotifications";

    @Override
    public void onCreate() {
        Log.d("TAG", "onCreate: notification service started");
        super.onCreate();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Perform the check and send notification here
                Log.d("TAG", "run: checking app usage");
                checkAppUsage();
                handler.postDelayed(this, 10000);
            }
        };
    }

    private void checkAppUsage() {
        SharedPreferences preferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        long lastAppUsageTime = preferences.getLong("lastAppUsageTime", 0);
        long currentTime = new Date().getTime();
        Log.d("TAG", "checkAppUsage: lastAppUsageTime = " + lastAppUsageTime + ", currentTime = " + currentTime);
        long daysSinceLastUsage = (currentTime - lastAppUsageTime) / SECONDS_IN_A_DAY;

        if (daysSinceLastUsage >= 14) {
            sendNotification();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, createNotification());
        Log.d("TAG", "onStartCommand: started");
        handler.postDelayed(runnable, 10000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification() {

        CharSequence name = "Reminder";
        String description = "the user doesn't use the app for 14 days";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("The Omnissiah has noticed that you haven't used the app for 14 days")
                .setContentText("Use the app; or you and the phone's machine spirit will be purged.")
                .setSmallIcon(R.drawable.cog_mechanicum)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        Log.d("TAG", "sendNotification: ");
        notificationManager.notify(1, builder.build());
    }
    private Notification createNotification(){
        Log.d("TAG", "createNotification: started");
        CharSequence name = "Test Name";
        String description = "Test Desc";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("The Omnissiah is watching you")
                .setContentText("Fail to use the app; and suffer the consequences of your heresy.")
                .setSmallIcon(R.drawable.cog_mechanicum)
                .setPriority(NotificationCompat.PRIORITY_MAX);
        return builder.build();
    }
}

