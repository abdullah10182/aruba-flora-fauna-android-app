package com.triangon.aruba_flora_fauna.activities;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.triangon.aruba_flora_fauna.BaseActivity;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.adapters.FloraCategoryRecyclerAdapter;
import com.triangon.aruba_flora_fauna.adapters.GridLayoutItemDecoration;
import com.triangon.aruba_flora_fauna.adapters.OnFloraCategoryListener;
import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel;

import java.util.List;

public class FloraCategoryListActivity extends BaseActivity implements OnFloraCategoryListener {

    private static final String TAG = "FloraCategoryListActivity";

    private FloraCategoryListViewModel mFloraCategoryListViewModel;
    private RecyclerView mRecyclerView;
    private FloraCategoryRecyclerAdapter mAdapter;
    private ImageView mLogoToolbar;
    private RelativeLayout mLogoHero;


    private List<String> lastSearches;
    private MaterialSearchBar searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flora_category_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = findViewById(R.id.rv_flora_category_list);
        mFloraCategoryListViewModel = ViewModelProviders.of(this).get(FloraCategoryListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        getFloraCategoriesApi();

        initAppBar();


        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Custom hint");
        searchBar.setSpeechMode(true);
        //enable searchbar callbacks

        //restore last queries from disk


        //Inflate menu and setup OnMenuItemClickListener
        searchBar.inflateMenu(R.menu.toolbar_main_menu);


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
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        mLogoToolbar.setVisibility(View.VISIBLE);
    }

    public void appBarListener(AppBarLayout appBarLayout) {

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    AnimatorSet set = new AnimatorSet();
                    int widthOfScreen = appBarLayout.getWidth();
                    int widthHeroImage = mLogoToolbar.getWidth();
                    set.playTogether(
                            //ObjectAnimator.ofFloat(mLogoToolbar, "translationX", ((widthOfScreen/2f) - (widthHeroImage/2.5f))).setDuration(300),
                            ObjectAnimator.ofFloat(mLogoHero, "alpha", 0f).setDuration(200),
                            ObjectAnimator.ofFloat(mLogoToolbar, "alpha", 1f).setDuration(200)
                    );
                    set.start();
                } else if (verticalOffset == 0) {
                    // If expanded, then do this
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(
                            ObjectAnimator.ofFloat(mLogoHero, "alpha", 1f).setDuration(200),
                            ObjectAnimator.ofFloat(mLogoToolbar, "alpha", 0f).setDuration(200)
                    );
                    set.start();
                }
            }
        });
    }


}
