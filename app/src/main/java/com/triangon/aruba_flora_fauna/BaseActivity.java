package com.triangon.aruba_flora_fauna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.triangon.aruba_flora_fauna.activities.FloraSpeciesDetailActivity;
import com.triangon.aruba_flora_fauna.activities.FloraSpeciesListActivity;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.requests.FloraSpeciesApi;
import com.triangon.aruba_flora_fauna.requests.ServiceGenerator;
import com.triangon.aruba_flora_fauna.requests.responses.FloraSpeciesListResponse;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesListViewModel;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesSuggestionsViewModel;
import com.triangon.aruba_flora_fauna.widgets.LatestFloraSpeciesAppWidget;
import com.triangon.aruba_flora_fauna.widgets.LatestFloraSpeciesWidgetService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    public ProgressBar mProgressBar;
    public MaterialSearchView mSearchView;
    private String[] mSuggestions;
    private FloraSpeciesSuggestionsViewModel mFloraSpeciesSuggestionsViewModel;
    @Nullable
    private ProgressBar mSearchProgressBar;
    private boolean mSearchInitiated = false;
    private static final String LATEST_SPECIES_PREFERENCES = "myPrefrences";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFloraSpeciesSuggestionsViewModel = ViewModelProviders.of(this).get(FloraSpeciesSuggestionsViewModel.class);
        //LatestFloraSpeciesWidgetService.startActionGetLatestFloraSpecies(this);
        subscribeObservers();
        //initLatestSpeciesStorage();
    }

    @Override
    public void setContentView(int layoutResID) {

        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);
        mProgressBar = constraintLayout.findViewById(R.id.progress_bar);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(constraintLayout);
    }

    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = findViewById(R.id.search_view);
        mSearchView.setMenuItem(item);

        mSearchProgressBar = findViewById(R.id.pb_search_loader);

        String[] tempStringArray;
        tempStringArray = new String[]{""}; //set empty array otherwise overlay does not work
        mSearchView.setSuggestions(tempStringArray);

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.closeSearch();
                Intent intent = new Intent(getApplicationContext(), FloraSpeciesListActivity.class);
                intent.putExtra("searchQuery", query);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mSuggestions == null && newText.length() > 0 && mSearchInitiated == false) {
                    getFloraSpeciesSuggestionsApi();
                }
                return true;
            }
        });


//        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//
//            }
//        });

        return true;
    }

    private void getFloraSpeciesSuggestionsApi() {
        mSearchInitiated = true;
        mSearchProgressBar.setVisibility(View.VISIBLE);
        mFloraSpeciesSuggestionsViewModel.getFloraSpeciesSuggestionsApi(null);
    }

    private void subscribeObservers() {
        mFloraSpeciesSuggestionsViewModel.getFloraSpeciesSuggestions().observe(this, new Observer<List<FloraSpecies>>() {
            @Override
            public void onChanged(List<FloraSpecies> floraSpecies) {
                if(floraSpecies != null && mSearchProgressBar != null) {
                    mSearchProgressBar.setVisibility(View.GONE);
                    ArrayList<String> suggestionsArrList = new ArrayList<String>();
                    for(FloraSpecies species : floraSpecies) {
                        suggestionsArrList.add(species.getCommonName());
                        if(species.getPapiamentoName() != null && !species.getPapiamentoName().trim().equals(species.getCommonName().trim()))
                            suggestionsArrList.add(species.getPapiamentoName() + " (" + species.getCommonName() + ")");
                    }

                    mSuggestions = suggestionsArrList.toArray(new String[suggestionsArrList.size()]);

                    if(mSuggestions.length > 0 && mSearchView != null)
                        mSearchView.setSuggestions(mSuggestions);

                    mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            mSearchView.closeSearch();
                            String query = (String) adapterView.getItemAtPosition(i);
                            String selectedId = getSelectedId(floraSpecies, query);
                            Intent intent = new Intent(getApplicationContext(), FloraSpeciesDetailActivity.class);
                            intent.putExtra("selectedSpeciesId", selectedId);
                            intent.putExtra("selectedSpeciesName", query);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private String getSelectedId(List<FloraSpecies> floraSpecies, String query) {
        String selectedId = "";
        String[] splitQuery = query.split("[\\(\\)]");
        String nameBetweenParentheses = null;
        if(splitQuery.length > 1){
            nameBetweenParentheses = splitQuery[1];
        }
        String nameClean = nameBetweenParentheses != null ? nameBetweenParentheses : query;
        for(FloraSpecies species : floraSpecies) {
            if(species.getCommonName().equals(nameClean)){
                selectedId = species.getId();
                break;
            }
        }

        return selectedId;
    }

    public void openSearch() {
        mSearchView.showSearch();
    }

    private void initLatestSpeciesStorage() {
        SharedPreferences prefs = getSharedPreferences(LATEST_SPECIES_PREFERENCES, MODE_PRIVATE);
        long latestFloraSpeciesSavedTime = prefs.getLong("latestFloraSpeciesSavedTime",0);
        long currentTimeMillis = System.currentTimeMillis();


        if(latestFloraSpeciesSavedTime == 0 || currentTimeMillis - latestFloraSpeciesSavedTime > 1800000) {
            FloraSpeciesApi floraSpeciesApi = ServiceGenerator.getFloraSpeciesApi();

            Call<FloraSpeciesListResponse> responseCall = floraSpeciesApi
                    .getFloraSpeciesSuggestions("created");

            responseCall.enqueue(new Callback<FloraSpeciesListResponse>() {
                @Override
                public void onResponse(Call<FloraSpeciesListResponse> call, Response<FloraSpeciesListResponse> response) {
                    if(response.code() == 200) {
                        List<FloraSpecies> speciesList = new ArrayList<>(response.body().getFloraSpecies());
                        setLatestSpeciesDataInSharedPreferences(speciesList);

                    } else {
                        try {
                            Log.d(TAG, "onResponse: " + response.errorBody().string() );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FloraSpeciesListResponse> call, Throwable t) {

                }
            });
        }
    }

    public void setLatestSpeciesDataInSharedPreferences(List<FloraSpecies> latestFloraSpecies) {
        SharedPreferences.Editor editor = getSharedPreferences(LATEST_SPECIES_PREFERENCES, MODE_PRIVATE).edit();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String jsonLatestFloraSpecies = gson.toJson(latestFloraSpecies);
        //set time
        long currentTimeMillis = System.currentTimeMillis();

        editor.putString("latestFloraSpecies", jsonLatestFloraSpecies);
        editor.putLong("latestFloraSpeciesSavedTime", currentTimeMillis);
        editor.commit();
    }

}
