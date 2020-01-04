package com.triangon.aruba_flora_fauna.viewmodels;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.repositories.FloraCategoryRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FloraCategoryListViewModel extends ViewModel {

    private FloraCategoryRepository mFloraCategoryRepository;

    public FloraCategoryListViewModel() {
        mFloraCategoryRepository = FloraCategoryRepository.getInstance();
    }

    public LiveData<List<FloraCategory>> getFloraCategories() {
        return mFloraCategoryRepository.getFloraCategories();
    }

    public void getFloraCategoriesApi() {
        mFloraCategoryRepository.getFloraCategoriesApi();
    }
}
