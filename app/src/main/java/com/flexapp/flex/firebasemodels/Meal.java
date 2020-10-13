package com.flexapp.flex.firebasemodels;

import com.flexapp.flex.utils.Utils;
import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class Meal implements Serializable {
    private int calories;
    private String description;
    private int fats;
    private String id;
    private String imageRef;
    private Utils.MEASURING_SYSTEM measuringSystem;
    private int proteins;
    private int sugar;
    private String title;

    public Meal() {
    }

    public Meal(String title2, String description2, String imageRef2, int calories2, int fats2, int proteins2, int sugar2) {
        this.title = title2;
        this.description = description2;
        this.imageRef = imageRef2;
        this.calories = calories2;
        this.fats = fats2;
        this.proteins = proteins2;
        this.sugar = sugar2;
    }

    public Meal(String id2, String title2, String description2, String imageRef2, int calories2, int fats2, int proteins2, int sugar2) {
        this.id = id2;
        this.title = title2;
        this.description = description2;
        this.imageRef = imageRef2;
        this.calories = calories2;
        this.fats = fats2;
        this.proteins = proteins2;
        this.sugar = sugar2;
    }

    public Meal(String id2, String title2, String description2, String imageRef2, int calories2, int fats2, int proteins2, int sugar2, Utils.MEASURING_SYSTEM measuringSystem2) {
        this.id = id2;
        this.title = title2;
        this.description = description2;
        this.imageRef = imageRef2;
        this.calories = calories2;
        this.fats = fats2;
        this.proteins = proteins2;
        this.sugar = sugar2;
        this.measuringSystem = measuringSystem2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getImageRef() {
        return this.imageRef;
    }

    public void setImageRef(String imageRef2) {
        this.imageRef = imageRef2;
    }

    public int getCalories() {
        return this.calories;
    }

    public void setCalories(int calories2) {
        this.calories = calories2;
    }

    public int getFats() {
        return this.fats;
    }

    public void setFats(int fats2) {
        this.fats = fats2;
    }

    public int getProteins() {
        return this.proteins;
    }

    public void setProteins(int proteins2) {
        this.proteins = proteins2;
    }

    public int getSugar() {
        return this.sugar;
    }

    public void setSugar(int sugar2) {
        this.sugar = sugar2;
    }

    public Utils.MEASURING_SYSTEM getMeasuringSystem() {
        return this.measuringSystem;
    }

    public void setMeasuringSystem(Utils.MEASURING_SYSTEM measuringSystem2) {
        this.measuringSystem = measuringSystem2;
    }

    public String toString() {
        return "Meal{id='" + this.id + '\'' + ", title='" + this.title + '\'' + ", description='" + this.description + '\'' + ", imageRef='" + this.imageRef + '\'' + ", calories=" + this.calories + ", fats=" + this.fats + ", proteins=" + this.proteins + ", sugar=" + this.sugar + ", measuringSystem=" + this.measuringSystem + '}';
    }
}
