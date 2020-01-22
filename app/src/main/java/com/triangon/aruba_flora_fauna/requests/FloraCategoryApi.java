package com.triangon.aruba_flora_fauna.requests;

import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FloraCategoryApi {

    @GET("aff-api/flora-categories")
    LiveData<FloraCategoryListResponse> getFloraCategories();
}
