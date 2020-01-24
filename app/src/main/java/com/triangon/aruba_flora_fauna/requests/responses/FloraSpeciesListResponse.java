package com.triangon.aruba_flora_fauna.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;

import java.util.List;

public class FloraSpeciesListResponse {

    @SerializedName("count")
    @Expose()
    private int count;

    @SerializedName("species")
    @Expose()
    private List<FloraSpecies> floraSpecies;

    @SerializedName("error")
    @Expose()
    private String error;

    public String getError() {
        return error;
    }

    public int getCount() {
        return count;
    }

    public List<FloraSpecies> getFloraSpecies() {
        return floraSpecies;
    }

    @Override
    public String toString() {
        return "FloraSpeciesListResponse{" +
                "count=" + count +
                ", floraSpecies=" + floraSpecies +
                ", error='" + error + '\'' +
                '}';
    }
}
