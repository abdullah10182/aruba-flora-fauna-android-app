package com.triangon.aruba_flora_fauna.viewmodels;

import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.repositories.FloraSpeciesRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class FloraSpeciesListViewModel extends ViewModel {

    private FloraSpeciesRepository mFloraSpeciesRepository;
    private String mSelectedFloraCategory;
    private String mSelectedFloraId;
    private String mSelectedSearchQuery;

    public FloraSpeciesListViewModel(){
        mFloraSpeciesRepository = FloraSpeciesRepository.getInstance();
    }

    public LiveData<List<FloraSpecies>> getFloraSpecies() {
        return mFloraSpeciesRepository.getFloraSpecies();
    }

    public void getFloraSpeciesApi(String category, String speciesId, String searchQuery) {
        mSelectedFloraCategory = category;
        mSelectedFloraId = speciesId;
        mSelectedSearchQuery = searchQuery;
        mFloraSpeciesRepository.getFloraSpeciesApi(category, speciesId, searchQuery);
    }

    public String getSelectedFloraCategory() {
        return mSelectedFloraCategory;
    }

    public String getSelectedFloraId() {
        return mSelectedFloraId;
    }

    public String getmSelectedSearchQuery() {
        return mSelectedSearchQuery;
    }

    public void resetFloraSpecies() {
        this.mFloraSpeciesRepository.resetFloraSpecies();
    }
}
