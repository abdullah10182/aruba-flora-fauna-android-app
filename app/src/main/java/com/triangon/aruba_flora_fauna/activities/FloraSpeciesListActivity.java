package com.triangon.aruba_flora_fauna.activities;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.triangon.aruba_flora_fauna.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.adapters.FloraSpeciesRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.OnFloraSpeciesListener;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.utils.Resource;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesListViewModel;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel.NO_RESULTS;

public class FloraSpeciesListActivity extends BaseActivity implements OnFloraSpeciesListener {

    private static final String TAG = "FloraSpeciesListActivit";

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
        getSpeciesList();
        initClickHandlerRetryButton();

    }

    private void initClickHandlerRetryButton() {
        mRetryCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showErrorScreen(false);
                getFloraSpeciesApi();
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
        if(mSelectedCategory != null && mSelectedCategoryName != null) {
            initToolbar(mSelectedCategoryName);
            mFloraSpeciesListViewModel.getFloraSpeciesApi(mSelectedCategory, null, null);
            getFloraSpeciesApi();
        } else if (mSearchQuery != null){
            initToolbar("Search Results: " + mSearchQuery);
            showProgressBar(true);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mFloraSpeciesListViewModel.getFloraSpeciesApi(null, null, mSearchQuery);
        }
    }

    private void getFloraSpeciesApi() {
        showProgressBar(true);
        mRecyclerView.setVisibility(View.GONE);
        mFloraSpeciesListViewModel.getFloraSpeciesApi(mSelectedCategory, null, null);
    }

    private void initRecyclerView() {
        int imageWidthPixels = 480;
        int imageHeightPixels = 480;

        ListPreloader.PreloadSizeProvider sizeProvider =
                new FixedPreloadSizeProvider(imageWidthPixels, imageHeightPixels);

        mAdapter = new FloraSpeciesRecyclerAdapter(this, initGlide(), sizeProvider);

        RecyclerViewPreloader<String> preloader =
                new RecyclerViewPreloader<>(
                        Glide.with(this), mAdapter, sizeProvider, 20);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();
//        mAdapter = new FloraSpeciesRecyclerAdapter(this, initGlide(), viewPreloader);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<>(
//                Glide.with(this),
//                mAdapter,
//                viewPreloader,
//                20);

        mRecyclerView.addOnScrollListener(preloader);

        mRecyclerView.setAdapter(mAdapter);
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.aff_logo_grey)
                .error(R.drawable.aff_logo_grey);
        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    private void subscribeObservers() {
        mFloraSpeciesListViewModel.getFloraSpecies().observe(this, new Observer<Resource<List<FloraSpecies>>>() {
            @Override
            public void onChanged(Resource<List<FloraSpecies>> listResource) {
                if(listResource != null) {
                    Log.d(TAG, "onChanged: status" + listResource.status);

                    if(listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING: {
                                //mAdapter.displayOnlyLoading();
                                showProgressBar(true);
                            }
                            case ERROR: {
                                Log.e(TAG, "onChanged: cannot refresh cache.");
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message );
                                Log.e(TAG, "onChanged: status: ERROR, #categories: " + listResource.data.size());
                                if(listResource.message != null)
                                    Toast.makeText(FloraSpeciesListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                if(listResource.message != null && listResource.message.equals(NO_RESULTS) && mSearchQuery != null)
                                    Toast.makeText(FloraSpeciesListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                else if(listResource.message != null && listResource.data.size() == 0)
                                    showErrorScreen(true);
                                else if (listResource.data.size() > 0 ){
                                    mAdapter.setFloraSpecies(listResource.data);
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    showProgressBar(false);
                                }
                                break;
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #categories: " + listResource.data.size());
                                //mAdapter.hideLoading();
                                showProgressBar(false);
                                //showErrorScreen(false);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mAdapter.setFloraSpecies(listResource.data);
                                break;
                            }
                        }
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
            showProgressBar(false);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorScreen.setVisibility(View.VISIBLE);
        } else {
            mErrorScreen.setVisibility(View.GONE);
        }
    }
}
