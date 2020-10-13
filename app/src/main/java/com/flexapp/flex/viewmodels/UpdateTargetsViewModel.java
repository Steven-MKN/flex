package com.flexapp.flex.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class UpdateTargetsViewModel extends BaseObservable {
    private int targetCalories;
    private int targetHeight;
    private int targetWeight;

    public UpdateTargetsViewModel() {
    }

    public UpdateTargetsViewModel(int targetWeight2, int targetHeight2, int targetCalories2) {
        this.targetWeight = targetWeight2;
        this.targetHeight = targetHeight2;
        this.targetCalories = targetCalories2;
    }

    @Bindable
    public int getTargetWeight() {
        return this.targetWeight;
    }

    public void setTargetWeight(int targetWeight2) {
        if (this.targetWeight != targetWeight2) {
            this.targetWeight = targetWeight2;
        }
    }

    @Bindable
    public int getTargetHeight() {
        return this.targetHeight;
    }

    public void setTargetHeight(int targetHeight2) {
        if (this.targetHeight != targetHeight2) {
            this.targetHeight = targetHeight2;
        }
    }

    @Bindable
    public int getTargetCalories() {
        return this.targetCalories;
    }

    public void setTargetCalories(int targetCalories2) {
        if (this.targetCalories != targetCalories2) {
            this.targetCalories = targetCalories2;
        }
    }
}
