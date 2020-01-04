package com.triangon.aruba_flora_fauna.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.triangon.aruba_flora_fauna.models.FloraCategory;

import java.util.List;

public class FloraCategoryListResponse {

    @SerializedName("count")
    @Expose()
    private int count;

    @SerializedName("flora_categories")
    @Expose()
    private List<FloraCategory> floraCategories;

    public int getCount() {
        return count;
    }

    public List<FloraCategory> getFloraCategories() {
        return floraCategories;
    }

    @Override
    public String toString() {
        return "FloraCategoryListResponse{" +
                "count=" + count +
                ", floraCategories=" + floraCategories +
                '}';
    }
}
