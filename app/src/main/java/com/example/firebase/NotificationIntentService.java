package com.example.firebase;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class NotificationIntentService extends IntentService {
    public NotificationIntentService() {
        super("Broadcaster");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Attendance")
                .setSmallIcon(R.mipmap.attendance_icon_round)
                .setContentTitle("Falak Attendance")
                .setContentText("Don't forget to log your attendance today!")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Intent current = new Intent(this, SplashScreen.class);
        PendingIntent pending = PendingIntent.getActivity(this, 16, current, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending);
        int notification_id = 16;
        manager.notify(notification_id, builder.build());
    }
}
