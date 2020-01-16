package com.triangon.aruba_flora_fauna.activities;

import com.triangon.aruba_flora_fauna.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.adapters.FloraSpeciesRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.OnFloraSpeciesListener;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesListViewModel;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FloraSpeciesListActivity extends BaseActivity implements OnFloraSpeciesListener {

    private FloraSpeciesListViewModel mFloraSpeciesListViewModel;
    @BindView(R.id.rv_flora_species_list)
    public RecyclerView mRecyclerView;
    private FloraSpeciesRecyclerAdapter mAdapter;
    private String mSelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_species_list);

        ButterKnife.bind(this);

        mFloraSpeciesListViewModel = ViewModelProviders.of(this).get(FloraSpeciesListViewModel.class);
        mSelectedCategory = getIntent().getExtras().getString("categoryId");
        initRecyclerView();
        subscribeObservers();
        getFloraSpeciesApi(mSelectedCategory);
        initToolbar();
    }

    private void initRecyclerView() {
        mAdapter = new FloraSpeciesRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getFloraSpeciesApi(String categoryId) {
//        if(mFloraSpeciesListViewModel.getSelectedFloraCategory() != null && mFloraSpeciesListViewModel.getSelectedFloraCategory().equals(categoryId)) {
//            mFloraSpeciesListViewModel.getFloraSpeciesApi(categoryId);
//        }

        showProgressBar(true);
        mFloraSpeciesListViewModel.getFloraSpeciesApi(categoryId);
    }

    private void subscribeObservers() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mFloraSpeciesListViewModel.getFloraSpecies().observe(this, new Observer<List<FloraSpecies>>() {
            @Override
            public void onChanged(List<FloraSpecies> floraSpecies) {
                if(floraSpecies != null) {
                    if(mFloraSpeciesListViewModel.getSelectedFloraCategory().equals(floraSpecies.get(0).getCategoryId())){
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mAdapter.setFloraSpecies(floraSpecies);
                        showProgressBar(false);
                    }
                }
            }
        });
    }

    @Override
    public void onFloraSpeciesClick(int position) {
        Intent intent = new Intent(this, FloraSpeciesDetailActivity.class);
        intent.putExtra("selectedSpecies", mAdapter.getFloraSpecies().get(position));
        startActivity(intent);
    }

    public void initToolbar() {
        Toolbar toolbar  = findViewById(R.id.toolbar);
        String categoryName = getIntent().getExtras().getString("categoryName");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
