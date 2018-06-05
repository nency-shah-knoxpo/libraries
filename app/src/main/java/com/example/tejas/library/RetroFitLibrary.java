package com.example.tejas.library;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitLibrary
{
public static Retrofit getRetrofitInstance() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://globalcity-20.appspot.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    return retrofit;
}
}
