package com.example.chattapp.fragments;

import com.example.chattapp.Notifications.MyResponse;
import com.example.chattapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAAowXMzE:APA91bE3wsRvUuPES-sUBll1txwzL9JngjskBHblu_GUD5a5WEtYvO3gYySJtpWSDBps3vnGSJQUUwi2ePkiLfyvh4xFTv4uLxdnEtvB0oyyTAcrgfvPsHD9NFKzNZGR2FVBqbYAm82q"

    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
