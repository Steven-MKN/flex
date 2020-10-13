package com.flexapp.flex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.flexapp.flex.databinding.FragmentTodayBinding;
import com.flexapp.flex.firebasemodels.Meal;
import com.flexapp.flex.firebasemodels.Today;
import com.flexapp.flex.interfaces.ITodayFragment;
import com.flexapp.flex.repos.MainRepository;
import com.flexapp.flex.utils.Constants;
import com.flexapp.flex.utils.Utils;
import com.flexapp.flex.viewmodels.MealViewModel;
import com.flexapp.flex.viewmodels.TodayViewModel;
import java.util.ArrayList;
import java.util.Iterator;

public class TodayFragment extends Fragment implements ITodayFragment {
    public String TAG = getClass().getName();
    private FragmentTodayBinding mBinding;
    public MainRepository repo;
    public TodayViewModel todayViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(this.TAG, "onCreateView...");
        this.mBinding = FragmentTodayBinding.inflate(inflater);
        this.repo = MainRepository.getInstance();
        this.todayViewModel = new TodayViewModel();
        getUpdatedData();
        this.mBinding.setData(this.todayViewModel);
        this.mBinding.setParent(this);
        return this.mBinding.getRoot();
    }

    /* access modifiers changed from: package-private */
    public void getUpdatedData() {
        getActivity().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "getTodayData onReceive...");
                if (intent.getBooleanExtra(Constants.IS_SUCCESSFUL, false)) {
                    Today today = (Today) intent.getSerializableExtra(Today.class.getName());
                    if (today == null) {
                        Log.d(TAG, "today is null");
                    } else {
                        Log.d(TAG, today.toString());
                        todayViewModel.setHeight(today.getHeight());
                        todayViewModel.setWeight(today.getWeight());
                        todayViewModel.setMeasuringSystem(repo.getMeasuringSystem());
                        ArrayList<MealViewModel> mealsViewModel = new ArrayList<>();
                        Iterator<Meal> it = today.getMeals().iterator();
                        while (it.hasNext()) {
                            Meal m = it.next();
                            mealsViewModel.add(new MealViewModel(m.getTitle(), m.getDescription(), m.getImageRef(), m.getId()));
                        }
                        Log.d(TAG, mealsViewModel.toString());
                        todayViewModel.setMealViewModels(mealsViewModel);
                    }
                }
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(Constants.INTENT_GET_TODAY_DATA_ACTION));
        this.repo.getTodayData(Utils.getTodayDate(), getContext());
    }

    public void addMealClick() {
        NavHostFragment.findNavController(this).navigate(R.id.action_todayFragment_to_addMealFragment);
    }

    public void editMealClick(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("mealId", id);
        NavHostFragment.findNavController(this).navigate(R.id.action_todayFragment_to_addMealFragment, bundle);
    }

    public void onPause() {
        TodayFragment.super.onPause();
        this.repo.setWeightAndHeight(this.todayViewModel.getWeight(), this.todayViewModel.getHeight());
    }
}
