package com.triangon.aruba_flora_fauna.requests;

import com.triangon.aruba_flora_fauna.requests.responses.FloraSpeciesListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FloraSpeciesApi {

    @GET("aff-api/flora-species")
    Call<FloraSpeciesListResponse> getFloraSpecies(
            @Query("category")
            String category,
            @Query("species_id")
            String speciesId,
            @Query("q")
            String searchQuery
    );

    @GET("aff-api/flora-species-search-suggestions")
    Call<FloraSpeciesListResponse> getFloraSpeciesSuggestions();
}
