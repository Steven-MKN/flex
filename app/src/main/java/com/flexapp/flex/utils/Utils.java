package com.flexapp.flex.utils;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    public enum GENDER {
        MALE,
        FEMALE
    }

    public enum MEASURING_SYSTEM {
        METRIC,
        IMPERIAL
    }

    public static Date getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), 0, 0, 0);
        return calendar.getTime();
    }

    public static String getWeightMeasure(MEASURING_SYSTEM m) {
        if (m == MEASURING_SYSTEM.METRIC) {
            return "kg";
        }
        return "lb";
    }

    public static String getSmallWeightMeasure(MEASURING_SYSTEM m) {
        if (m == MEASURING_SYSTEM.METRIC) {
            return "g";
        }
        return "oz";
    }

    public static String getLengthMeasure(MEASURING_SYSTEM m) {
        if (m == MEASURING_SYSTEM.METRIC) {
            return "cm";
        }
        return "in";
    }

    public static String getCalorieMeasure(MEASURING_SYSTEM m) {
        if (m == MEASURING_SYSTEM.METRIC) {
            return Constants.CALORIES;
        }
        return "joules";
    }
}
