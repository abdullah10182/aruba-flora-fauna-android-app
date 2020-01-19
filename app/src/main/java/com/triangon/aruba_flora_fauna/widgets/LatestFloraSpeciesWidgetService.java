package com.triangon.aruba_flora_fauna.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;


public class LatestFloraSpeciesWidgetService extends IntentService {
    private static final String TAG = "LatestFloraSpeciesWidge";
    private static final String LATEST_SPECIES_PREFERENCES = "myPrefrences";

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

        SharedPreferences prefs = getSharedPreferences(LATEST_SPECIES_PREFERENCES, MODE_PRIVATE);
        String latestFloraSpecies = prefs.getString("latestFloraSpecies", "");

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        List<FloraSpecies> floraSpeciesList = new ArrayList<>();
        floraSpeciesList = gson.fromJson(latestFloraSpecies, new TypeToken<List<FloraSpecies>>(){}.getType());

        LatestFloraSpeciesAppWidget.getLatestFloraSpeciesWidget(getApplicationContext(), appWidgetManager, appWidgetIds, floraSpeciesList);

    }
}
