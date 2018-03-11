package com.example.android.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Double2;

/**
 * Created by lsitec219.franco on 22/01/18.
 */

public class Ingredient implements Parcelable{

    String name;
    Double quantity;
    String measure;


    public Ingredient() {}

    public Ingredient(String name, Double quantity, String measure) {
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.getName());
        out.writeDouble(this.getQuantity());
        out.writeString(this.getMeasure());
    }

    public static final Parcelable.Creator<Ingredient> CREATOR
            = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    private Ingredient(Parcel in) {
        name = in.readString();
        quantity = in.readDouble();
        measure = in.readString();
    }
}
