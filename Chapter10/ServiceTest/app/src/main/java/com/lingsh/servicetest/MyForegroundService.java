package com.lingsh.servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;

public class MyForegroundService extends Service {
    private static final String TAG = "MyForegroundService";

    public MyForegroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "创建前台服务");


        final String NOTIFICATION_CHANNEL_ID = "com.lingsh.servicetest";
        String channelName = "My Foreground Service";

        // Android 8.0
        if (SDK_INT >= O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent it, int flags, int startId) {
        return super.onStartCommand(it, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
