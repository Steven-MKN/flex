package com.flexapp.flex;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.flexapp.flex.databinding.FragmentSettingsBinding;
import com.flexapp.flex.firebasemodels.Users;
import com.flexapp.flex.interfaces.ISettingsFragment;
import com.flexapp.flex.repos.MainRepository;
import com.flexapp.flex.utils.Constants;
import com.flexapp.flex.utils.Converters;
import com.flexapp.flex.utils.Utils;
import com.flexapp.flex.viewmodels.SettingsViewModel;
import java.util.Calendar;
import java.util.Date;

public class SettingsFragment extends Fragment implements ISettingsFragment {
    /* access modifiers changed from: private */
    public String TAG = getClass().getName();
    protected DatePickerDialog.OnDateSetListener dateChangedListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Log.v(TAG, "DatePickerDialog:onDateSet...");
            if (viewModel != null) {
                Calendar c = Calendar.getInstance();
                c.set(year, month, day, 0, 0, 0);
                viewModel.setDate(Converters.dateToString(c.getTime()));
            }
        }
    };
    /* access modifiers changed from: private */
    public FragmentSettingsBinding mBinding;
    /* access modifiers changed from: private */
    public MainRepository repo;
    /* access modifiers changed from: private */
    public SettingsViewModel viewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(this.TAG, "onCreateView...");
        this.mBinding = FragmentSettingsBinding.inflate(inflater);
        this.repo = MainRepository.getInstance();
        SettingsViewModel settingsViewModel = new SettingsViewModel();
        this.viewModel = settingsViewModel;
        this.mBinding.setData(settingsViewModel);
        this.mBinding.setParent(this);
        getSettings();
        return this.mBinding.getRoot();
    }

    public void onSaveClicked() {
        Log.v(this.TAG, "onSaveClicked...");
        getActivity().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                mBinding.progressBar.setVisibility(View.GONE);
                if (intent.getBooleanExtra(Constants.IS_SUCCESSFUL, false)) {
                    toast("Saved!");
                } else {
                    toast("Error, please try again");
                }
                goBack();
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(Constants.INTENT_SAVE_SETTINGS_ACTION));
        Users user = new Users(Converters.stringToDate(this.viewModel.getDate()), this.viewModel.getName(), this.viewModel.getGender(), this.viewModel.getMeasuringSystem());
        this.mBinding.progressBar.setVisibility(View.VISIBLE);
        this.repo.setSettings(user, getContext());
    }

    private void getSettings() {
        Log.v(this.TAG, "getSettings...");
        getContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Users user;
                Log.v(TAG, "onReceive...");
                if (intent.getBooleanExtra(Constants.IS_SUCCESSFUL, false)) {
                    user = (Users) intent.getSerializableExtra(Users.class.getName());
                } else {
                    user = new Users();
                }
                String d = Converters.dateToString(user.getDob());
                Log.d(TAG, d);
                viewModel.setName(user.getName());
                viewModel.setEmail(repo.getCurrentUser().getEmail());
                viewModel.setDate(d);
                viewModel.setMeasuringSystem(user.getMeasuringSystem());
                viewModel.setGender(user.getGender());
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(Constants.INTENT_GET_USER_DATA_ACTION));
        this.repo.getSettings(getContext());
    }

    public void onLogoutClicked() {
        Log.v(this.TAG, "onLogoutClicked...");
        this.mBinding.progressBar.setVisibility(View.VISIBLE);
        getContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "onReceive...");
                mBinding.progressBar.setVisibility(View.GONE);
                context.unregisterReceiver(this);
                restartApp();
            }
        }, new IntentFilter(Constants.INTENT_SIGN_OUT_ACTION));
        this.repo.logout(getContext());
    }

    public void onResetPasswordClicked() {
        Log.v(this.TAG, "onResetPasswordClicked...");
        this.mBinding.progressBar.setVisibility(View.VISIBLE);
        getContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.v(TAG, "onReceive...");
                mBinding.progressBar.setVisibility(View.GONE);
                if (intent.getBooleanExtra(Constants.IS_SUCCESSFUL, false)) {
                    alert("Successful", "A reset link was sent to your email");
                } else {
                    alert("Failed", intent.getStringExtra(Constants.MESSAGE));
                }
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(Constants.INTENT_RESET_PASSWORD_ACTION));
        this.repo.forgotPassword((String) null, getContext());
    }

    public void onMeasureSystemChange(Utils.MEASURING_SYSTEM m) {
        Log.v(this.TAG, "onMeasureSystemChange...");
        this.viewModel.setMeasuringSystem(m);
    }

    public void onGenderChange(Utils.GENDER g) {
        Log.v(this.TAG, "onGenderChange...");
        this.viewModel.setGender(g);
    }

    /* access modifiers changed from: private */
    public void toast(String m) {
        Log.v(this.TAG, "toast...");
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
    }

    /* access modifiers changed from: private */
    public void goBack() {
        Log.v(this.TAG, "goBack...");
        getActivity().onBackPressed();
    }

    /* access modifiers changed from: private */
    public void restartApp() {
        Log.v(this.TAG, "restartApp...");
        startActivity(new Intent(getContext(), StartActivity.class));
        getActivity().finish();
    }

    /* access modifiers changed from: private */
    public void alert(String title, String message) {
        Log.v(this.TAG, "alert...");
        new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Light_Dialog_Alert).setTitle(title).setMessage(message).setPositiveButton("OK", (DialogInterface.OnClickListener) null).setCancelable(true).show();
    }

    public void onDateClicked() {
        Log.v(this.TAG, "onDateClicked...");
        Date showingDate = Converters.stringToDate(this.viewModel.getDate());
        Date date = showingDate;
        new DatePickerFragment(getContext(), this.dateChangedListener, date, new Date(-1893456000), Utils.getTodayDate()).show(getParentFragmentManager(), Constants.CHANGE_DOB_DATE);
    }

    public static class DatePickerFragment extends DialogFragment {
        private int day;
        private Context fragCtx;
        private DatePickerDialog.OnDateSetListener listener;
        private long maxDate;
        private long minDate;
        private int month;
        private int year;

        public DatePickerFragment(Context context, DatePickerDialog.OnDateSetListener listener2, Date showingDate, Date minDate2, Date maxDate2) {
            this.fragCtx = context;
            this.listener = listener2;
            this.minDate = minDate2.getTime();
            this.maxDate = maxDate2.getTime();
            Calendar c = Calendar.getInstance();
            c.setTime(showingDate);
            this.year = c.get(1);
            this.month = c.get(2);
            this.day = c.get(5);
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog dialog = new DatePickerDialog(this.fragCtx, this.listener, this.year, this.month, this.day);
            dialog.getDatePicker().setMinDate(this.minDate);
            dialog.getDatePicker().setMaxDate(this.maxDate);
            return dialog;
        }
    }
}
