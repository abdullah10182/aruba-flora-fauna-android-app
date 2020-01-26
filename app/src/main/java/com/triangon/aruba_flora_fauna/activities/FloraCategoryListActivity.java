package com.triangon.aruba_flora_fauna.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.FixedPreloadSizeProvider;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.google.android.material.appbar.AppBarLayout;
import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.adapters.FloraCategoryRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.FloraSpeciesRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.GridLayoutItemDecoration;
import com.triangon.aruba_flora_fauna.adapters.OnFloraCategoryListener;
import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.utils.Resource;
import com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel;

import java.util.List;

import static com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel.QUERY_EXHAUSTED;

public class FloraCategoryListActivity extends BaseActivity implements OnFloraCategoryListener {

    private static final String TAG = "CategoryListActivity";

    private FloraCategoryListViewModel mFloraCategoryListViewModel;
    @BindView(R.id.rv_flora_category_list)
    public RecyclerView mRecyclerView;
    private FloraCategoryRecyclerAdapter mAdapter;
    @BindView(R.id.iv_logo_toolbar)
    public ImageView mLogoToolbar;
    @BindView(R.id.iv_logo_hero)
    public RelativeLayout mLogoHero;
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.iv_search_circle_bg)
    public ImageView mSearchIconCircleBg;
    @BindView(R.id.fl_error_screen)
    public FrameLayout mErrorScreen;
    @BindView(R.id.btn_retry_call)
    public Button mRetryCallButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_category_list);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mFloraCategoryListViewModel = ViewModelProviders.of(this).get(FloraCategoryListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        getFloraCategoriesApi();

        initAppBar();

        initSearchButton();

        initClickHandlerRetryButton();

    }

    private void initClickHandlerRetryButton() {
        mRetryCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showErrorScreen(false);
                getFloraCategoriesApi();
            }
        });
    }

    private void getFloraCategoriesApi() {
        showProgressBar(true);
        //mFloraCategoryListViewModel.getFloraCategoriesApi();
        mFloraCategoryListViewModel.getFloraCategoriesApi(/*params*/);
    }

    private void subscribeObservers() {
        mFloraCategoryListViewModel.getFloraCategories().observe(this, new Observer<Resource<List<FloraCategory>>>() {
            @Override
            public void onChanged(Resource<List<FloraCategory>> listResource) {
                if(listResource != null) {
                    Log.d(TAG, "onChanged: status" + listResource.status);

                    if(listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING: {
                                //mAdapter.displayOnlyLoading();
                                showProgressBar(true);
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #categories: " + listResource.data.size());
                                //mAdapter.hideLoading();
                                showProgressBar(false);
                                mAdapter.setFloraCategories(listResource.data);
                                break;
                            }
                            case ERROR: {
                                Log.e(TAG, "onChanged: cannot refresh cache.");
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message );
                                Log.e(TAG, "onChanged: status: ERROR, #categories: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setFloraCategories(listResource.data);
                                Toast.makeText(FloraCategoryListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                if(listResource.message.equals(QUERY_EXHAUSTED)){
                                    mAdapter.setQueryExhausted();
                                    showErrorScreen(true);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        });

        mFloraCategoryListViewModel.getViewState().observe(this, new Observer<FloraCategoryListViewModel.ViewState>() {
            @Override
            public void onChanged(FloraCategoryListViewModel.ViewState viewState) {
                if(viewState != null) {
                    switch (viewState){
                        case FLORA_CATEGORIES: {
                            displayFloraCategories();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void displayFloraCategories() {
        //mAdapter.displayFloraCategories();
    }

    private RequestManager initGlide() {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.aff_logo_grey)
                .error(R.drawable.aff_logo_grey);
        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

    private void initRecyclerView() {
        int columnCount = 2;
        int imageWidthPixels = 480;
        int imageHeightPixels = 480;

        ListPreloader.PreloadSizeProvider sizeProvider =
                new FixedPreloadSizeProvider(imageWidthPixels, imageHeightPixels);

        mAdapter = new FloraCategoryRecyclerAdapter(this, initGlide(), sizeProvider);

        RecyclerViewPreloader<String> preloader =
                new RecyclerViewPreloader<>(
                        Glide.with(this), mAdapter, sizeProvider, 20);


//        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();
//        mAdapter = new FloraCategoryRecyclerAdapter(this, initGlide(), viewPreloader);
//        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<String>(
//                Glide.with(this),
//                mAdapter,
//                viewPreloader,
//                20);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),columnCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new GridLayoutItemDecoration(columnCount, 0, true));

        mRecyclerView.addOnScrollListener(preloader);

        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onFloraCategoryClick(int position) {
        Intent intent = new Intent(this, FloraSpeciesListActivity.class);
        String categoryId = mAdapter.getFloraCategories().get(position).getId();
        String categoryName = mAdapter.getFloraCategories().get(position).getName();
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("categoryName", categoryName);
        startActivity(intent);
    }

    public void initAppBar() {
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        appBarListener(appBarLayout);
    }

    public void appBarListener(AppBarLayout appBarLayout) {

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    //if collapses
                    if(mLogoToolbar.getVisibility() == View.GONE)
                        mLogoToolbar.setVisibility(View.VISIBLE);

                    mToolbar.findViewById(R.id.action_search).setVisibility(View.VISIBLE);

                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(
                            ObjectAnimator.ofFloat(mLogoHero, "alpha", 0f).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "alpha", 1f).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "scaleX", 1f ).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "scaleY", 1f ).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "translationX", 0 ).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "translationY", 0 ).setDuration(400),
                            ObjectAnimator.ofFloat(mSearchIconCircleBg, "scaleX", 0.6f ).setDuration(400),
                            ObjectAnimator.ofFloat(mSearchIconCircleBg, "scaleY", 0.6f ).setDuration(400),
                            ObjectAnimator.ofFloat(mSearchIconCircleBg, "alpha", 0f ).setDuration(400)
                    );
                    set.start();
                } else if (verticalOffset == 0) {
                    // If expanded, then do this
                    if(mToolbar.findViewById(R.id.action_search) != null)
                        mToolbar.findViewById(R.id.action_search).setVisibility(View.GONE);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(
                            ObjectAnimator.ofFloat(mLogoHero, "alpha", 1f).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "alpha", 0f).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "scaleX", 1.5f ).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "scaleY", 1.5f ).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "translationX", 150 ).setDuration(400),
                            ObjectAnimator.ofFloat(mLogoToolbar, "translationY", -60 ).setDuration(400),
                            ObjectAnimator.ofFloat(mSearchIconCircleBg, "scaleX", 1f ).setDuration(400),
                            ObjectAnimator.ofFloat(mSearchIconCircleBg, "scaleY", 1f ).setDuration(400),
                            ObjectAnimator.ofFloat(mSearchIconCircleBg, "alpha", 1f ).setDuration(400)


                    );
                    set.start();
                }
            }
        });
    }

    public void initSearchButton() {
        mSearchIconCircleBg.bringToFront();
        mSearchIconCircleBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("test");
                openSearch();
            }
        });
    }

    public void showErrorScreen(Boolean show){
        if(show) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mErrorScreen.setVisibility(View.VISIBLE);
        } else {
            mErrorScreen.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

}
