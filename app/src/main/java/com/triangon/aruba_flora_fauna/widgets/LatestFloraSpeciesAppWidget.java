package com.triangon.aruba_flora_fauna.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.triangon.aruba_flora_fauna.R;
import com.triangon.aruba_flora_fauna.activities.FloraCategoryListActivity;
import com.triangon.aruba_flora_fauna.models.FloraSpecies;
import com.triangon.aruba_flora_fauna.requests.FloraSpeciesApi;
import com.triangon.aruba_flora_fauna.requests.ServiceGenerator;
import com.triangon.aruba_flora_fauna.requests.responses.FloraCategoryListResponse;
import com.triangon.aruba_flora_fauna.requests.responses.FloraSpeciesListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class LatestFloraSpeciesAppWidget extends AppWidgetProvider {
    private static final String TAG = "LatestSpeciesAppWidget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, List<FloraSpecies> latestSpecies) {

        //click widget go to app
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.latest_flora_species_app_widget);
        Intent intent = new Intent(context, FloraCategoryListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.ll_widget_wrapper, pendingIntent);

        String latestSpeciesList = createStringListSpecies(latestSpecies);
        views.setTextViewText(R.id.tv_latest_species_widget, latestSpeciesList);
       // displayWidgetText(latestSpecies, context, views);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static String createStringListSpecies(List<FloraSpecies> latestSpecies) {
        String speciesList = "";

        if(latestSpecies.size() > 0) {
            for(int i=0; i <= 10; i++){
                speciesList = speciesList + latestSpecies.get(i).getCommonName();
                speciesList = speciesList + "\n";
            }
        }

        return speciesList;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        List<FloraSpecies> speciesListEmpty = new ArrayList<>();
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, speciesListEmpty);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        LatestFloraSpeciesWidgetService.startActionGetLatestFloraSpecies(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void getLatestFloraSpeciesWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, List<FloraSpecies> latestSpecies) {
        for(int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, latestSpecies );
        }
    }

    public void displayWidgetText(List<FloraSpecies> latestSpecies, Context context, RemoteViews view) {

        String[] textArray = {"One", "Two", "Three", "Four"};
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for( int i = 0; i < textArray.length; i++ ) {
            RemoteViews textView = new RemoteViews(context.getPackageName(), R.layout.latest_flora_species_app_widget);
            //textView.setText(textArray[i]);
            view.addView(R.id.ll_widget_wrapper, textView);
        }

    }
}
