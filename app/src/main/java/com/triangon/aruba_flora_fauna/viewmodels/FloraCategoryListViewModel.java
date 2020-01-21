package com.triangon.aruba_flora_fauna.viewmodels;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.repositories.FloraCategoryRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FloraCategoryListViewModel extends ViewModel {

    private FloraCategoryRepository mFloraCategoryRepository;
    private boolean mDidRetrieveCategories;

    public FloraCategoryListViewModel() {
        mFloraCategoryRepository = FloraCategoryRepository.getInstance();
        mFloraCategoryRepository.getFloraCategoriesApi();
        mDidRetrieveCategories = false;
    }

    public LiveData<List<FloraCategory>> getFloraCategories() {
        return mFloraCategoryRepository.getFloraCategories();
    }

    public void getFloraCategoriesApi() {
        mFloraCategoryRepository.getFloraCategoriesApi();
    }

    public LiveData<Boolean> isCategoryRequestTimedOut() {
        return mFloraCategoryRepository.isCategoryRequestTimedOut();
    }

    public boolean isDidRetrieveCategories() {
        return mDidRetrieveCategories;
    }

    public void setDidRetrieveCategories(boolean didRetrieveCategories) {
        this.mDidRetrieveCategories = didRetrieveCategories;
    }
}
