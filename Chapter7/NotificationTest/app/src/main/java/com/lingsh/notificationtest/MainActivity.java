package com.lingsh.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String NOTI_TITLE = "This is content title";
    private static final String NOTI_CONTENT = "This is content text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendNotification = (Button) findViewById(R.id.send_notification);
        sendNotification.setOnClickListener(this);

        Button sendNotificationSound = (Button) findViewById(R.id.send_notification_with_sound);
        sendNotificationSound.setOnClickListener(this);

        Button sendNotificationVibrate = (Button) findViewById(R.id.send_notification_with_vibrate);
        sendNotificationVibrate.setOnClickListener(this);

        Button sendNotificationLights = (Button) findViewById(R.id.send_notification_with_lights);
        sendNotificationLights.setOnClickListener(this);

        Button sendNotificationDefault = (Button) findViewById(R.id.send_notification_with_default);
        sendNotificationDefault.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notification:
                baseNotification();
                break;
            case R.id.send_notification_with_sound:
                soundNotification();
                break;
            case R.id.send_notification_with_vibrate:
                vibrateNotification();
                break;
            case R.id.send_notification_with_lights:
                lightsNotification();
                break;
            case R.id.send_notification_with_default:
                defaultNotification();
                break;
            default:
                break;
        }
    }

    private void baseNotification() {
        String channelId = "my_channel_id_01";
        runNotification(getBaseBuilder(channelId).build());
    }

    private void soundNotification() {
        String channelId = "my_channel_id_02";
        Uri sound = Uri.fromFile(new File("/system/media/audio/ringtones/Luna.ogg"));
        runNotification(getBaseBuilder(channelId).setSound(sound).build());
    }

    private void vibrateNotification() {
        String channelId = "my_channel_id_03";
        long[] pattern = {0, 1000, 1000, 1000};
        runNotification(getBaseBuilder(channelId).setVibrate(pattern).build());
    }

    private void lightsNotification() {
        String channelId = "my_channel_id_04";
        runNotification(getBaseBuilder(channelId).setLights(Color.GREEN, 1000, 1000).build());
    }

    private void defaultNotification() {
        String channelId = "my_channel_id_05";
        runNotification(getBaseBuilder(channelId).setDefaults(NotificationCompat.DEFAULT_ALL).build());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, NotificationActivity.class);
        return PendingIntent.getActivity(this, 0, intent, 0);
    }

    private NotificationCompat.Builder getBaseBuilder(String channelId) {
        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle(NOTI_TITLE)
                .setContentText(NOTI_CONTENT)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void runNotification(Notification notification) {
        NotificationManager manager = getNotificationManager();

        if (manager != null) {
            manager.notify(1, notification);
        } else {
            Log.d(TAG, "manager is null");
        }
    }
}
