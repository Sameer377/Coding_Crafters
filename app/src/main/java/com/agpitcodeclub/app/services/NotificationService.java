package com.agpitcodeclub.app.services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;

import androidx.core.app.NotificationCompat;

import com.agpitcodeclub.app.MainActivity;
import com.agpitcodeclub.app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NotificationService extends FirebaseMessagingService {
    private static final String CHANNEL_ID ="Channel_Id" ;

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived (remoteMessage);
        Intent intent = new Intent (this, MainActivity.class);
        NotificationManager manager = (NotificationManager) getSystemService (Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();
        createNotificationChannel(manager);
        intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent1 = PendingIntent.getActivities ( this,0, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT);
        Notification notification ;
                notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setSmallIcon(R.drawable.logo)
                        .setAutoCancel(true)
                        .setContentIntent(intent1)
                        .build();
            manager.notify(notificationId, notification);
    }

    @SuppressLint("NewApi")
    private void createNotificationChannel(NotificationManager manager) {
        NotificationChannel channel = null;
            channel = new NotificationChannel(CHANNEL_ID, "channelName", NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription("My descrition");
            channel.enableLights(true);
            channel.setLightColor(R.color.frag_bg);

        manager.createNotificationChannel(channel);
    }
}
