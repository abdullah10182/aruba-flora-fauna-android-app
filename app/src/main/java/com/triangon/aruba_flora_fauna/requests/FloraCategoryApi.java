package com.triangon.aruba_flora_fauna.requests;

import com.triangon.aruba_flora_fauna.requests.responses.ApiResponse;
import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FloraCategoryApi {

    @GET("aff-api/flora-categories")
    LiveData<ApiResponse<FloraCategoryListResponse>> getFloraCategories();

    // SEARCH
//    @GET("api/search")
//    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipe(
//            @Query("key") String key,
//            @Query("q") String query,
//            @Query("page") String page
//    );
}
