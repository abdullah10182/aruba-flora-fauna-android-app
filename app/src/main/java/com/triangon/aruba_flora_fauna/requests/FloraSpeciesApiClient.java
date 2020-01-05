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
    private RetrieveFloraSpeciesRunnable mRetrieveFloraSpeciesRunnable;

    public static FloraSpeciesApiClient getInstance() {
        if(instance == null) {
            instance = new FloraSpeciesApiClient();
        }

        return instance;
    }

    private FloraSpeciesApiClient() {
        mFloraSpecies = new MutableLiveData<>();
    }

    public LiveData<List<FloraSpecies>> getFloraSpecies() {
        return mFloraSpecies;
    }

    public void getFloraSpeciesApi(String category) {
        if(mRetrieveFloraSpeciesRunnable != null) {
            mRetrieveFloraSpeciesRunnable = null;
        }
        mFloraSpecies.setValue(null);
        mRetrieveFloraSpeciesRunnable = new RetrieveFloraSpeciesRunnable(category);
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveFloraSpeciesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let user know call timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public class RetrieveFloraSpeciesRunnable implements Runnable {

        private String category;
        boolean cancelRequest;

        public RetrieveFloraSpeciesRunnable(String category) {
            this.category = category;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getFloraSpecies(category).execute();
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

        private Call<FloraSpeciesListResponse> getFloraSpecies(String category) {
            return ServiceGenerator.getFloraSpeciesApi().getFloraSpecies(category);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request");
        }
    }
}
