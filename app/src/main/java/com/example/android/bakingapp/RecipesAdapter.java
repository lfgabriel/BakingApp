package com.example.android.bakingapp;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Gabriel Franco on 23/01/17.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesAdapterViewHolder> {

    private List<Recipe> mRecipeData;
    private Context context;


    private final RecipesAdapterOnClickHandler mClickHandler;


    public interface RecipesAdapterOnClickHandler {
        void onClickRecipe(Recipe recipeDetails);
    }

    public RecipesAdapter(Context context, RecipesAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }


    public class RecipesAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public final TextView mRecipesTextView;
        public final ImageView mRecipesImageView;

        public RecipesAdapterViewHolder(View view) {
            super(view);
            mRecipesTextView = (TextView) view.findViewById(R.id.tv_recipe_name);
            mRecipesImageView = (ImageView) view.findViewById(R.id.iv_recipe_image);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipeDetails = mRecipeData.get(adapterPosition);
            mClickHandler.onClickRecipe(recipeDetails);
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
    public RecipesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipesAdapterViewHolder(view);
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
    public void onBindViewHolder(RecipesAdapterViewHolder recipesAdapterViewHolder, int position) {;
        recipesAdapterViewHolder.mRecipesTextView.setText(mRecipeData.get(position).getName());

        String recipeImage = mRecipeData.get(position).getImagePath();
        if ((recipeImage != null) && !recipeImage.isEmpty()) {
            Picasso.with(context).load(mRecipeData.get(position).getImagePath()).
                    into(recipesAdapterViewHolder.mRecipesImageView);
        }

    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {

        if (mRecipeData  == null) return 0;
        return mRecipeData.size();

    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param recipesData The new weather data to be displayed.
     */
    public void setRecipesData(List<Recipe> recipesData) {
        Log.e("oioi", "setRecipesData. recipesData size: " + recipesData.size());
        mRecipeData = recipesData;
        notifyDataSetChanged();
    }


}
