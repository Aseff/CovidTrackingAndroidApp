package com.example.covid19.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAo5BXs64:APA91bEKnZW8qjYksbe3OsY7gAcxyG4a_tQsGcaD62YSNu4Vk9DjO6_hXk_vTeCz8gfiQ3IdeVNCDdyf3o8WdQlFpJFcFL8Ha26XCTKupgv_71MuVt9RU7S1c9byKRRAwYu61iQ20GK_"

    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
