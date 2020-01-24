package com.triangon.aruba_flora_fauna.viewmodels;

import android.app.Application;
import android.util.Log;

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

    //query extras
    private boolean isQueryExhausted;
    //private String query;
    //private int pageNumber;
    private boolean isPerformingQuery;
    private boolean cancelRequest;
    private long requestStartTime;

    public static final String QUERY_EXHAUSTED = "No Results";

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
        if(!isPerformingQuery){
            isQueryExhausted = false;
            executeGetFloraCategories();
        }
    }

    private void executeGetFloraCategories() {
        requestStartTime = System.currentTimeMillis();
        isPerformingQuery = true;
        cancelRequest = false;
        viewState.setValue(ViewState.FLORA_CATEGORIES);

        final LiveData<Resource<List<FloraCategory>>> repositorySource = floraCategoryRepository.getFloraCategoriesApi(/*param*/);

        floraCategories.addSource(repositorySource, new Observer<Resource<List<FloraCategory>>>() {
            @Override
            public void onChanged(Resource<List<FloraCategory>> listResource) {
                if(!cancelRequest) {
                    if(listResource != null) {
                        floraCategories.setValue(listResource);
                        if(listResource.status == Resource.Status.SUCCESS ){
                            Log.d(TAG, "onChanged Success: Request Time " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds");
                            isPerformingQuery = false;
                            if(listResource.data != null) {
                                if (listResource.data.size() == 0) {
                                    Log.d(TAG, "onChanged: query is EXHAUSTED...");
                                    floraCategories.setValue(new Resource<List<FloraCategory>>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            QUERY_EXHAUSTED
                                    ));
                                    isQueryExhausted = true;
                                }
                            }
                            // must remove or it will keep listening to repository
                            floraCategories.removeSource(repositorySource);
                        }
                        else if(listResource.status == Resource.Status.ERROR ){
                            Log.d(TAG, "onChanged Error: Request Time " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds");
                            isPerformingQuery = false;
                            floraCategories.removeSource(repositorySource);
                        }
                    }
                    else{
                        floraCategories.removeSource(repositorySource);
                    }
                } else {
                    floraCategories.removeSource(repositorySource);
                }
            }
        });
    }

    public void cancelGetFloraCategoriesRequest() {
        if(isPerformingQuery) {
            Log.d(TAG, "cancelGetFloraCategoriesRequest: canceling request");
            cancelRequest = true;
            isPerformingQuery =  false;
        }
    }

//    public int getPageNumber() {
//        return pageNumber;
//    }
}
