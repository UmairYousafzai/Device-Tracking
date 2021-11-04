package com.example.devicetracker.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.devicetracker.MainActivity;
import com.example.devicetracker.R;
import com.example.devicetracker.models.LocationSharingUser;
import com.example.devicetracker.models.User;
import com.example.devicetracker.utils.UserRepository;
import com.example.devicetracker.utils.UserViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private UserRepository userRepository;
    private LocationSharingUser user = new LocationSharingUser();
    private String senderID;
    private DatabaseReference databaseReference;


    NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String senderEmail = remoteMessage.getData().get("email");
        userRepository = new UserRepository(getApplication());

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.orderByChild("email").equalTo(senderEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    user = dataSnapshot.getValue(LocationSharingUser.class);
                    addUserToDb(user);
                    createNotification(title,message);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Add User: ", " " + error.getMessage());

            }
        });



    }

    public void createNotification(String title, String message)
    {

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);


        Bundle bundle = new Bundle();
        bundle.putString("recevierID",user.getId());
        PendingIntent pendingIntent;
        if (title.equals("Message")) {
            pendingIntent = new NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.chatFragment)
                    .setArguments( bundle)
                    .createPendingIntent();
        }
        else {
            pendingIntent = new NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.location_request)
                    .createPendingIntent();
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_tracking_track_svgrepo_com);
        } else {
            builder.setSmallIcon(R.drawable.ic_tracking_track_svgrepo_com);
        }


        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);

        mNotificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }


// notificationId is a unique int for each notification that you must define
        mNotificationManager.notify(100, builder.build());
    }
    public void addUserToDb(LocationSharingUser senderUser) {

        if (!userRepository.isUserExists(senderUser.getEmail())) {


            userRepository.insertUser(senderUser);

        }


    }

}
