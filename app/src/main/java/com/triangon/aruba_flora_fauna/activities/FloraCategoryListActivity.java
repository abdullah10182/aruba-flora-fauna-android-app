package com.triangon.aruba_flora_fauna.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.google.android.material.appbar.AppBarLayout;
import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.adapters.FloraCategoryRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.GridLayoutItemDecoration;
import com.triangon.aruba_flora_fauna.adapters.OnFloraCategoryListener;
import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel;

import java.util.List;

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
    }

    private void subscribeObservers() {
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
        mAdapter.displayFloraCategories();
    }

    private void initRecyclerView() {
        int columnCount = 2;
        mAdapter = new FloraCategoryRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),columnCount);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new GridLayoutItemDecoration(columnCount, 0, true));
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
        }
    }

}
