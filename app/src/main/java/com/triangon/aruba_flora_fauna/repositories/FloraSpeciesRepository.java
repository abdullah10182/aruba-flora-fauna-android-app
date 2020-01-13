package com.triangon.aruba_flora_fauna.repositories;

import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.requests.FloraSpeciesApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FloraSpeciesRepository {

    private static FloraSpeciesRepository instance;
    private FloraSpeciesApiClient mFloraSpeciesApiClient;
    private FloraSpeciesApiClient mFloraSpeciesSuggestionsApiClient;

    public static FloraSpeciesRepository getInstance() {
        if (instance == null) {
            instance = new FloraSpeciesRepository();
        }
        return instance;
    }

    private FloraSpeciesRepository() {
        mFloraSpeciesApiClient = FloraSpeciesApiClient.getInstance();
        mFloraSpeciesSuggestionsApiClient = FloraSpeciesApiClient.getInstance();
    }


    public LiveData<List<FloraSpecies>> getFloraSpecies() {
        return mFloraSpeciesApiClient.getFloraSpecies();
    }

    public LiveData<List<FloraSpecies>> getFloraSpeciesSuggestions() {
        return mFloraSpeciesSuggestionsApiClient.getFloraSpeciesSuggestions();
    }

    public void getFloraSpeciesApi(String category) {
        mFloraSpeciesApiClient.getFloraSpeciesApi(category);
    }

    public void getFloraSpeciesSuggestionsApi() {
        mFloraSpeciesSuggestionsApiClient.getFloraSpeciesSuggestionsApi();
    }


}
