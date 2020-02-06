package com.triangon.aruba_flora_fauna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import com.triangon.aruba_flora_fauna.activities.FloraSpeciesListActivity;
import com.triangon.aruba_flora_fauna.dialogs.DownloadDataDialog;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.models.ImageBundle;
import com.triangon.aruba_flora_fauna.utils.Resource;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesListViewModel;
import com.triangon.aruba_flora_fauna.viewmodels.FloraSpeciesSuggestionsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static com.triangon.aruba_flora_fauna.viewmodels.FloraCategoryListViewModel.NO_RESULTS;


public abstract class BaseActivity extends AppCompatActivity implements DownloadDataDialog.DownloadDataDialogListener {

    private static final String TAG = "BaseActivity";
    public ProgressBar mProgressBar;
    public MaterialSearchView mSearchView;
    private String[] mSuggestions;
    private FloraSpeciesSuggestionsViewModel mFloraSpeciesSuggestionsViewModel;
    private FloraSpeciesListViewModel mFloraSpeciesListViewModel;
    @Nullable
    private ProgressBar mSearchProgressBar;
    private boolean mSearchInitiated = false;
    private static final String FIRST_TIME_OPEN_PREFERENCES = "myPrefrences";
    private FirebaseAnalytics mFirebaseAnalytics;
    private AlertDialog mDownloadDialog;
    private TextView mDownloadTextIndicator;
    private ProgressBar mDownloadProgressBarIndicator;
    private LinearLayout mDownloadIndicatorWrapper;
    int mSpeciesIndex = 0;
    int mAdditionalImagesIndex = 0;
    String mCurrentPreloadImageSize; //small, medium etc
    List<FloraSpecies> mSpeciesDownload;
    MenuItem mDownloadActionMenuItem;
    TextView mCancelDownload;
    boolean mAllowedToDownload = true;
    boolean mDownloading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mFloraSpeciesSuggestionsViewModel = ViewModelProviders.of(this).get(FloraSpeciesSuggestionsViewModel.class);
        mFloraSpeciesListViewModel = ViewModelProviders.of(this).get(FloraSpeciesListViewModel.class);

        setFirstTimeAppOpenedInSharedPreferences();
        //openDownloadDialog();

        //LatestFloraSpeciesWidgetService.startActionGetLatestFloraSpecies(this);
        //initLatestSpeciesStorage();
    }

    @Override
    public void setContentView(int layoutResID) {

        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);
        mProgressBar = constraintLayout.findViewById(R.id.progress_bar);
        mDownloadIndicatorWrapper = constraintLayout.findViewById(R.id.ll_download_indicator_wrapper);
        mDownloadTextIndicator = constraintLayout.findViewById(R.id.tv_download_text_indicator);
        mDownloadProgressBarIndicator = constraintLayout.findViewById(R.id.pb_download_indicator);

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
        mDownloadActionMenuItem = menu.getItem(1);

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
                return false;
                //TODO: make suggestions work again
//                if(mSuggestions == null && newText.length() > 0 && mSearchInitiated == false) {
//                    getFloraSpeciesSuggestionsApi();
//                }
//                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_offline_download:
                openDownloadDialog();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void getFloraSpeciesSuggestionsApi() {
        mSearchInitiated = true;
        mSearchProgressBar.setVisibility(View.VISIBLE);
        mFloraSpeciesSuggestionsViewModel.getFloraSpeciesSuggestionsApi(null);
    }

    private void subscribeObservers() {
//        mFloraSpeciesSuggestionsViewModel.getFloraSpeciesSuggestions().observe(this, new Observer<List<FloraSpecies>>() {
//            @Override
//            public void onChanged(List<FloraSpecies> floraSpecies) {
//                if(floraSpecies != null && mSearchProgressBar != null) {
//                    mSearchProgressBar.setVisibility(View.GONE);
//                    ArrayList<String> suggestionsArrList = new ArrayList<String>();
//                    for(FloraSpecies species : floraSpecies) {
//                        suggestionsArrList.add(species.getCommonName());
//                        if(species.getPapiamentoName() != null && !species.getPapiamentoName().trim().equals(species.getCommonName().trim()))
//                            suggestionsArrList.add(species.getPapiamentoName() + " (" + species.getCommonName() + ")");
//                    }
//
//                    mSuggestions = suggestionsArrList.toArray(new String[suggestionsArrList.size()]);
//
//                    if(mSuggestions.length > 0 && mSearchView != null)
//                        mSearchView.setSuggestions(mSuggestions);
//
//                    mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                            mSearchView.closeSearch();
//                            String query = (String) adapterView.getItemAtPosition(i);
//                            String selectedId = getSelectedId(floraSpecies, query);
//                            Intent intent = new Intent(getApplicationContext(), FloraSpeciesDetailActivity.class);
//                            intent.putExtra("selectedSpeciesId", selectedId);
//                            intent.putExtra("selectedSpeciesName", query);
//                            startActivity(intent);
//                        }
//                    });
//                }
//            }
//        });

        mFloraSpeciesListViewModel.getFloraSpecies().observe(this, new Observer<Resource<List<FloraSpecies>>>() {
            @Override
            public void onChanged(Resource<List<FloraSpecies>> listResource) {
                if(listResource != null) {
                    Log.d(TAG, "onChanged: status" + listResource.status);

                    if(listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING: {

                            }
                            case ERROR: {
                                if(listResource.message != null && !mDownloading) {
                                    mSpeciesIndex = 0;
                                    mDownloadActionMenuItem.setEnabled(true);
                                    mDownloadIndicatorWrapper.setVisibility(View.GONE);
                                    System.out.println("--->" + listResource.message);
                                    Toast.makeText(BaseActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case SUCCESS: {
                                if(mAllowedToDownload)
                                    mDownloadActionMenuItem.setEnabled(false);
                                //mDownloadIndicatorWrapper.setVisibility(View.VISIBLE);
                                mSpeciesDownload = listResource.data;
                                mCurrentPreloadImageSize = "small";
                                preloadImageData();
                                break;
                            }
                        }
                    }
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

    public void setFirstTimeAppOpenedInSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(FIRST_TIME_OPEN_PREFERENCES, MODE_PRIVATE);
        if(prefs.contains("appOpenedFirstTime")) {
            boolean appOpenedFirstTime = prefs.getBoolean("appOpenedFirstTime", false);
        } else {
            SharedPreferences.Editor editor = getSharedPreferences(FIRST_TIME_OPEN_PREFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean("appOpenedFirstTime", true);
            editor.commit();
            openDownloadDialog();
        }
    }

    public void openDownloadDialog() {
        DownloadDataDialog downloadDataDialog = new DownloadDataDialog();
        downloadDataDialog.show(getSupportFragmentManager(), "download dialog");
    }

    @Override
    public void onYesDownloadClicked() {
        mDownloadActionMenuItem.setEnabled(false);
        mDownloadIndicatorWrapper.setVisibility(View.VISIBLE);
        mDownloadTextIndicator.setText("Starting...");

        initCancelDownloadButton();
        mAllowedToDownload = true;
        subscribeObservers();
        mFloraSpeciesListViewModel.getFloraSpeciesApi("all", null, null);
    }

    private void initCancelDownloadButton() {
        mCancelDownload = findViewById(R.id.tv_cancel_download);
        mCancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDownload();
            }

        });
    }

    private void resetDownload() {
        mAllowedToDownload = false;
        mSpeciesIndex = 0;
        mAdditionalImagesIndex = 0;
        mDownloadIndicatorWrapper.setVisibility(View.GONE);
        mDownloadActionMenuItem.setEnabled(true);
        mDownloadProgressBarIndicator.setProgress(0);
        mCurrentPreloadImageSize="small";
        mDownloading = false;
    }

    private void preloadImageData() {
        if(mSpeciesDownload.size() > 0 && mSpeciesIndex <= mSpeciesDownload.size()) {
            FloraSpecies floraSpecies = mSpeciesDownload.get(mSpeciesIndex);
            List<ImageBundle> images = floraSpecies.getAdditionalImages();
            String imageUrl = "";

            if(mAllowedToDownload) {
                if(mCurrentPreloadImageSize == "small" || mCurrentPreloadImageSize == "medium"){

                    for (ImageBundle image : images) {
                        if(mCurrentPreloadImageSize == "small"){
                            imageUrl = image.getImageSmall();
                            preloadImageGlide(imageUrl, images.size());
                        }
                        else if(mCurrentPreloadImageSize == "medium") {
                            imageUrl = image.getImageMedium();
                            preloadImageGlide(imageUrl, images.size());
                        }
                    }
                } else if(mCurrentPreloadImageSize == "thumbnail") {
                    ImageBundle image = mSpeciesDownload.get(mSpeciesIndex).getMainImage();
                    imageUrl = image.getImageThumbnail();
                    preloadImageGlide(imageUrl, images.size());
                }
            }
        }
    }

    private void preloadImageGlide(String imageUrl, int imageSize) {
        RequestManager rm = Glide.with(getApplicationContext());
        rm.load(imageUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //cancelSynchronous();
                        imageDone(imageSize);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageDone(imageSize);
                        return false;
                    }
                })
                .submit();
    }

    private void imageDone(int imageSize) {
        System.out.println("mSpeciesIndex " + mSpeciesIndex);
        System.out.println("mAdditionalImagesIndex " + mAdditionalImagesIndex);
        System.out.println("mCurrentPreloadImageSize " + mCurrentPreloadImageSize);

        mDownloading = true;

        if(mAdditionalImagesIndex >= imageSize) {
            mAdditionalImagesIndex = 0;
            mCurrentPreloadImageSize="small";
        }

        if(imageSize-1 == mAdditionalImagesIndex && mCurrentPreloadImageSize == "small") {
            mAdditionalImagesIndex = 0;
            mCurrentPreloadImageSize = "medium";
            if(mSpeciesIndex < mSpeciesDownload.size())
                preloadImageData();
        } else if (imageSize-1 == mAdditionalImagesIndex && mCurrentPreloadImageSize == "medium") {
            mAdditionalImagesIndex = 0;
            mCurrentPreloadImageSize = "thumbnail";
            if(mSpeciesIndex < mSpeciesDownload.size())
                preloadImageData();
        } else if (mCurrentPreloadImageSize == "thumbnail") {
            mAdditionalImagesIndex = 0;
            mCurrentPreloadImageSize = "small";
            mSpeciesIndex++;
            updateIndicatorText();
            if(mSpeciesIndex < mSpeciesDownload.size())
                preloadImageData();
        }
        else {
            mAdditionalImagesIndex++;
        }
    }

    private void cancelSynchronous() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resetDownload();
                Toast.makeText(BaseActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateIndicatorText(){
        float speciesSize = mSpeciesDownload.size()-1;
        float speciesIndex = mSpeciesIndex;
        float percentage = ( speciesIndex / speciesSize ) * 100;

        runOnUiThread(new Runnable() {
            public void run() {
                if(percentage >= 100) {
                    mDownloadTextIndicator.setText("Done!");
                    mDownloading = false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadActionMenuItem.setEnabled(true);
                            mSpeciesIndex = 0;
                            //mDownloadDialog.dismiss();
                            mDownloadIndicatorWrapper.setVisibility(View.GONE);
                            Toast.makeText(BaseActivity.this, "Data downloaded for offline usage.", Toast.LENGTH_SHORT).show();
                        }
                    }, 500);

                } else {
                    mDownloadTextIndicator.setText(Integer.toString((int) percentage)  + "%");
                    mDownloadProgressBarIndicator.setProgress((int) percentage);
                }
            }
        });
    }
}
