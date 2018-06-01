package com.example.tejas.library;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandListFragment extends Fragment implements View.OnClickListener {
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
        startFetching();
    }

    private void startFetching() {
        /*mItemsTask = new FetchItemsTask();
        mItemsTask.execute();*/

        BrandService brandService=RetroFitLibrary.getRetroObject().create(BrandService.class);
        Call<String> call = brandService.getBrandJSON("ag9ifmdsb2JhbGNpdHktMjByEQsSBEFyZWEYgICAgM7fhwoM");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String brand = response.body();
                Log.d("TAG",brand);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
        startFetching();
    }

    public class BrandHolder extends RecyclerView.ViewHolder {

        private TextView mBrandName;

        public BrandHolder(View itemView) {
            super(itemView);
            mBrandName = itemView.findViewById(R.id.txt_brand_name);
        }

        public void bindBrand(Brand brand) {
            mBrandName.setText(brand.getBrandName());
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
