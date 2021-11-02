package com.example.devicetracker.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.devicetracker.R;
import com.example.devicetracker.utils.CONSTANTS;
import com.example.devicetracker.utils.CustomLocation;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationService extends Service {
    private DatabaseReference databaseReference;
    CustomLocation customLocation;

    public LocationService() {

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        customLocation = new CustomLocation(this);
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(CONSTANTS.ACTION_START_LOCATION_SERVICE)) {
                    showNotification();
                }
                if (action.equals(CONSTANTS.ACTION_STOP_LOCATION_SERVICE)) {
                    customLocation.stopLocationUpdates();
                    stopForeground(true);
                    stopSelf();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    private void showNotification() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("lastLocation");


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelId");

        ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_tracking_track_svgrepo_com);
        } else {
            builder.setSmallIcon(R.drawable.ic_tracking_track_svgrepo_com);
        }


        builder.setSmallIcon(R.drawable.ic_tracking_track_svgrepo_com)
                .setContentText("This is service notificaion")
                .setContentTitle("Title");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            builder.setChannelId(channelId);
        }


// notificationId is a unique int for each notification that you must define

        Notification notification = builder.build();
        CustomLocation.CustomLocationResults locationResults = new CustomLocation.CustomLocationResults() {
            @Override
            public void gotLocation(Location location) {
                String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                databaseReference.setValue(locationString).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("Location :", locationString);

                    }
                });

            }
        };
        customLocation.getLastLocation(locationResults);

        startForeground(123, notification);

    }

}