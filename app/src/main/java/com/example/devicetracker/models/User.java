package com.example.devicetracker.models;

import java.util.ArrayList;
import java.util.List;

public class User {

    protected String id,email,password,lastLocation,userName;
    private List<AssignedUser> assignedUser;
    public User() {
    }

    public User(String id, String email, String password, String lastLocation, String userName, List<AssignedUser> assignedUser) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastLocation = lastLocation;
        this.userName = userName;
        this.assignedUser = assignedUser;
    }

    public User(String id, String email, String password, String lastLocation, String userName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastLocation = lastLocation;
        this.userName = userName;
    }

    public List<AssignedUser> getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(List<AssignedUser> assignedUser) {
        this.assignedUser = assignedUser;
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
