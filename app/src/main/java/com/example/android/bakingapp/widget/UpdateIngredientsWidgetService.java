package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;

import java.util.List;


public class UpdateIngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListViewFactory(this.getApplicationContext(),
                intent));
    }

    class ListViewFactory implements RemoteViewsService.RemoteViewsFactory {

        private List<String> mIngredientsList;
        private Context mContext;

        public ListViewFactory(Context context, Intent intent) {
            mContext = context;
            mIngredientsList = intent.getStringArrayListExtra(BakingAppWidgetService.WIDGET_INGREDIENTS_LIST);
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            this.mIngredientsList = BakingAppWidgetProvider.mIngredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredientsList != null) {
                return mIngredientsList.size();
            } else
                return 1;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews widgetView = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_item_widget);
            if (mIngredientsList==null) {
                widgetView.setTextViewText(R.id.tv_ingredient_title_widget, "No data to display. Please select a recipe");
            } else {
                widgetView.setTextViewText(R.id.tv_ingredient_title_widget, mIngredientsList.get(i));
            }

            Intent fillInIntent = new Intent();
            widgetView.setOnClickFillInIntent(R.id.tv_ingredient_title_widget, fillInIntent);
            return widgetView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
