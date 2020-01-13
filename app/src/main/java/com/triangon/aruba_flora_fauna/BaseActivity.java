package com.triangon.aruba_flora_fauna;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    public ProgressBar mProgressBar;
    public MaterialSearchView mSearchView;
    private String[] mSuggestions;
    private FloraSpeciesListViewModel mFloraSpeciesListViewModel;
    @Nullable
    private ProgressBar mSearchProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFloraSpeciesListViewModel = ViewModelProviders.of(this).get(FloraSpeciesListViewModel.class);
        subscribeObservers();
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

        String[] myStringArray;
        myStringArray = new String[]{""}; //set empty array otherwise overlay does not work
        mSearchView.setSuggestions(myStringArray);

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mSuggestions == null && newText.length() > 0)
                    getFloraSpeciesSuggestionsApi();
                return true;
            }
        });


        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        return true;
    }

    private void getFloraSpeciesSuggestionsApi() {
        mSearchProgressBar.setVisibility(View.VISIBLE);
        mFloraSpeciesListViewModel.getFloraSpeciesSuggestionsApi();
    }

    private void subscribeObservers() {
        mFloraSpeciesListViewModel.getFloraSpecies().observe(this, new Observer<List<FloraSpecies>>() {
            @Override
            public void onChanged(List<FloraSpecies> floraSpecies) {
                if(floraSpecies != null && mSearchProgressBar != null) {
                    mSearchProgressBar.setVisibility(View.GONE);
                    ArrayList<String> suggestionsArrList = new ArrayList<String>();
                    for(FloraSpecies species : floraSpecies) {
                        suggestionsArrList.add(species.getCommonName());
                        if(species.getPapiamentoName() != null && species.getPapiamentoName().trim() != species.getCommonName().trim())
                            suggestionsArrList.add(species.getPapiamentoName());
                    }

                    mSuggestions = suggestionsArrList.toArray(new String[suggestionsArrList.size()]);

                    if(mSuggestions.length > 0 && mSearchView != null)
                        mSearchView.setSuggestions(mSuggestions);
                    mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String query = (String) adapterView.getItemAtPosition(i);
                            String selectedId = getSelectedId(floraSpecies, query);
                        }
                    });
                }
            }
        });
    }

    private String getSelectedId(List<FloraSpecies> floraSpecies, String query) {
        String selectedId = "";

        for(FloraSpecies species : floraSpecies) {
            if(species.getCommonName() == query || species.getPapiamentoName() == query){
                selectedId = species.getId();
                String query1 = selectedId + species.getCommonName();
                Toast.makeText(getApplicationContext(), query1, Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return selectedId;
    }

    public void openSearch() {
        mSearchView.showSearch();
    }
}
