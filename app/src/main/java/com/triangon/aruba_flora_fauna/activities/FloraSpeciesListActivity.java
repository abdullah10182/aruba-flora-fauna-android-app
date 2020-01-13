package com.triangon.aruba_flora_fauna.activities;

import com.triangon.aruba_flora_fauna.R;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.adapters.FloraCategoryRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.FloraSpeciesRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.GridLayoutItemDecoration;
import com.triangon.aruba_flora_fauna.adapters.OnFloraSpeciesListener;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesListViewModel;

import java.util.List;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FloraSpeciesListActivity extends BaseActivity implements OnFloraSpeciesListener {

    private FloraSpeciesListViewModel mFloraSpeciesListViewModel;
    private RecyclerView mRecyclerView;
    private FloraSpeciesRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_species_list);

        mFloraSpeciesListViewModel = ViewModelProviders.of(this).get(FloraSpeciesListViewModel.class);
        mRecyclerView = findViewById(R.id.rv_flora_species_list);
        String categoryId = getIntent().getExtras().getString("categoryId");
        getFloraSpeciesApi(categoryId);
        subscribeObservers();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new FloraSpeciesRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                    mAdapter.setmFloraSpecies(floraSpecies);
                    showProgressBar(false);
                }
            }
        });
    }

    @Override
    public void onFloraSpeciesClick(int position) {
        String text = mAdapter.getFloraSpecies().get(position).getCommonName();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
