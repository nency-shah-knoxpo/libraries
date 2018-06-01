package com.example.tejas.library;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BrandService {

    @GET("brand")
    Call<String> getBrandJSON(@Query("areaId") String areaId);
}
