package com.triangon.aruba_flora_fauna.requests;

import com.google.gson.Gson;
import com.triangon.aruba_flora_fauna.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static FloraCategoryApi floraCategoryApi = retrofit.create(FloraCategoryApi.class);

    public static FloraCategoryApi getFloraCategoryApi() {
        return floraCategoryApi;
    }
}
