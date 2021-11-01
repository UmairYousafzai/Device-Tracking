package com.example.devicetracker.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.devicetracker.models.LocationSharingUser;
import com.example.devicetracker.models.User;

import java.util.List;

@androidx.room.Dao
public interface DeviceTrackerDao {


                /*
    //
    // *******User  Queries*******
    //
     */


    @Insert
    void insertUser(LocationSharingUser user);

    @Delete
    void deleteUser(LocationSharingUser user);

    @Query("Delete from LocationSharingUser")
    void deleteAllUser();

    @Query("Select *From LocationSharingUser")
    LiveData<List<LocationSharingUser>> getAllUser();

}