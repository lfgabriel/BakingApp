package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsitec219.franco on 24/01/18.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipesDetailAdapter.RecipesDetailAdapterOnClickHandler {

    private String TAG = RecipeDetailActivity.class.getSimpleName();

    private Recipe mRecipe;
    private boolean isTablet;

    private static final String TAG_RECIPE_DETAIL_FRAGMENT = "recipe-detail-fragment";
    public static final String TAG_RECIPE_DETAIL_STEP_FRAGMENT = "recipe-detail-step-fragment";

    private TextView mErrorMessageDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mRecipe = getIntent().getExtras().getParcelable(MainActivity.RECIPE_TO_BE_DETAILED);

        if (getSupportFragmentManager().findFragmentByTag(TAG_RECIPE_DETAIL_FRAGMENT) == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(MainActivity.RECIPE_TO_BE_DETAILED, mRecipe);
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail_activity, recipeDetailFragment, TAG_RECIPE_DETAIL_FRAGMENT)
                    .addToBackStack(null)
                    .commit();
        }

        //Check if the device is a tablet
        //linear_layout_tablet will only be created in tablets
        if(findViewById(R.id.linear_layout_tablet) != null) {
            Log.e(TAG, "Tablet");
            if (getSupportFragmentManager().findFragmentByTag(TAG_RECIPE_DETAIL_STEP_FRAGMENT) == null) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(MainActivity.RECIPE_STEP_LIST, (ArrayList<RecipeStep>) mRecipe.getSteps());
                bundle.putInt(MainActivity.RECIPE_STEP_INDEX, 0);
                RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                recipeStepDetailFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_detail_activity_right, recipeStepDetailFragment, TAG_RECIPE_DETAIL_STEP_FRAGMENT)
                        .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipe != null)
            outState.putParcelable(MainActivity.RECIPE_BUNDLE, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClickRecipeStep(List<RecipeStep> recipeStepList, int recipeStepIndex) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MainActivity.RECIPE_STEP_LIST, (ArrayList) recipeStepList);
        bundle.putInt(MainActivity.RECIPE_STEP_INDEX, recipeStepIndex);
        Log.e(TAG, "Step : " + recipeStepIndex + 1);

        //IF IT IS A TABLET
        if(findViewById(R.id.linear_layout_tablet) != null) {
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_detail_activity_right, recipeStepDetailFragment, TAG_RECIPE_DETAIL_STEP_FRAGMENT)
                .commit();
            return;
        }

        if (getSupportFragmentManager().findFragmentByTag(TAG_RECIPE_DETAIL_STEP_FRAGMENT) == null) {
            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
            recipeStepDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_detail_activity, recipeStepDetailFragment,
                            TAG_RECIPE_DETAIL_STEP_FRAGMENT)
                    .addToBackStack("recipe")
                    .commit();
        }


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            getSupportFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }



}
