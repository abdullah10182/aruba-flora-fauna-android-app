package com.triangon.aruba_flora_fauna.activities;

import com.triangon.aruba_flora_fauna.R;
import android.os.Bundle;
import android.widget.TextView;

import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesListViewModel;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class FloraSpeciesListActivity extends BaseActivity {

    private FloraSpeciesListViewModel mFloraSpeciesListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_species_list);

        mFloraSpeciesListViewModel = ViewModelProviders.of(this).get(FloraSpeciesListViewModel.class);
        String categoryId = getIntent().getExtras().getString("categoryId");
        getFloraSpeciesApi(categoryId);
        subscribeObservers();
    }

    private void getFloraSpeciesApi(String categoryId) {
        showProgressBar(true);
        mFloraSpeciesListViewModel.getFloraSpeciesApi(categoryId);
    }

    private void subscribeObservers() {
        mFloraSpeciesListViewModel.getFloraSpecies().observe(this, new Observer<List<FloraSpecies>>() {
            @Override
            public void onChanged(List<FloraSpecies> floraSpecies) {
                if(floraSpecies != null) {
                    showProgressBar(false);
                }
            }
        });
    }
}
