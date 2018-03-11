/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.bakingapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.bakingapp.Ingredient;
import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.Recipe;
import com.example.android.bakingapp.RecipeStep;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BAKING_APP_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    /* Variables that we will use to parse json into our objects */
    final static String COMMON_ID = "id";
    final static String RECIPE_NAME = "name";
    final static String RECIPE_INGREDIENTS = "ingredients";
    final static String RECIPE_IMAGE = "image";
    final static String INGREDIENTE_NAME = "ingredient";
    final static String INGREDIENTE_QUANTITY = "quantity";
    final static String INGREDIENTE_MEASURE = "measure";
    final static String RECIPE_STEPS = "steps";
    final static String RECIPE_STEP_SHORT_DESCRIPTION = "shortDescription";
    final static String RECIPE_STEP_DESCRIPTION = "description";
    final static String RECIPE_STEP_VIDEO_URL = "videoURL";
    final static String RECIPE_STEP_THUMBNAIL_DESCRIPTION = "thumbnailURL";

    final String STATUS_CODE = "status_code";


    /**
     * Builds the URL to get a raw json response
     *
     * @return The URL to use to query the server.
     */
    public static URL buildUrlForBakingApp() {

        Uri builtUri = Uri.parse(BAKING_APP_URL).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static List<Recipe> getRecipeArrayFromJson(Context context, String jsonBakingResponse)
            throws JSONException{

        List<Recipe> returnRecipes = new ArrayList<>();

        //GET ENTIRE JSON

        JSONArray rawJsonArray = new JSONArray(jsonBakingResponse);

        for (int i = 0; i < rawJsonArray.length(); i++) {

            //GET INDIVIDUAL RECIPE
            JSONObject singleRecipe = rawJsonArray.getJSONObject(i);

            Recipe tempRecipe = new Recipe();

            //POPULATES RECIPE
            tempRecipe.setId(singleRecipe.getString(COMMON_ID));
            tempRecipe.setName(singleRecipe.getString(RECIPE_NAME));
            tempRecipe.setImagePath(singleRecipe.getString(RECIPE_IMAGE));

            /*
            Handle ingredients of the recipe
             */

            JSONArray recipeIngredients = singleRecipe.getJSONArray(RECIPE_INGREDIENTS);

            List<Ingredient> ingredients = new ArrayList<>();

            for (int j = 0; j < recipeIngredients.length(); j++) {
                JSONObject singleIngredient = recipeIngredients.getJSONObject(j);

                Ingredient tempIngredient = new Ingredient();

                tempIngredient.setName(singleIngredient.getString(INGREDIENTE_NAME));
                tempIngredient.setMeasure(singleIngredient.getString(INGREDIENTE_MEASURE));
                tempIngredient.setQuantity(singleIngredient.getDouble(INGREDIENTE_QUANTITY));

                ingredients.add(tempIngredient);
            }

            tempRecipe.setIngredients(ingredients);


            /*
            Handle steps of the recipe
             */
            JSONArray recipeSteps = singleRecipe.getJSONArray(RECIPE_STEPS);

            List<RecipeStep> steps = new ArrayList<>();

            for (int k = 0; k < recipeSteps.length(); k++) {
                JSONObject singeStep = recipeSteps.getJSONObject(k);

                RecipeStep tempStep = new RecipeStep();

                tempStep.setId(singeStep.getString(COMMON_ID));
                tempStep.setShortDescription(singeStep.getString(RECIPE_STEP_SHORT_DESCRIPTION));
                tempStep.setDescription(singeStep.getString(RECIPE_STEP_DESCRIPTION));
                tempStep.setVideoURL(singeStep.getString(RECIPE_STEP_VIDEO_URL));
                tempStep.setThumbnailURL(singeStep.getString(RECIPE_STEP_THUMBNAIL_DESCRIPTION));

                steps.add(tempStep);
            }

            tempRecipe.setSteps(steps);
            //SET THE POPULATED RECIPE TO THE LIST OF RECIPES
            returnRecipes.add(tempRecipe);
        }

        return returnRecipes;
    }
}
