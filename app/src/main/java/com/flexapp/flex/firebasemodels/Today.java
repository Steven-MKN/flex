package com.flexapp.flex.firebasemodels;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Today implements Serializable {
    public static final String HEIGHT = "height";
    public static final String WEIGHT = "weight";
    private Date date;
    private int height;
    private ArrayList<Meal> meals;
    private int weight;

    public Today() {
    }

    public Today(ArrayList<Meal> meals2, int weight2, int height2, Date date2) {
        this.meals = meals2;
        this.weight = weight2;
        this.height = height2;
        this.date = date2;
    }

    public ArrayList<Meal> getMeals() {
        return this.meals;
    }

    public void setMeals(ArrayList<Meal> meals2) {
        this.meals = meals2;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight2) {
        this.weight = weight2;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date2) {
        this.date = date2;
    }

    public String toString() {
        return "Today{meals=" + this.meals.toString() + ", weight=" + this.weight + ", height=" + this.height + ", date=" + this.date + '}';
    }
}
