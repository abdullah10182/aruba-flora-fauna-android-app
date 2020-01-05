package com.triangon.aruba_flora_fauna.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.graphics.Rect;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.adapters.FloraCategoryRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.GridLayoutItemDecoration;
import com.triangon.aruba_flora_fauna.adapters.OnFloraCategoryListener;
import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.requests.FloraCategoryApi;
import com.triangon.aruba_flora_fauna.requests.ServiceGenerator;
import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;
import com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FloraCategoryListActivity extends BaseActivity implements OnFloraCategoryListener {

    private static final String TAG = "FloraCategoryListActivity";

    private FloraCategoryListViewModel mFloraCategoryListViewModel;
    private RecyclerView mRecyclerView;
    private FloraCategoryRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_category_list);

        mRecyclerView = findViewById(R.id.rv_flora_category_list);
        mFloraCategoryListViewModel = ViewModelProviders.of(this).get(FloraCategoryListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        testRetrofitRequest();

    }

    private void getFloraCategoriesApi() {
        mFloraCategoryListViewModel.getFloraCategoriesApi();
    }

    private void subscribeObservers() {
        showProgressBar(true);
        mFloraCategoryListViewModel.getFloraCategories().observe(this, new Observer<List<FloraCategory>>() {
            @Override
            public void onChanged(List<FloraCategory> floraCategories) {
                if(floraCategories != null) {
                    System.out.println(floraCategories.get(0).getName());
                    mAdapter.setFloraCategories(floraCategories);
                    showProgressBar(false);
                }
            }
        });
    }

    private void initRecyclerView() {
        int columnCount = 2;
        mAdapter = new FloraCategoryRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),columnCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new GridLayoutItemDecoration(columnCount, 8, true));
    }

    private void testRetrofitRequest() {
        getFloraCategoriesApi();
    }

//    private void testRetrofitRequest1() {
//        FloraCategoryApi floraCategoryApi = ServiceGenerator.getFloraCategoryApi();
//
//        Call<FloraCategoryListResponse> responseCall = floraCategoryApi
//                .getFloraCategories();
//
//        responseCall.enqueue(new Callback<FloraCategoryListResponse>() {
//            @Override
//            public void onResponse(Call<FloraCategoryListResponse> call, Response<FloraCategoryListResponse> response) {
//                showProgressBar(false);
//                Log.d(TAG, "onResponse: " + response.toString());
//                if(response.code() == 200) {
//                    Log.d(TAG, "onResponse: " + response.body().toString());
//                    List<FloraCategory> floraCategoryList = new ArrayList<>(response.body().getFloraCategories());
//
//                    for(FloraCategory category : floraCategoryList) {
//                        System.out.println(category.getCategoryImage().getImageLarge());
//                    }
//                } else {
//                    try {
//                        Log.d(TAG, "onResponse: " + response.errorBody().string() );
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FloraCategoryListResponse> call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    public void onFloraCategoryClick(int position) {
        System.out.println(position);
        String text = mAdapter.getFloraCategories().get(position).getName();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
