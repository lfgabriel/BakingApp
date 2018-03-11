package com.example.android.bakingapp;

import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.bakingapp.widget.BakingAppWidgetService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsitec219.franco on 31/01/18.
 */

public class RecipeDetailFragment extends Fragment {

    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private RecipesDetailAdapter mRecipesDetailAdapter;
    private Recipe mRecipe;
    private static final String RECIPE_DETAIL_BUNDLE = "key-recipe_detail-bundle";
    private static final String SCROLL_VIEW_BUNDLE = "key-scroll_view-bundle";
    private ScrollView mIngredientsScrollView;
    private int[] mIngredientsScrollViewPosition = {0,0};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        //LAYOUT ITEMS
        TextView ingredientsTextView = (TextView) rootView.findViewById(R.id.tv_detail_recipe_ingredients);
        RecyclerView recyclerViewSteps = (RecyclerView) rootView.findViewById(R.id.rv_detail_steps);
        mIngredientsScrollView = (ScrollView) rootView.findViewById(R.id.sv_ingredients);

        //ADAPTER AND RECYCLER VIEW
        mRecipesDetailAdapter = new RecipesDetailAdapter((RecipesDetailAdapter.RecipesDetailAdapterOnClickHandler) getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSteps.setAdapter(mRecipesDetailAdapter);
        recyclerViewSteps.setLayoutManager(linearLayoutManager);

        //INITIALIZE RECIPE
        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_DETAIL_BUNDLE);
            mIngredientsScrollViewPosition = savedInstanceState.getIntArray(SCROLL_VIEW_BUNDLE);
        }
        else {
            mRecipe = getArguments().getParcelable(MainActivity.RECIPE_TO_BE_DETAILED);
        }

        if (mRecipe != null) {
            loadIngredientsIntoUIandWidget(mRecipe.getIngredients(), ingredientsTextView);
            loadStepsIntoUI((ArrayList<RecipeStep>) mRecipe.getSteps());
        }

        return rootView;
    }

    private void loadStepsIntoUI(List<RecipeStep> stepsList) {
        if (stepsList != null) {
            mRecipesDetailAdapter.setRecipesData(stepsList);
        }
    }

    private void loadIngredientsIntoUIandWidget(List<Ingredient> ingredientsList, TextView ingredientsTextView) {
        if (ingredientsList != null) {
            ArrayList<String> ingredientsListString = new ArrayList<>();
            ingredientsListString.add(mRecipe.getName());
            for (int x = 0; x < ingredientsList.size(); x++) {
                Ingredient i = ingredientsList.get(x);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (x == ingredientsList.size() - 1) {
                        CharSequence text = Html.fromHtml("* <b>" + i.getQuantity()
                                + " " + i.getMeasure() + " of " + i.getName(), 0);
                        ingredientsListString.add(text.toString());
                        ingredientsTextView.append(text);
                    } else {
                        CharSequence text = Html.fromHtml("* <b>" + i.getQuantity()
                                + " " + i.getMeasure() + " of " + i.getName(), 0);
                        ingredientsListString.add(text.toString());
                        ingredientsTextView.append(Html.fromHtml("* <b>" + i.getQuantity()
                                + " " + i.getMeasure() + " of " + i.getName() + "<br>", 0));
                    }
                } else {
                    if (x == ingredientsList.size() - 1) {
                        CharSequence text = Html.fromHtml("* <b>" + i.getQuantity()
                                + " " + i.getMeasure() + " of " + i.getName());
                        ingredientsListString.add(text.toString());
                        ingredientsTextView.append(text);
                    } else {
                        CharSequence text = Html.fromHtml("* <b>" + i.getQuantity()
                                + " " + i.getMeasure() + " of " + i.getName() + "<br>");
                        ingredientsListString.add(text.toString());
                        ingredientsTextView.append(text);
                    }
                }
            }
            mIngredientsScrollView.scrollTo(mIngredientsScrollViewPosition[0], mIngredientsScrollViewPosition[1]);
            BakingAppWidgetService.startActionListIngredients(getContext(), ingredientsListString);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mRecipe != null) {
            int[] i = new int[]{mIngredientsScrollView.getScrollX(),
                    mIngredientsScrollView.getScrollY()};
            outState.putIntArray(SCROLL_VIEW_BUNDLE, i);
            outState.putParcelable(RECIPE_DETAIL_BUNDLE, mRecipe);
        }
        super.onSaveInstanceState(outState);
    }


}
