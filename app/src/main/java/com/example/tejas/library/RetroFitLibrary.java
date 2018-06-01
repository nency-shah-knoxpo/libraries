package com.example.tejas.library;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroFitLibrary
{
public static Retrofit getRetroObject() {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://globalcity-20.appspot.com/api/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    return retrofit;
}
}
