package com.triangon.aruba_flora_fauna.activities;

import com.triangon.aruba_flora_fauna.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

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
    @BindView(R.id.tv_no_results)
    public TextView mNoResults;
    private FloraSpeciesRecyclerAdapter mAdapter;
    private String mSelectedCategory;
    private String mSelectedCategoryName;
    private String mSearchQuery;
    private Toolbar mToolbar;
    @BindView(R.id.fl_error_screen)
    public FrameLayout mErrorScreen;
    @BindView(R.id.btn_retry_call)
    public Button mRetryCallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_species_list);

        ButterKnife.bind(this);

        mFloraSpeciesListViewModel = ViewModelProviders.of(this).get(FloraSpeciesListViewModel.class);
        mSelectedCategory = getIntent().getExtras().getString("categoryId");
        mSelectedCategoryName = getIntent().getExtras().getString("categoryName");
        mSearchQuery = getIntent().getExtras().getString("searchQuery");

        initRecyclerView();
        subscribeObservers();
        showErrorScreen(false);
        //showProgressBar(true);
        getSpeciesList();
        initClickHandlerRetryButton();

    }

    private void initClickHandlerRetryButton() {
        mRetryCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showErrorScreen(false);
                getSpeciesList();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //showProgressBar(true);
        //mRecyclerView.setVisibility(View.INVISIBLE);
       // getSpeciesList();
    }

    private void getSpeciesList() {
        showProgressBar(true);
        mFloraSpeciesListViewModel.setDidRetrieveSpecies(false);
        if(mSelectedCategory != null && mSelectedCategoryName != null) {
            initToolbar(mSelectedCategoryName);
            mFloraSpeciesListViewModel.getFloraSpeciesApi(mSelectedCategory, null, null);
        } else if (mSearchQuery != null){
            initToolbar("Search Results: " + mSearchQuery);
            mFloraSpeciesListViewModel.resetFloraSpecies();
            mFloraSpeciesListViewModel.getFloraSpeciesApi(null, null, mSearchQuery);
        }
    }

    private void initRecyclerView() {
        mAdapter = new FloraSpeciesRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void subscribeObservers() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mFloraSpeciesListViewModel.getFloraSpecies().observe(this, new Observer<List<FloraSpecies>>() {
            @Override
            public void onChanged(List<FloraSpecies> floraSpecies) {
                if(floraSpecies != null && floraSpecies.size() > 0) {
                    mAdapter.setFloraSpecies(floraSpecies);
                    mFloraSpeciesListViewModel.setDidRetrieveSpecies(true);
                    mFloraSpeciesListViewModel.setIsPerformingQuery(false);
                    if(mSelectedCategory != null && mFloraSpeciesListViewModel.getSelectedFloraCategory().equals(floraSpecies.get(0).getCategoryId())){
                        mRecyclerView.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                        mNoResults.setVisibility(View.GONE);
                        mAdapter.setFloraSpecies(floraSpecies);
                    } else if(mSearchQuery != null){
                        mRecyclerView.setVisibility(View.VISIBLE);
                        showProgressBar(false);
                        mNoResults.setVisibility(View.GONE);
                        mAdapter.setFloraSpecies(floraSpecies);
                    }
                } else if (floraSpecies != null && floraSpecies.size() == 0 && mSelectedCategory == null){
                    mNoResults.setVisibility(View.VISIBLE);
                    showProgressBar(false);
                }
            }
        });

//        mFloraSpeciesListViewModel.isSpeciesRequestTimedOut().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if( aBoolean && !mFloraSpeciesListViewModel.isDidRetrieveSpecies()) {
//                    showProgressBar(false);
//                    showErrorScreen(true);
//                }
//            }
//        });
    }

    @Override
    public void onFloraSpeciesClick(int position) {
        Intent intent = new Intent(this, FloraSpeciesDetailActivity.class);
        intent.putExtra("selectedSpecies", mAdapter.getFloraSpecies().get(position));
        startActivity(intent);
    }

    public void initToolbar(String title) {
        mToolbar  = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showErrorScreen(Boolean show){
        if(show) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorScreen.setVisibility(View.VISIBLE);
        } else {
            mErrorScreen.setVisibility(View.GONE);
        }
    }
}
