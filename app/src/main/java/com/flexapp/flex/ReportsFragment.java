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
import com.flexapp.flex.databinding.FragmentReportsBinding;
import com.flexapp.flex.interfaces.IReportsFragment;
import com.flexapp.flex.repos.MainRepository;
import com.flexapp.flex.utils.Constants;
import com.flexapp.flex.viewmodels.ReportsViewModel;
import im.dacer.androidcharts.LineView;
import java.util.ArrayList;

public class ReportsFragment extends Fragment implements IReportsFragment {
    private String TAG = getClass().getName();
    private LineView caloriesLineView;
    private LineView heightLineView;
    private FragmentReportsBinding mBinding;
    public MainRepository.MatrixData matrixData;
    public MainRepository repo;
    private ReportsViewModel viewModel;
    private LineView weightLineView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView...");
        mBinding = FragmentReportsBinding.inflate(inflater);
        repo = MainRepository.getInstance();
        ReportsViewModel reportsViewModel = new ReportsViewModel();
        viewModel = reportsViewModel;
        mBinding.setData(reportsViewModel);
        mBinding.setParent(this);
        initViews();
        getData();
        return mBinding.getRoot();
    }

    public void initViews() {
        View view = mBinding.getRoot();
        heightLineView = view.findViewById(R.id.line_view_height);
        weightLineView = view.findViewById(R.id.line_view_weight);
        caloriesLineView = view.findViewById(R.id.line_view_calories);
    }

    public void getData() {
        getContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(Constants.IS_SUCCESSFUL, false)) {
                    matrixData = (MainRepository.MatrixData) intent.getSerializableExtra(MainRepository.MatrixData.class.getName());

                    context.unregisterReceiver(this);

                    getContext().registerReceiver(new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            intent.getIntExtra("weight", 0);
                            intent.getIntExtra("height", 0);
                            intent.getIntExtra(Constants.CALORIES, 0);

                            context.unregisterReceiver(this);
                            // loadViews(intent.getIntExtra("height", 0), intent.getIntExtra("weight", 0), intent.getIntExtra(Constants.CALORIES, 0));
                            //getMoData();
                        }
                    }, new IntentFilter(Constants.INTENT_GET_TARGETS_ACTION));
                    repo.getTargets(getContext());
                }
            }
        }, new IntentFilter(Constants.INTENT_GET_NUTRITIONAL_INFO_ACTION));
        repo.getNutritionalInfo(getContext());
    }

    public void onUpdateTargetsClick() {
        Log.v(TAG, "onUpdateTargetsClick...");
        NavHostFragment.findNavController(this).navigate(R.id.action_reportsFragment_to_updateTargetsFragment);
    }

    public void loadViews(int height, int weight, int calories) {
        Log.d(TAG, matrixData.dates.toString());
        Log.d(TAG, matrixData.calories.toString());
        Log.d(TAG, matrixData.weight.toString());
        Log.d(TAG, matrixData.height.toString());

        ArrayList<ArrayList<Integer>> caloriesDataList = new ArrayList<>();
        caloriesDataList.add(matrixData.calories);

        ArrayList<Integer> caloriesList = new ArrayList<>();
        for (int i = 0; i < matrixData.calories.size(); i++) {
            caloriesList.add(Integer.valueOf(calories));
        }

        caloriesLineView.setDrawDotLine(false);
        caloriesLineView.setShowPopup(2);
        caloriesLineView.setColorArray(new int[]{17170451, 17170455});
        caloriesLineView.setDataList(caloriesDataList);
    }

    public void getMoData() {
        ArrayList<Integer> cals = new ArrayList<>();
        cals.add(700);
        cals.add(1200);
        cals.add(800);
        cals.add(800);
        cals.add(1000);
        cals.add(500);
        cals.add(600);

        ArrayList<Integer> weight = new ArrayList<>();
        weight.add(72);
        weight.add(72);
        weight.add(73);
        weight.add(73);
        weight.add(73);
        weight.add(74);
        weight.add(74);

        ArrayList<Integer> height = new ArrayList<>();
        height.add(170);
        height.add(170);
        height.add(170);
        height.add(170);
        height.add(170);
        height.add(170);
        height.add(170);

        matrixData.calories = cals;
        matrixData.height = height;
        matrixData.weight = weight;

        loadViews(175, 75, 1200);
    }
}
