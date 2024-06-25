package com.example.devicetracker.Notification;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body JsonObject payLoad);
}

