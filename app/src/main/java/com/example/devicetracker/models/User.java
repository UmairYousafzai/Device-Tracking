package com.example.devicetracker.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

public class User {


    protected String id;
    protected String email;
    protected String password;
    protected String lastLocation;
    protected String userName;
    private boolean requestAccepted;
    private Map<String,String> assignedUsers = new HashMap<>();
    public User() {
    }

    public User(String id, String email, String password, String lastLocation, String userName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastLocation = lastLocation;
        this.userName = userName;
    }

    public User(String id, String email, String password, String lastLocation, String userName, boolean requestAccepted, Map<String, String> assignedUsers) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastLocation = lastLocation;
        this.userName = userName;
        this.requestAccepted = requestAccepted;
        this.assignedUsers = assignedUsers;
    }

    public boolean isRequestAccepted() {
        return requestAccepted;
    }

    public void setRequestAccepted(boolean requestAccepted) {
        this.requestAccepted = requestAccepted;
    }

    public Map<String, String> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(Map<String, String> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
