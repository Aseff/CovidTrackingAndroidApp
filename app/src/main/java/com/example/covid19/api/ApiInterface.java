package com.example.covid19.api;

import com.example.covid19.Entity.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("news")
    Call<News> getNews(

    // @Query("") String q
    //  @Query("apiKey") String apiKey

    );

}
