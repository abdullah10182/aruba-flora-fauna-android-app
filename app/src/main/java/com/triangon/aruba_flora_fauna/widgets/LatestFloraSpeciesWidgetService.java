package com.triangon.aruba_flora_fauna.widgets;

import android.app.IntentService;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.requests.FloraSpeciesApi;
import com.triangon.aruba_flora_fauna.requests.ServiceGenerator;
import com.triangon.aruba_flora_fauna.requests.responses.FloraSpeciesListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LatestFloraSpeciesWidgetService extends JobService {
    private static final String TAG = "LatestFloraSpeciesWidge";
    private static final String LATEST_SPECIES_PREFERENCES = "myPrefrences";
    private boolean jobCancelled = false;

    public static final String ACTION_GET_LATEST_FLORA_SPECIES =
            " com.triangon.aruba_flora_fauna.action.get_latest_flora_species";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        handleActionGetLatestFloraSpecies(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobCancelled = true;
        return false;
    }

    private void handleActionGetLatestFloraSpecies(JobParameters jobParameters) {
        if(jobCancelled)
            return;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, LatestFloraSpeciesAppWidget.class));

        FloraSpeciesApi floraSpeciesApi = ServiceGenerator.getFloraSpeciesApi();

        Call<FloraSpeciesListResponse> responseCall = floraSpeciesApi
                .getFloraSpeciesSuggestions("created");

        responseCall.enqueue(new Callback<FloraSpeciesListResponse>() {
            @Override
            public void onResponse(Call<FloraSpeciesListResponse> call, Response<FloraSpeciesListResponse> response) {
                if(response.code() == 200) {
                    List<FloraSpecies> speciesList = new ArrayList<>(response.body().getFloraSpecies());
                    jobFinished(jobParameters, false);
                    LatestFloraSpeciesAppWidget.getLatestFloraSpeciesWidget(getApplicationContext(), appWidgetManager, appWidgetIds, speciesList);

                } else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string() );
                    } catch (IOException e) {
                        e.printStackTrace();
                        List<FloraSpecies> speciesList = new ArrayList<>();
                        LatestFloraSpeciesAppWidget.getLatestFloraSpeciesWidget(getApplicationContext(), appWidgetManager, appWidgetIds, speciesList);
                    }
                }
            }

            @Override
            public void onFailure(Call<FloraSpeciesListResponse> call, Throwable t) {
                List<FloraSpecies> speciesList = new ArrayList<>();
                LatestFloraSpeciesAppWidget.getLatestFloraSpeciesWidget(getApplicationContext(), appWidgetManager, appWidgetIds, speciesList);
                Log.w("MyTag", "requestFailed", t);
            }
        });

    }
}
