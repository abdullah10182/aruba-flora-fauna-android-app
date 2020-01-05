package com.triangon.aruba_flora_fauna.repositories;

import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.requests.FloraSpeciesApiClient;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FloraSpeciesRepository {

    private static FloraSpeciesRepository instance;
    private FloraSpeciesApiClient mFloraSpeciesApiClient;

    public static FloraSpeciesRepository getInstance() {
        if (instance == null) {
            instance = new FloraSpeciesRepository();
        }
        return instance;
    }

    private FloraSpeciesRepository() {
        mFloraSpeciesApiClient = FloraSpeciesApiClient.getInstance();
    }


    public LiveData<List<FloraSpecies>> getFloraSpecies() {
        return mFloraSpeciesApiClient.getFloraSpecies();
    }

    public void getFloraSpeciesApi(String category) {
        mFloraSpeciesApiClient.getFloraSpeciesApi(category);
    }


}
