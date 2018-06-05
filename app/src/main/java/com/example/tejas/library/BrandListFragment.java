package com.example.tejas.library;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = BrandListFragment.class.getSimpleName();

    private List<Brand> mBrands;
    private RecyclerView mRecyclerView;
    private ProgressBar mFetchingPB;
    private LinearLayout mEmptyWrapperLL;
    private Button mRetryBtn;

    private boolean mIsFetching;

    private FetchItemsTask mItemsTask;

    @Override
    public void onDestroyView() {
        mItemsTask.cancel(true);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brand_list, container, false);

        init(v);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();

        mRetryBtn.setOnClickListener(this);

        updateUI();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(isOnline()) {
            startFetching();
        }
        else
        {
            Toast.makeText(getActivity(),R.string.toast_no_wifi,Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileDataOn = networkInfo.isConnected();
        return (isWifiConn || isMobileDataOn);

    }
    private void startFetching() {
        /*mItemsTask = new FetchItemsTask();
        mItemsTask.execute();*/

        mIsFetching = true;
        updateUI();

        BrandService brandService=RetroFitLibrary.getRetrofitInstance().create(BrandService.class);
        Call<BrandResponse> call = brandService.getBrandJSON("ag9ifmdsb2JhbGNpdHktMjByEQsSBEFyZWEYgICAgM7fhwoM");
        call.enqueue(new Callback<BrandResponse>() {

            @Override
            public void onResponse(Call<BrandResponse> call, Response<BrandResponse> response) {
              mBrands =   response.body().mBrands;
              setupAdapter();

              mIsFetching = false;

              updateUI();
              Log.d(TAG, "SUCCESS");
            }

            @Override
            public void onFailure(Call<BrandResponse> call, Throwable t) {
                mIsFetching = false;
                updateUI();
            }
        });

    }

    private void init(View v) {
        mIsFetching = false;
        mBrands = new ArrayList<>();
        mRecyclerView = v.findViewById(R.id.rv_brand);
        mFetchingPB = v.findViewById(R.id.pb_fetching);
        mEmptyWrapperLL = v.findViewById(R.id.ll_empty_wrapper);
        mRetryBtn = v.findViewById(R.id.btn_retry);
    }

    private void updateUI() {
        mFetchingPB.setVisibility(mIsFetching ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(mIsFetching ? View.GONE : View.VISIBLE);
        mEmptyWrapperLL.setVisibility(!mIsFetching && mBrands.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new BrandAdapter(mBrands));

        }
    }

    @Override
    public void onClick(View v) {
        if(isOnline()){
        startFetching();
        }
        else
        {
            Toast.makeText(getActivity(),R.string.toast_no_wifi,Toast.LENGTH_SHORT).show();
        }
    }

    public class BrandHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private Brand mBrand;
        private TextView mBrandNameTV;
        private ImageView mBrandLogoIV;

        public BrandHolder(View itemView) {
            super(itemView);
            mBrandNameTV = itemView.findViewById(R.id.txt_brand_name);
            mBrandLogoIV = itemView.findViewById(R.id.img_brand_logo);
            itemView.setOnClickListener(this);
        }

        public void bindBrand(Brand brand) {
            mBrand = brand;
            mBrandNameTV.setText(brand.getBrandName());
            GlideApp.with(BrandListFragment.this).
                    load(brand.getBrandLogo()).
                    centerCrop().
                    into(mBrandLogoIV);

        }

        @Override
        public void onClick(View v) {

            Brand brand = mBrand;
            Intent i = DetailBrandActivity.newIntent(getActivity(),brand);
            startActivity(i);

        }
    }


    private class BrandAdapter extends RecyclerView.Adapter<BrandHolder> {

        public BrandAdapter(List<Brand> brands) {
            mBrands = brands;
        }

        @Override
        public BrandHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_brand, parent, false);
            return new BrandHolder(view);
        }

        @Override
        public void onBindViewHolder(BrandHolder holder, int position) {
            Brand brand = mBrands.get(position);
            holder.bindBrand(brand);
        }

        @Override
        public int getItemCount() {
            return mBrands.size();
        }

        public void setBrands(List<Brand> brands) {
            mBrands = brands;
        }

        public void removeItem(int position) {
            mBrands.remove(position);
            notifyItemRemoved(position);
        }

        public void restoreItem(Brand brand, int position) {
            mBrands.add(position, brand);
            notifyItemInserted(position);
        }
    }


    private class FetchItemsTask extends AsyncTask<Void, Void, List<Brand>> {

        @Override
        protected void onPreExecute() {
            mIsFetching = true;
            updateUI();
        }

        @Override
        protected List<Brand> doInBackground(Void... params) {
            return new Fetcher().fetchItems();

        }

        @Override
        protected void onPostExecute(List<Brand> items) {
            mBrands = items;
            setupAdapter();
            mIsFetching = false;
            updateUI();

        }
    }
}
