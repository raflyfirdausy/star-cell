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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rfl.trn.starr_cell.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String TAG = "FIREBASEMEssagingService";
    public static final String REPLY_ACTION = "reply";
    public static final String KEY_NOTIFICATION_ID = "id_notif";
    public static final int NOTIFICATION_ID = 9;
    public static final String ISI_PESAN = "id";
    public static final String REPLY_KEY = "key";
    public static final String NOTIFICATION_CHANNEL_ID = "notifikasi";
    public String instanceId = null;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        instanceId = s;
        Log.d("@@@@", "onTokenRefresh: " + instanceId);

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference
                .child("admin")
                .child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    databaseReference
                            .child("admin")
                            .child(firebaseUser.getUid())
                            .child("instanceId")
                            .setValue(instanceId);
                } else {
                    databaseReference.child("konter")
                            .child(firebaseUser.getUid())
                            .child("instanceId")
                            .setValue(instanceId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String notificationTitle = null,
                notificationBody = null,
                mesfrom = null,
                userIMg = null;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();


            sendNotificationBelow(notificationTitle, notificationBody);
        }

    }

    public void sendNotificationBelow(String notificationTitle, String notificationBody) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel CHANNEL_ID = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH);
            CHANNEL_ID.setDescription(NOTIFICATION_CHANNEL_ID);
            CHANNEL_ID.enableLights(true);
            CHANNEL_ID.enableVibration(true);
            //CHANNEL_ID.setVibrationPattern(new long[]{100, 200 ,400 ,500,400,300,200,400,100});
            notificationManager.createNotificationChannel(CHANNEL_ID);
        }

               Notification builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_absensi)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationBody))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();



        notificationManager.notify(NOTIFICATION_ID, builder);

    }



}


