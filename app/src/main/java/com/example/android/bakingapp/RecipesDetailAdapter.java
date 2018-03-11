package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gabriel Franco on 23/01/17.
 */

public class RecipesDetailAdapter extends RecyclerView.Adapter<RecipesDetailAdapter.RecipesDetailAdapterViewHolder> {

    private Recipe mRecipe;
    private List<RecipeStep> mRecipeStepList;
    private Context context;


    private final RecipesDetailAdapterOnClickHandler mClickHandler;


    public interface RecipesDetailAdapterOnClickHandler {
        void onClickRecipeStep(List<RecipeStep> recipeStepList, int recipeStepIndex);
    }

    public RecipesDetailAdapter(RecipesDetailAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class RecipesDetailAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final TextView mStepsTextView;

        public RecipesDetailAdapterViewHolder(View view) {
            super(view);
            mStepsTextView = (TextView) view.findViewById(R.id.tv_recipe_step_short_description);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            mClickHandler.onClickRecipeStep(mRecipeStepList, getAdapterPosition());
        }
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public RecipesDetailAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_detail_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipesDetailAdapterViewHolder(view);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the recipe
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param recipesAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(RecipesDetailAdapterViewHolder recipesAdapterViewHolder, int position) {;
        recipesAdapterViewHolder.mStepsTextView.setText(mRecipeStepList.get(position).getShortDescription());
    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {

        if (mRecipeStepList == null) return 0;
        return mRecipeStepList.size();

    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param recipeStepData The new weather data to be displayed.
     */
    public void setRecipesData(List<RecipeStep> recipeStepData) {
        mRecipeStepList = recipeStepData;
        notifyDataSetChanged();
    }


}
