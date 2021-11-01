package com.example.devicetracker.utils;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.devicetracker.models.LocationSharingUser;
import com.example.devicetracker.models.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private final LiveData<List<LocationSharingUser>> allUsers;


    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = userRepository.getAllUser();

    }


    public void insertUser(LocationSharingUser user)
    {
        userRepository.insertUser(user);
    }

    public void deleteAllUsers()
    {
        userRepository.deleteAllUser();
    }


    public void deleteUser(LocationSharingUser user)
    {
        userRepository.deleteUser(user);
    }
    public LiveData<List<LocationSharingUser>> getAllUser( )
    {
        return userRepository.getAllUser();
    }


}

