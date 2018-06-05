package com.example.tejas.library;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class Brand implements Parcelable {

    protected Brand(Parcel in) {
        mBrandName = in.readString();
        mBrandLogo = in.readString();
    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel in) {
            return new Brand(in);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mBrandName);
        dest.writeString(mBrandLogo);
    }



    private UUID mBrandId;

   @SerializedName("brandName")
   @Expose
   private String mBrandName;

   @SerializedName("brandLogo")
   @Expose
   private String mBrandLogo;



   public Brand(String brandName){
       mBrandId = UUID.randomUUID();
       this.mBrandName = brandName;
   }



    public UUID getBrandId() {
        return mBrandId;
    }



    public String getBrandName() {
        return mBrandName;
    }

    public void setBrandName(String brandName) {
        mBrandName = brandName;
    }

    public String getBrandLogo() {
        return mBrandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        mBrandLogo = brandLogo;
    }


}
