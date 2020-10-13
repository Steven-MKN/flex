package com.flexapp.flex.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.flexapp.flex.utils.Utils;

public class AddMealViewModel extends BaseObservable {
    private int calories;
    private String description;
    private int fats;
    private String id;
    private String imageRef;
    private Utils.MEASURING_SYSTEM measuringSystem;
    private int protein;
    private int sugar;
    private String title;

    public AddMealViewModel() {
        this.title = "";
        this.description = "";
        this.imageRef = "";
        this.calories = -1;
        this.fats = -1;
        this.protein = -1;
        this.sugar = -1;
    }

    public AddMealViewModel(String title2, String description2, String imageRef2, int calories2, int fats2, int protein2, int sugar2, Utils.MEASURING_SYSTEM measuringSystem2, String id2) {
        this.title = title2;
        this.description = description2;
        this.imageRef = imageRef2;
        this.calories = calories2;
        this.fats = fats2;
        this.protein = protein2;
        this.sugar = sugar2;
        this.measuringSystem = measuringSystem2;
        this.id = id2;
    }

    @Bindable
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        if (this.title != title2) {
            this.title = title2;
            notifyPropertyChanged(22);
        }
    }

    @Bindable
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        if (this.description != description2) {
            this.description = description2;
            notifyPropertyChanged(5);
        }
    }

    @Bindable
    public String getImageRef() {
        return this.imageRef;
    }

    public void setImageRef(String imageRef2) {
        if (this.imageRef != imageRef2) {
            this.imageRef = imageRef2;
            notifyPropertyChanged(11);
        }
    }

    @Bindable
    public int getCalories() {
        return this.calories;
    }

    public void setCalories(int calories2) {
        if (this.calories != calories2) {
            this.calories = calories2;
            notifyPropertyChanged(1);
        }
    }

    @Bindable
    public int getFats() {
        return this.fats;
    }

    public void setFats(int fats2) {
        if (this.fats != fats2) {
            this.fats = fats2;
            notifyPropertyChanged(7);
        }
    }

    @Bindable
    public int getProtein() {
        return this.protein;
    }

    public void setProtein(int protein2) {
        if (this.protein != protein2) {
            this.protein = protein2;
            notifyPropertyChanged(17);
        }
    }

    @Bindable
    public int getSugar() {
        return this.sugar;
    }

    public void setSugar(int sugar2) {
        if (this.sugar != sugar2) {
            this.sugar = sugar2;
            notifyPropertyChanged(18);
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public Utils.MEASURING_SYSTEM getMeasuringSystem() {
        return this.measuringSystem;
    }

    public void setMeasuringSystem(Utils.MEASURING_SYSTEM measuringSystem2) {
        this.measuringSystem = measuringSystem2;
    }
}
