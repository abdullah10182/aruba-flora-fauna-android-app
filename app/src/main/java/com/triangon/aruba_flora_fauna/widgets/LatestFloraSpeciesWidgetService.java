package com.triangon.aruba_flora_fauna.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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

public class LatestFloraSpeciesWidgetService extends IntentService {
    private static final String TAG = "LatestFloraSpeciesWidge";

    public static final String ACTION_GET_LATEST_FLORA_SPECIES =
            " com.triangon.aruba_flora_fauna.action.get_latest_flora_species";

    public LatestFloraSpeciesWidgetService() {
        super("LatestFloraSpeciesWidgetService");
    }

    public static void startActionGetLatestFloraSpecies(Context context) {
        Intent intent = new Intent(context, LatestFloraSpeciesWidgetService.class);
        intent.setAction(ACTION_GET_LATEST_FLORA_SPECIES);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_LATEST_FLORA_SPECIES.equals(action)) {
                handleActionGetLatestFloraSpecies();
            }
        }
    }

    private void handleActionGetLatestFloraSpecies() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, LatestFloraSpeciesAppWidget.class));
        
        FloraSpeciesApi floraSpeciesApi = ServiceGenerator.getFloraSpeciesApi();

        Call<FloraSpeciesListResponse> responseCall = floraSpeciesApi
                .getFloraSpeciesSuggestions("created");

        responseCall.enqueue(new Callback<FloraSpeciesListResponse>() {
            @Override
            public void onResponse(Call<FloraSpeciesListResponse> call, Response<FloraSpeciesListResponse> response) {
                if(response.code() == 200) {
                    List<FloraSpecies> spieciesList = new ArrayList<>(response.body().getFloraSpecies());
                    LatestFloraSpeciesAppWidget.getLatestFloraSpeciesWidget(getApplicationContext(), appWidgetManager, appWidgetIds, spieciesList);

                } else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<FloraSpeciesListResponse> call, Throwable t) {

            }
        });

    }
}
