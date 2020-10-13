package com.flexapp.flex.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.flexapp.flex.R;
import com.flexapp.flex.databinding.WidgetMealBinding;
import com.flexapp.flex.interfaces.ITodayFragment;
import com.flexapp.flex.viewmodels.MealViewModel;
import java.util.ArrayList;

public class MealAdapters extends RecyclerView.Adapter<MealAdapters.BindingHolder> {
    public static String S_TAG = "com.flexapp.flex.adapters.MealAdapters";
    private String TAG;
    private ArrayList<MealViewModel> meals;
    private ITodayFragment todayFragment;

    public MealAdapters(ITodayFragment todayFragment2, ArrayList<MealViewModel> meals2) {
        String name = getClass().getName();
        this.TAG = name;
        Log.v(name, "MealAdapters...");
        this.todayFragment = todayFragment2;
        this.meals = meals2;
    }

    public void updateList(ArrayList<MealViewModel> meals2) {
        this.meals = meals2;
    }

    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v(this.TAG, "onCreateViewHolder...");
        return new BindingHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.widget_meal, parent, false).getRoot());
    }

    public void onBindViewHolder(BindingHolder holder, int position) {
        Log.v(this.TAG, "onBindViewHolder...");
        holder.binding.setData(this.meals.get(position));
        holder.binding.setParent(this.todayFragment);
        holder.binding.executePendingBindings();
    }

    public int getItemCount() {
        Log.v(this.TAG, "getItemCount...");
        return this.meals.size();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder {
        WidgetMealBinding binding;

        public BindingHolder(View itemView) {
            super(itemView);
            Log.i(MealAdapters.S_TAG, "BindingHolder creating instance...");
            this.binding = DataBindingUtil.bind(itemView);
        }
    }
}
