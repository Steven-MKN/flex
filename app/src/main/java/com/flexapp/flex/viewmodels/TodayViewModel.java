package com.flexapp.flex.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.flexapp.flex.utils.Utils;

import java.util.ArrayList;

public class TodayViewModel extends BaseObservable {
    private int height;
    private ArrayList<MealViewModel> mealViewModels;
    private Utils.MEASURING_SYSTEM measuringSystem;
    private int weight;

    public TodayViewModel() {
        this.weight = 0;
        this.height = 0;
        this.measuringSystem = Utils.MEASURING_SYSTEM.METRIC;
        this.mealViewModels = new ArrayList<>();
    }

    public TodayViewModel(int weight2, int height2, Utils.MEASURING_SYSTEM measuringSystem2, ArrayList<MealViewModel> mealViewModels2) {
        this.weight = weight2;
        this.height = height2;
        this.measuringSystem = measuringSystem2;
        this.mealViewModels = mealViewModels2;
    }

    @Bindable
    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight2) {
        if (this.weight != weight2) {
            this.weight = weight2;
            notifyPropertyChanged(23);
        }
    }

    @Bindable
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        if (this.height != height2) {
            this.height = height2;
            notifyPropertyChanged(9);
        }
    }

    @Bindable
    public Utils.MEASURING_SYSTEM getMeasuringSystem() {
        return this.measuringSystem;
    }

    public void setMeasuringSystem(Utils.MEASURING_SYSTEM measuringSystem2) {
        if (this.measuringSystem != measuringSystem2) {
            this.measuringSystem = measuringSystem2;
            notifyPropertyChanged(13);
        }
    }

    @Bindable
    public ArrayList<MealViewModel> getMealViewModels() {
        return this.mealViewModels;
    }

    public void setMealViewModels(ArrayList<MealViewModel> mealViewModels2) {
        if (this.mealViewModels != mealViewModels2) {
            this.mealViewModels = mealViewModels2;
            notifyPropertyChanged(12);
        }
    }
}
