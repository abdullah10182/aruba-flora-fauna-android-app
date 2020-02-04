package com.triangon.aruba_flora_fauna.repositories;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.persistence.AffDatabase;
import com.triangon.aruba_flora_fauna.persistence.FloraCategoryDao;
import com.triangon.aruba_flora_fauna.persistence.FloraSpeciesDao;
import com.triangon.aruba_flora_fauna.requests.ServiceGenerator;
import com.triangon.aruba_flora_fauna.requests.executors.AppExecutors;
import com.triangon.aruba_flora_fauna.requests.responses.ApiResponse;
import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;
import com.triangon.aruba_flora_fauna.requests.responses.FloraSpeciesListResponse;
import com.triangon.aruba_flora_fauna.utils.Constants;
import com.triangon.aruba_flora_fauna.utils.NetworkBoundResource;
import com.triangon.aruba_flora_fauna.utils.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import retrofit2.http.Query;

public class FloraSpeciesRepository {

    private static final String TAG = "FloraSpeciesRepository";
    private static FloraSpeciesRepository instance;
    private FloraSpeciesDao floraSpeciesDao;

    public static FloraSpeciesRepository getInstance(Context context) {
        if(instance == null) {
            instance = new FloraSpeciesRepository(context);
        }
        return instance;
    }

    private FloraSpeciesRepository(Context context) {
        floraSpeciesDao = AffDatabase.getInstance(context).getFloraSpeciesDao();
    }

    public LiveData<Resource<List<FloraSpecies>>> getFloraSpeciesApi(final String category, final String speciesId, final String searchQuery) {
        return new NetworkBoundResource<List<FloraSpecies>, FloraSpeciesListResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull FloraSpeciesListResponse item) {
                if(item.getFloraSpecies() != null) {
                    FloraSpecies[] floraSpecies = new FloraSpecies[item.getFloraSpecies().size()];

                    int index = 0;
                    for(long rowid: floraSpeciesDao.insertFloraSpecies((FloraSpecies[]) (item.getFloraSpecies().toArray(floraSpecies))))  {
                        if(rowid == -1) {
                            Log.d(TAG, "saveCallResult: this is cat is already in cache");
                            // if already exists, I don't want to set the category or timestamp b/c they will be erased
                            floraSpecies[index].setTimestamp((int) (System.currentTimeMillis() / 1000));
                            floraSpeciesDao.insertFloraSpecies(floraSpecies[index]);
                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<FloraSpecies> data) {
                return true; // always query the network for now TEMP
//                int currentTime = (int) (System.currentTimeMillis() / 1000);
//                int lastRefresh = 0;
//                if(data.size() > 0){
//                    lastRefresh = data.get(0).getTimestamp();
//                } else {
//                    return true;
//                }
//
//                if((currentTime - lastRefresh) >= Constants.FLORA_CATEGORY_REFRESH_TIME){
//                    Log.d(TAG, "shouldFetch: SHOULD REFRESH?! " + true);
//                    return true;
//                }
//                Log.d(TAG, "shouldFetch: SHOULD REFRESH?! " + false);
//                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<FloraSpecies>> loadFromDb() {
                if(category == "all")
                    return floraSpeciesDao.getAllFloraSpecies();
                else
                    return floraSpeciesDao.getFloraSpecies(category, speciesId, searchQuery);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<FloraSpeciesListResponse>> createCall() {
                return ServiceGenerator.getFloraSpeciesApi().getFloraSpecies( category, speciesId, searchQuery);
            }
        }.getAsLiveData();
    }

}
