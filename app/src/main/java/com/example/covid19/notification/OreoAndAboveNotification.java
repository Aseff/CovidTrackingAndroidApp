package com.example.covid19.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.covid19.Activities.ui.notifications.NotificationsFragment;
import com.example.covid19.Fragments.NotifFragment;

public class OreoAndAboveNotification extends ContextWrapper {
    private static final String ID = "some_id";
    private static final String NAME = "FirebaseApp";
    private NotificationManager notificationManager;

    public OreoAndAboveNotification(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
            createChannel();
        }

    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel=new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);


    }
    public NotificationManager  getManager(){
        if(notificationManager==null){
            notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOnNotifications(String title, String body, PendingIntent intent, Uri soundUri, String icon){
        return  new Notification.Builder(getApplicationContext(),ID)
                .setContentIntent(intent)
                .setContentText(body)
                .setContentTitle(title)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon));
    }

}
