package com.example.tejas.library;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BrandService {
    //brand is url api http://globalcity-20.appspot.com/api/v1/brand?areaId=ag9ifmdsb2JhbGNpdHktMjByEQsSBEFyZWEYgICAgM7fhwoM
    @GET("brand")
    Call<BrandResponse> getBrandJSON(@Query("areaId") String areaId);


}
