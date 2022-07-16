package com.example.mycollegeapp.ui.chatSection.Notifications;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

public class OreoNotification extends ContextWrapper {

    private static final String CHANNEL_ID = "com.example.mycollegeapp";
    private static final String CHANNEL_NAME = "chat";

    private NotificationManager notificationManager;

    Uri defaultSoundUri;

    public OreoNotification(Context base) {
        super(base);

        defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
   private void createChannel(){

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
       /* if (defaultSoundUri != null) {
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            channel.setSound(defaultSoundUri, att);
        }*/

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotification(String title, String body,
                                                    PendingIntent pendingIntent, Uri soundUri , String icon){
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(Integer.parseInt(icon))
                .setSound(soundUri)
                .setAutoCancel(true);
    }

}
