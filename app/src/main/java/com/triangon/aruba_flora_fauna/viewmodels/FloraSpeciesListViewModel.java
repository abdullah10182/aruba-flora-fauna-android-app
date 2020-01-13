package com.triangon.aruba_flora_fauna.viewmodels;

import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.repositories.FloraSpeciesRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FloraSpeciesListViewModel extends ViewModel {

    private FloraSpeciesRepository mFloraSpeciesRepository;
    private FloraSpeciesRepository mFloraSpeciesSuggestionsRepository;

    public FloraSpeciesListViewModel(){
        mFloraSpeciesRepository = FloraSpeciesRepository.getInstance();
        mFloraSpeciesSuggestionsRepository = FloraSpeciesRepository.getInstance();
    }

    public LiveData<List<FloraSpecies>> getFloraSpecies() {
        return mFloraSpeciesRepository.getFloraSpecies();
    }

    public LiveData<List<FloraSpecies>> getFloraSpeciesSuggestions() {
        return mFloraSpeciesRepository.getFloraSpeciesSuggestions();
    }

    public void getFloraSpeciesApi(String category) {
        mFloraSpeciesRepository.getFloraSpeciesApi(category);
    }

    public void getFloraSpeciesSuggestionsApi() {
        mFloraSpeciesRepository.getFloraSpeciesSuggestionsApi();
    }
}
