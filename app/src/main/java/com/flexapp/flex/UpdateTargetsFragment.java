package com.flexapp.flex;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.flexapp.flex.databinding.FragmentUpdateTargetsBinding;
import com.flexapp.flex.interfaces.IUpdateTargetsFragment;

public class UpdateTargetsFragment extends Fragment implements IUpdateTargetsFragment {
    private String TAG = getClass().getName();
    private FragmentUpdateTargetsBinding mBinding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(this.TAG, "onCreateView...");
        FragmentUpdateTargetsBinding inflate = FragmentUpdateTargetsBinding.inflate(inflater);
        this.mBinding = inflate;
        return inflate.getRoot();
    }

    public void onSaveClicked() {
    }
}
