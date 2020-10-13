package com.flexapp.flex.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class MealViewModel extends BaseObservable {
    private String description;
    private String id;
    private String imageRef;
    private String title;

    public MealViewModel(String title2, String description2, String imageRef2) {
        this.title = title2;
        this.description = description2;
        this.imageRef = imageRef2;
    }

    public MealViewModel(String title2, String description2, String imageRef2, String id2) {
        this.title = title2;
        this.description = description2;
        this.imageRef = imageRef2;
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
    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        if (this.id != id2) {
            this.id = id2;
            notifyPropertyChanged(10);
        }
    }

    public String toString() {
        return "MealViewModel{title='" + this.title + '\'' + ", description='" + this.description + '\'' + ", imageRef='" + this.imageRef + '\'' + ", id='" + this.id + '\'' + '}';
    }
}
