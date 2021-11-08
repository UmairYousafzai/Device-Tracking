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
                    "Authorization:key=AAAAO4iaNNE:APA91bH5eKtvcPx-VukMMpiRc6IT6SH9gZdwThK6VNsvZuoPUeEhNJPZucvDCv6Bt5TU64fYYkpdAc9C5erjUaZF8QgwRWpRTMBqOOKjOBHy-2-_QYZ9b-Cp_AVcg9mfcXe_ldfmWD6i" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body JsonObject payLoad);
}

