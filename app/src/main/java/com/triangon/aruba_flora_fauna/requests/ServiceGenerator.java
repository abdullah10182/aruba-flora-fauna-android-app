package com.triangon.aruba_flora_fauna.requests;


import com.triangon.aruba_flora_fauna.utils.Constants;
import com.triangon.aruba_flora_fauna.utils.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.triangon.aruba_flora_fauna.utils.Constants.CONNECTION_TIMEOUT;
import static com.triangon.aruba_flora_fauna.utils.Constants.READ_TIMEOUT;
import static com.triangon.aruba_flora_fauna.utils.Constants.WRITE_TIMEOUT;

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder()
            //establish connection to server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            //time between eachbyte read between the server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            //time between each byte sent to the server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    //flora categories
    private static FloraCategoryApi floraCategoryApi = retrofit.create(FloraCategoryApi.class);

    public static FloraCategoryApi getFloraCategoryApi() {
        return floraCategoryApi;
    }

    //flora Species
    private static FloraSpeciesApi floraSpeciesApi = retrofit.create(FloraSpeciesApi.class);

    public static FloraSpeciesApi getFloraSpeciesApi() {
        return floraSpeciesApi;
    }
}
