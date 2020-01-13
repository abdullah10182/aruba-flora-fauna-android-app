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
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.adapters.FloraCategoryRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.GridLayoutItemDecoration;
import com.triangon.aruba_flora_fauna.adapters.OnFloraCategoryListener;
import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.requests.FloraSpeciesApi;
import com.triangon.aruba_flora_fauna.requests.ServiceGenerator;
import com.triangon.aruba_flora_fauna.requests.responses.FloraSpeciesListResponse;
import com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FloraCategoryListActivity extends BaseActivity implements OnFloraCategoryListener {

    private static final String TAG = "CategoryListActivity";

    private FloraCategoryListViewModel mFloraCategoryListViewModel;
    private RecyclerView mRecyclerView;
    private FloraCategoryRecyclerAdapter mAdapter;
    private ImageView mLogoToolbar;
    private RelativeLayout mLogoHero;
    @Nullable
    private Toolbar mToolbar;
    @BindView(R.id.iv_search_circle_bg)
    public ImageView mSearchIconCircleBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_category_list);

        ButterKnife.bind(this);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = findViewById(R.id.rv_flora_category_list);
        mFloraCategoryListViewModel = ViewModelProviders.of(this).get(FloraCategoryListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        getFloraCategoriesApi();

        initAppBar();

        initSearchButton();

    }

    private void getFloraCategoriesApi() {
        mFloraCategoryListViewModel.getFloraCategoriesApi();
    }

    private void subscribeObservers() {
        showProgressBar(true);
        mFloraCategoryListViewModel.getFloraCategories().observe(this, new Observer<List<FloraCategory>>() {
            @Override
            public void onChanged(List<FloraCategory> floraCategories) {
                if(floraCategories != null) {
                    System.out.println(floraCategories.get(0).getName());
                    mAdapter.setFloraCategories(floraCategories);
                    showProgressBar(false);
                }
            }
        });
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
        String text = mAdapter.getFloraCategories().get(position).getName();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, FloraSpeciesListActivity.class);
        String categoryId = mAdapter.getFloraCategories().get(position).getId();
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }

    public void initAppBar() {
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        mLogoToolbar = findViewById(R.id.iv_logo_toolbar);
        mLogoHero = findViewById(R.id.iv_logo_hero);
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

}
