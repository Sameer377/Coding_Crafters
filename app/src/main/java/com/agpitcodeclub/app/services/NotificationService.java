package com.agpitcodeclub.app.services;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.agpitcodeclub.app.Dashboard;
import com.agpitcodeclub.app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class NotificationService extends FirebaseMessagingService {
    private static final String CHANNEL_ID ="Channel_Id" ;

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived (remoteMessage);

     /*   if(remoteMessage.getData().get(""))

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setSummaryText(messageBody);
        notiStyle.bigPicture(picture);*/
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.logo);
        Intent intent = new Intent (this, Dashboard.class);
        NotificationManager manager = (NotificationManager) getSystemService (Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt();
        createNotificationChannel(manager);
        intent.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent1 = PendingIntent.getActivities(this, 0, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

//      if(remoteMessage.getData().get("imageUrl" )==null) {

       /*Notification notification ;
                notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("messege"))
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(icon)
                        .setAutoCancel(true)
                        .setContentIntent(intent1)
                        .build();
                         manager.notify(notificationId, notification);


        }else {*/


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setLargeIcon(icon)/*Notification icon image*/
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setAutoCancel(true)
                    .setContentIntent(intent1)
                    .setContentText(remoteMessage.getData().get("messege"));
                    if(remoteMessage.getData().get("imgURL")!=null) {
                        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(getBitmapfromUrl(remoteMessage.getData().get("imgURL"))));/*Notification with Image*/
                    }

            manager.notify(notificationId, notificationBuilder.build());
//        }
   }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        }
        catch (Exception e) {
            Log.e("awesome","Error in getting notification image: " + e.getLocalizedMessage());
            return null;
        }
    }

    /*
    * NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
notiStyle.setSummaryText(messageBody);
notiStyle.bigPicture(picture);

Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher)
            .setLargeIcon(bigIcon)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setStyle(notiStyle); code here

NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
notificationManager.notify(0, notificationBuilder.build());
    * */

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager manager) {
        NotificationChannel channel = null;

            channel = new NotificationChannel(CHANNEL_ID, "channelName", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My descrition");
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);

            manager.createNotificationChannel(channel);
    }
}
