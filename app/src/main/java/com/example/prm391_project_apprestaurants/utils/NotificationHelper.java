package com.example.prm391_project_apprestaurants.utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.prm391_project_apprestaurants.MainActivity;
import com.example.prm391_project_apprestaurants.R;

import java.util.Objects;

public class NotificationHelper {
    public static final String CHANNEL_ID = "default_channel";
    private static final int NOTIFICATION_ID = 1001;

    public static void showNotification(Context context, String title, String message, @Nullable PendingIntent pendingIntent, int priority) {
        try {
            createNotificationChannel(context);

            // Tạo notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.restaurant)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(priority)
                    .setAutoCancel(true);
            if (pendingIntent != null) {
                builder.setContentIntent(pendingIntent);
            }
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (Build.VERSION.SDK_INT < 33 || ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        }catch (Exception e){
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
    }
    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default Channel";
            String description = "Kênh thông báo mặc định";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
