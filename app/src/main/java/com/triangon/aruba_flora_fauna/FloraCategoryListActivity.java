package com.triangon.aruba_flora_fauna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.requests.FloraCategoryApi;
import com.triangon.aruba_flora_fauna.requests.ServiceGenerator;
import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;
import com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FloraCategoryListActivity extends BaseActivity  {

    private static final String TAG = "FloraCategoryListActivi";
    private FloraCategoryListViewModel mFloraCategoryListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_category_list);

        mFloraCategoryListViewModel = ViewModelProviders.of(this).get(FloraCategoryListViewModel.class);

        subscribeObservers();

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                testRetrofitRequest();
            }
        });
    }

    private void getFloraCategoriesApi() {
        mFloraCategoryListViewModel.getFloraCategoriesApi();
    }

    private void subscribeObservers() {
        mFloraCategoryListViewModel.getFloraCategories().observe(this, new Observer<List<FloraCategory>>() {
            @Override
            public void onChanged(List<FloraCategory> floraCategories) {
                System.out.println(floraCategories.get(0).getName());
            }
        });
    }

    private void testRetrofitRequest() {
        getFloraCategoriesApi();
    }

    private void testRetrofitRequest1() {
        FloraCategoryApi floraCategoryApi = ServiceGenerator.getFloraCategoryApi();

        Call<FloraCategoryListResponse> responseCall = floraCategoryApi
                .getFloraCategories();

        responseCall.enqueue(new Callback<FloraCategoryListResponse>() {
            @Override
            public void onResponse(Call<FloraCategoryListResponse> call, Response<FloraCategoryListResponse> response) {
                showProgressBar(false);
                Log.d(TAG, "onResponse: " + response.toString());
                if(response.code() == 200) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    List<FloraCategory> floraCategoryList = new ArrayList<>(response.body().getFloraCategories());

                    for(FloraCategory category : floraCategoryList) {
                        System.out.println(category.getCategoryImage().getImageLarge());
                    }
                } else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FloraCategoryListResponse> call, Throwable t) {

            }
        });
    }
}
