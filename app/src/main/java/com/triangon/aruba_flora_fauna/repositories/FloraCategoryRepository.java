package com.triangon.aruba_flora_fauna.repositories;

import android.content.Context;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.persistence.FloraCategoryDao;
import com.triangon.aruba_flora_fauna.persistence.FloraCategoryDatabase;
import com.triangon.aruba_flora_fauna.requests.FloraCategoryApiClient;
import com.triangon.aruba_flora_fauna.requests.executors.AppExecutors;
import com.triangon.aruba_flora_fauna.requests.responses.ApiResponse;
import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;
import com.triangon.aruba_flora_fauna.utils.NetworkBoundResource;
import com.triangon.aruba_flora_fauna.utils.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class FloraCategoryRepository {

    private static FloraCategoryRepository instance;
    private FloraCategoryDao floraCategoryDao;

    public static FloraCategoryRepository FloraCategoryRepository(Context context) {
        if(instance == null) {
            instance = new FloraCategoryRepository(context);
        }

        return instance;
    }

    private FloraCategoryRepository(Context context) {
        floraCategoryDao = FloraCategoryDatabase.getInstance(context).getFloraCategoryDao();
    }

    public LiveData<Resource<List<FloraCategory>>> searchFloraCategoriesApi(final String query) {
        return new NetworkBoundResource<List<FloraCategory>, FloraCategoryListResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull FloraCategoryListResponse item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable List<FloraCategory> data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<FloraCategory>> loadFromDb() {
                return floraCategoryDao.getFloraCategories();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<FloraCategoryListResponse>> createCall() {
                return null;
            }
        }.getAsLiveData();
    }
}
