package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    public static ArrayList<String> mIngredientsList;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<String> mIngredientsList) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipe_ingredients);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.lv_widget, pendingIntent);
        //views.setOnClickPendingIntent(R.id.widget_baking_image , pendingIntent);

        Intent widgetIntent = new Intent(context, UpdateIngredientsWidgetService.class);
        widgetIntent.putStringArrayListExtra(BakingAppWidgetService.WIDGET_INGREDIENTS_LIST, mIngredientsList);
        views.setRemoteAdapter(R.id.lv_widget, widgetIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, ArrayList<String> mIngredientsList) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, mIngredientsList);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidgetProvider.class));
        if (intent.getAction().equals(BakingAppWidgetService.ACTION_LIST_INGREDIENTS)) {
            mIngredientsList=intent.getStringArrayListExtra(BakingAppWidgetService.WIDGET_INGREDIENTS_LIST);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);
        updateWidgets(context,appWidgetManager,appWidgetIds,mIngredientsList);
        super.onReceive(context, intent);
    }
}

