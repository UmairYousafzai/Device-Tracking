package com.example.devicetracker.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.devicetracker.R;
import com.example.devicetracker.utils.CustomLocation;
import com.google.android.gms.location.LocationResult;

public class LocationService extends Service {
    CustomLocation customLocation= new CustomLocation(this);
    public LocationService() {
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
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

        CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
            @Override
            public void gotLocation(Location location) {

            }
        };
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"channelId");

        builder.setSmallIcon(R.drawable.ic_tracking_track_svgrepo_com)
                .setContentText("This is service notificaion")
                .setContentTitle("Title");
        Notification notification=builder.build();

        startForeground(123,notification);

    }

}