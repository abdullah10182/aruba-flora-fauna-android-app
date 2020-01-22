package com.triangon.aruba_flora_fauna.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class FloraCategoryListViewModel extends AndroidViewModel {

    private static final String TAG = "CategoryListViewModel";

    public enum ViewState {FLORA_CATEGORIES};
    private MutableLiveData<ViewState> viewState;

    public FloraCategoryListViewModel(@NonNull Application application) {
        super(application);

        init();
    }

    private void init() {
        if(viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(ViewState.FLORA_CATEGORIES);
        }
    }

    public MutableLiveData<ViewState> getViewState() {
        return viewState;
    }
}
