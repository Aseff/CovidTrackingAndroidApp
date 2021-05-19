package com.example.covid19.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=key should be here"

    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
