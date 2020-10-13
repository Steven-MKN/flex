package com.flexapp.flex.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.flexapp.flex.utils.Utils;

public class SettingsViewModel extends BaseObservable {
    private String date;
    private String email;
    private Utils.GENDER gender;
    private Utils.MEASURING_SYSTEM measuringSystem;
    private String name;

    public SettingsViewModel() {
        this.name = "";
        this.email = "";
        this.date = "";
        this.measuringSystem = Utils.MEASURING_SYSTEM.METRIC;
        this.gender = Utils.GENDER.MALE;
    }

    public SettingsViewModel(String name2, String email2, String date2, Utils.MEASURING_SYSTEM measuringSystem2, Utils.GENDER gender2) {
        this.name = name2;
        this.email = email2;
        this.date = date2;
        this.measuringSystem = measuringSystem2;
        this.gender = gender2;
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        if (this.name != name2) {
            this.name = name2;
            notifyPropertyChanged(14);
        }
    }

    @Bindable
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email2) {
        if (this.email != email2) {
            this.email = email2;
            notifyPropertyChanged(6);
        }
    }

    @Bindable
    public String getDate() {
        return this.date;
    }

    public void setDate(String date2) {
        if (this.date != date2) {
            this.date = date2;
            notifyPropertyChanged(4);
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
    public Utils.GENDER getGender() {
        return this.gender;
    }

    public void setGender(Utils.GENDER gender2) {
        if (this.gender != gender2) {
            this.gender = gender2;
            notifyPropertyChanged(8);
        }
    }
}
