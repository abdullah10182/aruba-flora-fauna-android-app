package com.triangon.aruba_flora_fauna.requests;

import android.util.Log;

import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.requests.executors.AppExecutors;
import com.triangon.aruba_flora_fauna.requests.responses.FloraSpeciesListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;

import static com.triangon.aruba_flora_fauna.utils.Constants.NETWORK_TIMEOUT;

public class FloraSpeciesApiClient {

    private static final String TAG = "SpeciesApiClientApi";
    private static FloraSpeciesApiClient instance;
    private MutableLiveData<List<FloraSpecies>> mFloraSpecies;
    private MutableLiveData<List<FloraSpecies>> mFloraSpeciesSuggestions;
    private RetrieveFloraSpeciesRunnable mRetrieveFloraSpeciesRunnable;
    private RetrieveFloraSpeciesSuggestionsRunnable mRetrieveFloraSpeciesSuggestionsRunnable;

    public static FloraSpeciesApiClient getInstance() {
        if(instance == null) {
            instance = new FloraSpeciesApiClient();
        }

        return instance;
    }

    private FloraSpeciesApiClient() {
        mFloraSpecies = new MutableLiveData<>();
        mFloraSpeciesSuggestions = new MutableLiveData<>();
    }

    public LiveData<List<FloraSpecies>> getFloraSpecies() {
        return mFloraSpecies;
    }

    public LiveData<List<FloraSpecies>> getFloraSpeciesSuggestions() {
        return mFloraSpeciesSuggestions;
    }

    public void getFloraSpeciesApi(String category, String speciesId, String searchQuery) {
        if(mRetrieveFloraSpeciesRunnable != null) {
            mRetrieveFloraSpeciesRunnable = null;
        }

        mRetrieveFloraSpeciesRunnable = new RetrieveFloraSpeciesRunnable(category, speciesId, searchQuery);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveFloraSpeciesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let user know call timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void getFloraSpeciesSuggestionsApi() {
        if(mRetrieveFloraSpeciesSuggestionsRunnable != null) {
            mRetrieveFloraSpeciesSuggestionsRunnable = null;
        }

        mRetrieveFloraSpeciesSuggestionsRunnable = new RetrieveFloraSpeciesSuggestionsRunnable();
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveFloraSpeciesSuggestionsRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let user know call timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public class RetrieveFloraSpeciesRunnable implements Runnable {

        private String mCategory;
        private String mSpeciesId;
        private String mSearchQuery;
        boolean cancelRequest;

        public RetrieveFloraSpeciesRunnable(String category, String speciesId, String searchQuery) {
            mCategory = category;
            mSpeciesId = speciesId;
            mSearchQuery = searchQuery;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getFloraSpecies(mCategory, mSpeciesId, mSearchQuery).execute();
                if(cancelRequest) {
                    return;
                }
                if(response.code() == 200) {
                    List<FloraSpecies> list = new ArrayList<>(((FloraSpeciesListResponse)response.body()).getFloraSpecies());
                    mFloraSpecies.postValue(list);
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mFloraSpecies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Call<FloraSpeciesListResponse> getFloraSpecies(String category, String speciesId, String searchQuery) {
            return ServiceGenerator.getFloraSpeciesApi().getFloraSpecies(category, speciesId, searchQuery);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request");
        }
    }

    public class RetrieveFloraSpeciesSuggestionsRunnable implements Runnable {

        boolean cancelRequest;

        public RetrieveFloraSpeciesSuggestionsRunnable() {
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getFloraSpeciesSuggestions().execute();
                if(cancelRequest) {
                    return;
                }
                if(response.code() == 200) {
                    List<FloraSpecies> list = new ArrayList<>(((FloraSpeciesListResponse)response.body()).getFloraSpecies());
                    mFloraSpeciesSuggestions.postValue(list);
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mFloraSpeciesSuggestions.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Call<FloraSpeciesListResponse> getFloraSpeciesSuggestions() {
            return ServiceGenerator.getFloraSpeciesApi().getFloraSpeciesSuggestions();
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request");
        }
    }

    public void resetFloraSpecies() {
        mFloraSpecies.setValue(null);
    }

}
