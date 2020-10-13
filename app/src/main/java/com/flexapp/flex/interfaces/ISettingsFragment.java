package com.flexapp.flex.interfaces;

import com.flexapp.flex.utils.Utils;

public interface ISettingsFragment {
    void onDateClicked();

    void onGenderChange(Utils.GENDER gender);

    void onLogoutClicked();

    void onMeasureSystemChange(Utils.MEASURING_SYSTEM measuring_system);

    void onResetPasswordClicked();

    void onSaveClicked();
}
