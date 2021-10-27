package com.example.devicetracker.models;

import java.util.List;

public class AssignedUser extends User {
    public AssignedUser() {
    }

    public AssignedUser(String id, String email, String password, String lastLocation, String userName) {
        super(id, email, password, lastLocation, userName);
    }
}
