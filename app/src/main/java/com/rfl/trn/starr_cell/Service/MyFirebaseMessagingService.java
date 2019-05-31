package com.rfl.trn.starr_cell.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mifmif.common.regex.Main;
import com.rfl.trn.starr_cell.Activity.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "FIREBASEMEssagingService";
    public static final String REPLY_ACTION = "reply";
    public static final String KEY_NOTIFICATION_ID = "id_notif";
    public static final int NOTIFICATION_ID = 1;
    public static final String ISI_PESAN = "id";
    public static final String REPLY_KEY = "key";
    public static final String NOTIFICATION_CHANNEL_ID = "ChatsApp";
    public String instanceId = null;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        instanceId = s;

        Log.d("@@@@", "onTokenRefresh: " + instanceId);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            if (firebaseUser != null) {
                FirebaseDatabase.getInstance().getReference()
                        .child("USER")
                        .child(firebaseUser.getUid())
                        .child("instanceId")
                        .setValue(instanceId);
            }
        } catch (NullPointerException e) {
            Crashlytics.logException(e);
        } catch (Exception e) {
            Crashlytics.logException(e);
        } catch (Throwable e) {
            Crashlytics.logException(e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String notificationTitle = null,
                notificationBody = null,
                mesfrom = null,
                userIMg = null;

        // Check if message contains a notification payload.
        if (remoteMessage.getData().size() > 0) {
            // Device to device
            try {


                notificationTitle = remoteMessage.getData().get("title");
                notificationBody = remoteMessage.getData().get("body");
                mesfrom = remoteMessage.getData().get("user_id");
                userIMg = remoteMessage.getData().get("icon");

               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //TODO :: Notifikasi android M keatas
                    sendNotificationM(notificationTitle, notificationBody, mesfrom, userIMg);
                } else {
                    //TODO :: Notifikasi android selain M,N,P
                    sendNotificationBelow(notificationTitle, notificationBody, mesfrom, userIMg);
                }
            } catch (NullPointerException e) {

            } catch (Exception e) {
                Crashlytics.logException(e);
            } catch (Throwable e) {
                Crashlytics.logException(e);
            }

        } else if (remoteMessage.getNotification() != null) {
            //FCM
        } else {

        }

    }



    public void sendNotificationBelow(String notificationTitle, String notificationBody, String mesfrom, String userIMg) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("uid", mesfrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //load image
        Bitmap bmp = null;
        try {
            InputStream inputStream = new URL(userIMg).openStream();
            bmp = BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Notification notificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .build();


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder);
    }

    public void sendNotificationM(String notificationTitle, String notificationBody, String mesfrom, String userIMg) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("uid", mesfrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //load image
        Bitmap bmp = null;
        try {
            InputStream inputStream = new URL(userIMg).openStream();
            bmp = BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        long time = new Date().getTime();


        Notification notificationBuilder = new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationBody))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .build();


        notificationManager.notify(NOTIFICATION_ID, notificationBuilder);
    }


}
