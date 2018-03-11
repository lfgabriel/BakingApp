package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.utilities.NetworkUtils;
import com.example.android.bakingapp.utilities.SimpleIdlingResource;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecipesRecyclerView;
    private RecipesAdapter mRecipesAdapter;
    Context context = this;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private static final int LOADER_ID = 706;

    List<Recipe> mRecipeList = new ArrayList<>();

    private SimpleIdlingResource mIdlingResource;

    public static String RECIPE_BUNDLE = "recipe-bundle";
    public static String RECIPE_TO_BE_DETAILED = "recipe-to-be-detailed";
    public static String RECIPE_STEP_INDEX = "recipe-step-index";
    public static String RECIPE_STEP_LIST = "recipe-step-list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INITIALIZE LAYOUT ITEMS
        mRecipesRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_recipes);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecipesRecyclerView.setLayoutManager(linearLayoutManager);
        mRecipesRecyclerView.setHasFixedSize(true);

        mRecipesAdapter = new RecipesAdapter(context, this);
        mRecipesRecyclerView.setAdapter(mRecipesAdapter);

        getIdlingResource();
        mIdlingResource.setIdle(false);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        mIdlingResource.setIdle(true);
    }


    @Override
    public Loader<List<Recipe>> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public List<Recipe> loadInBackground() {
                URL bakingAppURL = NetworkUtils.buildUrlForBakingApp();

                try {

                    String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(bakingAppURL);

                    mRecipeList = NetworkUtils.getRecipeArrayFromJson(MainActivity.this, jsonMoviesResponse);

                    return mRecipeList;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> recipeList) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (recipeList != null) {
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecipesRecyclerView.setVisibility(View.VISIBLE);

            //SET DATA OF ADAPTER
            Log.e(TAG, "Finish onLoadFinished");
            mRecipesAdapter.setRecipesData(recipeList);

        }
        else {
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mRecipesRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {

    }

    @Override
    public void onClickRecipe(Recipe recipeToBeDetailed) {
        Log.e(TAG, "Recipe: " + recipeToBeDetailed.getName());

        Intent recipeDetailIntent = new Intent(this, RecipeDetailActivity.class);
        recipeDetailIntent.putExtra(RECIPE_TO_BE_DETAILED, recipeToBeDetailed);
        startActivity(recipeDetailIntent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
