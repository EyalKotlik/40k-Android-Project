import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.wh40kapp.MainActivity;
import com.example.wh40kapp.R;

import java.util.Date;

public class AppUsageNotifications extends Service {
    private static final String CHANNEL_ID = "app_usage_notification_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final long SECONDS_IN_A_DAY = 24 * 60 * 60 * 1000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkAppUsage();
        return START_STICKY;
    }

    private void checkAppUsage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        long lastAppUsageTime = preferences.getLong("lastAppUsageTime", 0);
        long currentTime = new Date().getTime();
        long daysSinceLastUsage = (currentTime - lastAppUsageTime) / SECONDS_IN_A_DAY;

        if (daysSinceLastUsage >= 14 || true) {
            showNotification();
        }
    }

    private void showNotification() {
        createNotificationChannel();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.cog_mechanicum)
                .setContentTitle("App Usage Reminder")
                .setContentText("It's been 14 days since you last used the app.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "App Usage Channel";
            String channelDescription = "Channel for app usage notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
