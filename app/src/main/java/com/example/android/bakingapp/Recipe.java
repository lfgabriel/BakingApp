package com.example.android.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsitec219.franco on 22/01/18.
 */

public class Recipe implements Parcelable {

    String id;
    String name;
    List<Ingredient> ingredients;
    List<RecipeStep> steps;
    String imagePath;


    public Recipe() {}


    public Recipe(String id, String name, List<Ingredient> ingredients, List<RecipeStep> steps, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.imagePath = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public String getImagePath() { return imagePath; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.getId());
        out.writeString(this.getName());
        out.writeList(this.ingredients);
        out.writeList(this.steps);
        out.writeString(this.imagePath);
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        steps = new ArrayList<>();
        in.readList(this.steps, RecipeStep.class.getClassLoader());
        imagePath = in.readString();
    }
}
