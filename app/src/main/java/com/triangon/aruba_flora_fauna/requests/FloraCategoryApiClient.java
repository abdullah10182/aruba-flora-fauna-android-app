package com.triangon.aruba_flora_fauna.requests;

import android.util.Log;

import com.triangon.aruba_flora_fauna.requests.executors.AppExecutors;
import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;

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

public class FloraCategoryApiClient {

    private static final String TAG = "FloraCategoryApiClient";

    private static FloraCategoryApiClient instance;
    private MutableLiveData<List<FloraCategory>> mFloraCategories;
    private RetrieveFloraCategoriesRunnalble mRetrieveFloraCategoriesRunnalble;

    public static FloraCategoryApiClient getInstance() {
        if(instance == null) {
            instance = new FloraCategoryApiClient();
        }
        return instance;
    }

    private FloraCategoryApiClient() {
        mFloraCategories = new MutableLiveData<>();
    }

    public LiveData<List<FloraCategory>> getFloraCategories() {
        return mFloraCategories;
    }

    public void getFloraCategoriesApi() {
        if(mRetrieveFloraCategoriesRunnalble != null) {
             mRetrieveFloraCategoriesRunnalble = null;
        }
        mRetrieveFloraCategoriesRunnalble = new RetrieveFloraCategoriesRunnalble();
        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveFloraCategoriesRunnalble );

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //let user know call timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveFloraCategoriesRunnalble implements Runnable{

        //private String parameterExample
        boolean cancelRequest;

        public RetrieveFloraCategoriesRunnalble() {
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getFloraCategories().execute();
                if(cancelRequest) {
                    return;
                }
                if(response.code() == 200) {
                    List<FloraCategory> list = new ArrayList<>(((FloraCategoryListResponse)response.body()).getFloraCategories());
                    mFloraCategories.postValue(list);
                } else {
                    String error = response.errorBody().string();
                    Log.e(TAG, "run: " + error);
                    mFloraCategories.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Call<FloraCategoryListResponse> getFloraCategories() {
            return ServiceGenerator.getFloraCategoryApi().getFloraCategories();
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request");
        }
    }


}
