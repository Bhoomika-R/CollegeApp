package com.example.mycollegeapp.ui.chatSection.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.mycollegeapp.ui.chatSection.MessageActivity;
import com.example.mycollegeapp.ui.profile.ProfileSharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private ProfileSharedPreferences spProfile;
    private HashMap<String,String> profileData;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        spProfile = new ProfileSharedPreferences(getApplicationContext());
        profileData=spProfile.getUserProfile();
        String sented = remoteMessage.getData().get("sented");
        String user = remoteMessage.getData().get("user");

        SharedPreferences preferences = getSharedPreferences("PREFS",MODE_PRIVATE);
        String currentUser = preferences.getString("currentUser","none");

       FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

       if(firebaseUser!=null && sented.equals(firebaseUser.getUid())){
           if(!currentUser.equals(user)) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   sendOreoNotification(remoteMessage);
               } else {
                   sendNotification(remoteMessage);
               }
           }
       }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        if (s != null) {
            updateToken(s);
        }
    }

    private void updateToken(String refreshToken) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat section").child("tokens").child(profileData.get(ProfileSharedPreferences.KEY_BRANCH));
            Token token = new Token(refreshToken);
            reference.child(firebaseUser.getUid()).setValue(token);
        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title,body,pendingIntent,
                defaultSound,icon);

        int i=0;
        if(j>0){
            i=j;
        }

        oreoNotification.getManager().notify(i,builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage){

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

         RemoteMessage.Notification notification = remoteMessage.getNotification();
         int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i=0;
        if(j>0){
            i=j;
        }

        noti.notify(i,builder.build());
    }
}
