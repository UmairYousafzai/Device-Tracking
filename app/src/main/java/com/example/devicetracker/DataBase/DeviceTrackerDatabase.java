package com.example.devicetracker.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.devicetracker.models.LocationSharingUser;
import com.example.devicetracker.models.User;


@Database(entities = {LocationSharingUser.class}, version = 1, exportSchema = false)
public abstract class DeviceTrackerDatabase extends RoomDatabase {

    private static DeviceTrackerDatabase database;
    private static String DatabaseName = "DeviceTrackerDatabase";

    public synchronized  static DeviceTrackerDatabase getInstance(Context context){
        if (database == null) {

            database = Room.databaseBuilder(context.getApplicationContext(), DeviceTrackerDatabase.class, DatabaseName)
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return database;
    }

    public abstract DeviceTrackerDao dao();

}