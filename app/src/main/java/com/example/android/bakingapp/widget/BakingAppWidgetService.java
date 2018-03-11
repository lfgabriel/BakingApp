package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class BakingAppWidgetService extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_LIST_INGREDIENTS = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String WIDGET_INGREDIENTS_LIST = "widget-ingredients-list";


    public BakingAppWidgetService() {
        super("BakingAppWidgetService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionListIngredients(Context context, ArrayList<String> ingredientsList) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_LIST_INGREDIENTS);
        intent.putExtra(WIDGET_INGREDIENTS_LIST, ingredientsList);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            ArrayList<String> ingredientsList = intent.getStringArrayListExtra(WIDGET_INGREDIENTS_LIST);
            Intent widgetIntent = new Intent(ACTION_LIST_INGREDIENTS);
            widgetIntent.setAction(ACTION_LIST_INGREDIENTS);
            widgetIntent.putExtra(WIDGET_INGREDIENTS_LIST, ingredientsList);
            sendBroadcast(widgetIntent);
        }
    }
}
