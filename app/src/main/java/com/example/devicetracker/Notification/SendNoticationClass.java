package com.example.devicetracker.Notification;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNoticationClass {

    private APIService apiService;
    private String refreshToken;




    public void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                refreshToken=task.getResult();
                Token token= new Token(refreshToken);
                FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
            }
        });

    }
    public void sendNotifications(String usertoken, String senderEmail,String title, String message,Context context) {
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
//        Data data = new Data(title, message);
//        NotificationSender sender = new NotificationSender(data, usertoken);

        JsonObject payload = new JsonObject();
        payload.addProperty("to", usertoken);
        // compose data payload here
        JsonObject data = new JsonObject();
        data.addProperty("title", title);
        data.addProperty("message", message);
        data.addProperty("email",senderEmail);
        // add data payload
        payload.add("data", data);
        apiService.sendNotifcation(payload).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}
