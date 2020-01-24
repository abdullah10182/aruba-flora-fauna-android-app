package com.triangon.aruba_flora_fauna.repositories;

import android.content.Context;
import android.util.Log;

import com.triangon.aruba_flora_fauna.models.FloraCategory;
import com.triangon.aruba_flora_fauna.persistence.FloraCategoryDao;
import com.triangon.aruba_flora_fauna.persistence.FloraCategoryDatabase;
import com.triangon.aruba_flora_fauna.requests.FloraCategoryApiClient;
import com.triangon.aruba_flora_fauna.requests.ServiceGenerator;
import com.triangon.aruba_flora_fauna.requests.executors.AppExecutors;
import com.triangon.aruba_flora_fauna.requests.responses.ApiResponse;
import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;
import com.triangon.aruba_flora_fauna.utils.Constants;
import com.triangon.aruba_flora_fauna.utils.NetworkBoundResource;
import com.triangon.aruba_flora_fauna.utils.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class FloraCategoryRepository {

    private static final String TAG = "FloraCategoryRepository";
    private static FloraCategoryRepository instance;
    private FloraCategoryDao floraCategoryDao;

    public static FloraCategoryRepository getInstance(Context context) {
        if(instance == null) {
            instance = new FloraCategoryRepository(context);
        }
        return instance;
    }

    private FloraCategoryRepository(Context context) {
        floraCategoryDao = FloraCategoryDatabase.getInstance(context).getFloraCategoryDao();
    }

    public LiveData<Resource<List<FloraCategory>>> getFloraCategoriesApi(/*final String query*/) {
        return new NetworkBoundResource<List<FloraCategory>, FloraCategoryListResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull FloraCategoryListResponse item) {
                if(item.getFloraCategories() != null) {
                    FloraCategory[] floraCategories = new FloraCategory[item.getFloraCategories().size()];

                    int index = 0;
                    for(long rowid: floraCategoryDao.insertFloraCategories((FloraCategory[]) (item.getFloraCategories().toArray(floraCategories))))  {
                        if(rowid == -1) {
                            Log.d(TAG, "saveCallResult: this is cat is already in cache");
                            // if already exists, I don't want to set the category or timestamp b/c they will be erased
                            floraCategories[index].setTimestamp((int) (System.currentTimeMillis() / 1000));
                            floraCategoryDao.insertFloraCategories(floraCategories[index]);
//                            floraCategoryDao.updateFloraCategory(
//                                    floraCategories[index].getId(),
//                                    floraCategories[index].getName(),
//                                    floraCategories[index].getDescription(),
//                                    floraCategories[index].getCategoryImage()
//                            );
                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<FloraCategory> data) {
                //return true; // always query the network for now TEMP
                int currentTime = (int) (System.currentTimeMillis() / 1000);
                int lastRefresh = 0;
                if(data.size() > 0){
                    lastRefresh = data.get(0).getTimestamp();
                } else {
                    return true;
                }

                if((currentTime - lastRefresh) >= 60){
                    Log.d(TAG, "shouldFetch: SHOULD REFRESH?! " + true);
                    return true;
                }
                Log.d(TAG, "shouldFetch: SHOULD REFRESH?! " + false);
                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<FloraCategory>> loadFromDb() {
                return floraCategoryDao.getFloraCategories();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<FloraCategoryListResponse>> createCall() {
                return ServiceGenerator.getFloraCategoryApi().getFloraCategories(/*params*/);
            }
        }.getAsLiveData();
    }

}
