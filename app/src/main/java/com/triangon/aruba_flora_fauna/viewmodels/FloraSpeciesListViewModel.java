package com.triangon.aruba_flora_fauna.viewmodels;

import android.app.Application;
import android.util.Log;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.repositories.FloraCategoryRepository;
import com.triangon.aruba_flora_fauna.repositories.FloraSpeciesRepository;
import com.triangon.aruba_flora_fauna.utils.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import retrofit2.http.Query;

public class FloraSpeciesListViewModel extends AndroidViewModel {

    private static final String TAG = "SpeciesListViewModel";

    public enum ViewState {FLORA_SPECIES};
    private MutableLiveData<FloraSpeciesListViewModel.ViewState> viewState;
    private MediatorLiveData<Resource<List<FloraSpecies>>> floraSpecies = new MediatorLiveData<>();

    //query extras
    private boolean isQueryExhausted;
    private String category;
    private String speciesId;
    private String searchQuery;
    private boolean isPerformingQuery;
    private boolean cancelRequest;
    private long requestStartTime;

    public static final String QUERY_EXHAUSTED = "No Results";

    private FloraSpeciesRepository floraSpeciesRepository;

    public FloraSpeciesListViewModel(@NonNull Application application) {
        super(application);
        floraSpeciesRepository = FloraSpeciesRepository.getInstance(application);
        init();
    }

    private void init() {
        if(viewState == null) {
            viewState = new MutableLiveData<>();
            viewState.setValue(FloraSpeciesListViewModel.ViewState.FLORA_SPECIES);
        }
    }

    public LiveData<FloraSpeciesListViewModel.ViewState> getViewState() {
        return viewState;
    }

    public LiveData<Resource<List<FloraSpecies>>> getFloraSpecies() {
        return floraSpecies;
    }

    public void getFloraSpeciesApi(String category, String speciesId, String searchQuery) {
        if(!isPerformingQuery){
            isQueryExhausted = false;
            executeGetFloraSpecies(category, speciesId, searchQuery);
        }
    }

    private void executeGetFloraSpecies(String category, String speciesId, String searchQuery) {
        requestStartTime = System.currentTimeMillis();
        isPerformingQuery = true;
        cancelRequest = false;
        viewState.setValue(FloraSpeciesListViewModel.ViewState.FLORA_SPECIES);

        final LiveData<Resource<List<FloraSpecies>>> repositorySource = floraSpeciesRepository.getFloraSpeciesApi(category, speciesId, searchQuery);

        floraSpecies.addSource(repositorySource, new Observer<Resource<List<FloraSpecies>>>() {
            @Override
            public void onChanged(Resource<List<FloraSpecies>> listResource) {
                if(!cancelRequest) {
                    if(listResource != null) {
                        floraSpecies.setValue(listResource);
                        if(listResource.status == Resource.Status.SUCCESS ){
                            Log.d(TAG, "onChanged Success: Request Time " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds");
                            isPerformingQuery = false;
                            if(listResource.data != null) {
                                if (listResource.data.size() == 0) {
                                    Log.d(TAG, "onChanged: query is EXHAUSTED...");
                                    floraSpecies.setValue(new Resource<List<FloraSpecies>>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            QUERY_EXHAUSTED
                                    ));
                                    isQueryExhausted = true;
                                }
                            }
                            // must remove or it will keep listening to repository
                            floraSpecies.removeSource(repositorySource);
                        }
                        else if(listResource.status == Resource.Status.ERROR ){
                            Log.d(TAG, "onChanged Error: Request Time " + (System.currentTimeMillis() - requestStartTime) / 1000 + " seconds");
                            isPerformingQuery = false;
                            floraSpecies.removeSource(repositorySource);
                        }
                    }
                    else{
                        floraSpecies.removeSource(repositorySource);
                    }
                } else {
                    floraSpecies.removeSource(repositorySource);
                }
            }
        });
    }

    public void cancelGetFloraSpeciesRequest() {
        if(isPerformingQuery) {
            Log.d(TAG, "cancelGetFloraSpeciesRequest: canceling request");
            cancelRequest = true;
            isPerformingQuery =  false;
        }
    }

    public String getCategory() {
        return category;
    }

    public String getSpeciesId() {
        return speciesId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
