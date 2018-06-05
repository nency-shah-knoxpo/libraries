package com.example.tejas.library;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BrandResponse {

    @SerializedName("brands")
    List<Brand> mBrands;

}
