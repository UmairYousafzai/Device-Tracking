package com.example.devicetracker.Notification;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA4ksVaAY:APA91bFHSqpEN8wo7G1bfRDuRab3qvlw2l7nUsUKJHkYjlHghq6x3s2ttQxrLJnzZFPSUYT5W1BHiw1VXxpqZbTXfa2qp9A7g37_2Bv9TyHjx7M4Q8GTOtZcB7WIT19N_TrBSJ-1fBC_" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body JsonObject payLoad);
}

