package com.triangon.aruba_flora_fauna.repositories;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.requests.FloraCategoryApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FloraCategoryRepository {

    private static FloraCategoryRepository instance;
    private FloraCategoryApiClient mFloraCategoryApiClient;

    public static FloraCategoryRepository getInstance() {
        if(instance == null) {
            instance = new FloraCategoryRepository();
        }
        return instance;
    }

    private FloraCategoryRepository() {
        mFloraCategoryApiClient = FloraCategoryApiClient.getInstance();
    }

    public LiveData<List<FloraCategory>> getFloraCategories() {
        return mFloraCategoryApiClient.getFloraCategories();
    }

   public void getFloraCategoriesApi() {
        mFloraCategoryApiClient.getFloraCategoriesApi();
   }

}
