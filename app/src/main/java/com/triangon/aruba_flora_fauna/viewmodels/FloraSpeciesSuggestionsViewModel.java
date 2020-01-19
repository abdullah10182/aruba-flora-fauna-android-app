package com.triangon.aruba_flora_fauna.viewmodels;

import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.repositories.FloraSpeciesRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FloraSpeciesSuggestionsViewModel extends ViewModel {

    private FloraSpeciesRepository mFloraSpeciesSuggestionsRepository;

    public FloraSpeciesSuggestionsViewModel(){
        mFloraSpeciesSuggestionsRepository = mFloraSpeciesSuggestionsRepository.getInstance();
    }

    public LiveData<List<FloraSpecies>> getFloraSpeciesSuggestions() {
        return mFloraSpeciesSuggestionsRepository.getFloraSpeciesSuggestions();
    }

    public void getFloraSpeciesSuggestionsApi(String sortBy) {
        mFloraSpeciesSuggestionsRepository.getFloraSpeciesSuggestionsApi(sortBy);
    }
}
