package com.example.tejas.library;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class DetailBrandActivity extends SingleFragmentActivity {


    private static  final String TAG = DetailBrandActivity.class.getSimpleName();
    private static final String EXTRA_PRODUCT = TAG + " EXTRA_PRODUCT";
    public static Intent newIntent(Context context,Brand brand){

        Intent i = new Intent(context,DetailBrandActivity.class);
        i.putExtra(EXTRA_PRODUCT,brand);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        Brand brand = getIntent().getParcelableExtra(EXTRA_PRODUCT);
        return DetailBrandFragment.newInstance(brand);
    }
}
