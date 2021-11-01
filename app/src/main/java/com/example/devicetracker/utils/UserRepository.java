package com.example.devicetracker.utils;


import android.app.Application;
import android.icu.lang.UScript;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.example.devicetracker.DataBase.DeviceTrackerDao;
import com.example.devicetracker.DataBase.DeviceTrackerDatabase;
import com.example.devicetracker.models.LocationSharingUser;
import com.example.devicetracker.models.User;


import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {

    private DeviceTrackerDao mDao;
    Application application;
    private final LiveData<List<LocationSharingUser>> allUsers;
    private Executor executor= Executors.newSingleThreadExecutor();
    private Handler handler= new Handler(Looper.getMainLooper());



    public UserRepository(Application application) {
        DeviceTrackerDatabase database = DeviceTrackerDatabase.getInstance(application);
        mDao = database.dao();
        this.application = application;
        allUsers = mDao.getAllUser();

    }

    public void insertUser(LocationSharingUser user) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.insertUser(user);
            }
        });      }


    public void deleteAllUser( )
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteAllUser();
            }
        });
    }
    public void deleteUser( LocationSharingUser user)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDao.deleteUser(user);
            }
        });
    }

    public LiveData<List<LocationSharingUser>> getAllUser()
    {
        return allUsers;
    }


}

