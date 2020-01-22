package com.triangon.aruba_flora_fauna.viewmodels;

import android.app.Application;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.repositories.FloraCategoryRepository;
import com.triangon.aruba_flora_fauna.utils.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class FloraCategoryListViewModel extends AndroidViewModel {

    private static final String TAG = "CategoryListViewModel";

    public enum ViewState {FLORA_CATEGORIES};
    private MutableLiveData<ViewState> viewState;
    private MediatorLiveData<Resource<List<FloraCategory>>> floraCategories = new MediatorLiveData<>();

    private FloraCategoryRepository floraCategoryRepository;

    public FloraCategoryListViewModel(@NonNull Application application) {
        super(application);
        floraCategoryRepository = FloraCategoryRepository.getInstance(application);
        init();
    }

    private void init() {
        if(viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.FLORA_CATEGORIES);
        }
    }

    public LiveData<ViewState> getViewState() {
        return viewState;
    }

    public LiveData<Resource<List<FloraCategory>>> getFloraCategories() {
        return floraCategories;
    }

    public void getFloraCategoriesApi() {
        final LiveData<Resource<List<FloraCategory>>> repositorySource = floraCategoryRepository.getFloraCategoriesApi(/*param*/);

        floraCategories.addSource(repositorySource, new Observer<Resource<List<FloraCategory>>>() {
            @Override
            public void onChanged(Resource<List<FloraCategory>> listResource) {
                floraCategories.setValue(listResource);
            }
        });
    }
}
