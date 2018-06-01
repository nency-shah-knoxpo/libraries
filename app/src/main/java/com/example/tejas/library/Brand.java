package com.example.tejas.library;

import java.util.UUID;

public class Brand {

   private UUID mBrandId;
   private String mBrandName;

   public Brand(String brandName){
       mBrandId = UUID.randomUUID();
       this.mBrandName = brandName;
   }

    public UUID getmBrandId() {
        return mBrandId;
    }



    public String getBrandName() {
        return mBrandName;
    }

    public void setmBrandName(String mBrandName) {
        this.mBrandName = mBrandName;
    }


  /*  @Override
    public String toString() {
        return mBrandName;
    }*/
}
