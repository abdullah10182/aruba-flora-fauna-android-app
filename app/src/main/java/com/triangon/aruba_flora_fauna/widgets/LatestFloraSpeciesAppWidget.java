package com.triangon.aruba_flora_fauna.widgets;

import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import static android.content.Context.JOB_SCHEDULER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class LatestFloraSpeciesAppWidget extends AppWidgetProvider {
    private static final String TAG = "LatestSpeciesAppWidget";
    private static final int JOB_ID = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, List<FloraSpecies> latestSpecies) {

        //click widget go to app
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.latest_flora_species_app_widget);
        Intent intent = new Intent(context, FloraCategoryListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.ll_widget_wrapper, pendingIntent);

        populateWdigetList(latestSpecies, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void populateWdigetList(List<FloraSpecies> latestSpecies, RemoteViews views) {
        if(latestSpecies.size() > 0) {

            views.setTextViewText(R.id.tv_latest_species_widget_title_0, latestSpecies.get(0).getCommonName() + " (" +
                    latestSpecies.get(0).getPapiamentoName() + ")");
            views.setTextViewText(R.id.tv_latest_species_widget_subtitle_0, "Category: " + latestSpecies.get(0).getCategoryName());

            views.setTextViewText(R.id.tv_latest_species_widget_title_1, latestSpecies.get(1).getCommonName() + " (" +
                    latestSpecies.get(1).getPapiamentoName() + ")");
            views.setTextViewText(R.id.tv_latest_species_widget_subtitle_1, "Category: " + latestSpecies.get(1).getCategoryName());

            views.setTextViewText(R.id.tv_latest_species_widget_title_2, latestSpecies.get(2).getCommonName() + " (" +
                    latestSpecies.get(2).getPapiamentoName() + ")");
            views.setTextViewText(R.id.tv_latest_species_widget_subtitle_2, "Category: " + latestSpecies.get(2).getCategoryName());

            views.setTextViewText(R.id.tv_latest_species_widget_title_3, latestSpecies.get(3).getCommonName() + " (" +
                    latestSpecies.get(3).getPapiamentoName() + ")");
            views.setTextViewText(R.id.tv_latest_species_widget_subtitle_3, "Category: " + latestSpecies.get(3).getCategoryName());

            views.setTextViewText(R.id.tv_latest_species_widget_title_4, latestSpecies.get(4).getCommonName() + " (" +
                    latestSpecies.get(4).getPapiamentoName() + ")");
            views.setTextViewText(R.id.tv_latest_species_widget_subtitle_4, "Category: " + latestSpecies.get(4).getCategoryName());

            views.setViewVisibility(R.id.ll_widget_list_wrapper, View.VISIBLE);
            views.setViewVisibility(R.id.tv_widget_loading, View.GONE);

        }

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
        ComponentName serviceComponent = new ComponentName(context, LatestFloraSpeciesWidgetService.class);
        JobInfo info = new JobInfo.Builder(JOB_ID, serviceComponent)
                .setPeriodic(15 * 60 * 1000) //period that the job runs
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

    @Override
    public void onDisabled(Context context) {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(JOB_ID);
    }

    public static void getLatestFloraSpeciesWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, List<FloraSpecies> latestSpecies) {
        for(int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, latestSpecies );
        }
    }
}

