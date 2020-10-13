package com.flexapp.flex.adapters;

import android.util.Log;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.flexapp.flex.interfaces.ITodayFragment;
import com.flexapp.flex.viewmodels.MealViewModel;
import java.util.ArrayList;

public class BindingAdapters {
    private static String S_TAG = "com.flexapp.flex.adapters.BindingAdapters";

    @BindingAdapter(value = {"app:meals", "app:itodayfrag"}, requireAll = true)
    public static void setMealList(RecyclerView view, ArrayList<MealViewModel> meals, ITodayFragment todayFragment) {
        Log.v(S_TAG, "setMealList...");
        if (meals != null) {
            if (view.getLayoutManager() == null) {
                Log.i(S_TAG, "layout manager is null, creating...");
                view.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
            }
            MealAdapters timelineAdapter = (MealAdapters) view.getAdapter();
            if (timelineAdapter == null) {
                Log.i(S_TAG, "Product Adapter is null, creating...");
                view.setAdapter(new MealAdapters(todayFragment, meals));
                return;
            }
            timelineAdapter.updateList(meals);
            timelineAdapter.notifyDataSetChanged();
        }
    }
}
