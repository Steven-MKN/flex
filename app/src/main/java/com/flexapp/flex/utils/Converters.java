package com.flexapp.flex.utils;

import android.net.Uri;
import androidx.databinding.InverseMethod;
import com.flexapp.flex.R;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Converters {
    public static String defaultImagePath = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.ic_baseline_camera_alt_24).toString();
    private static SimpleDateFormat simpleFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static String dateToString(Date date) {
        return simpleFormat.format(date);
    }

    public static Date stringToDate(String s) {
        try {
            return simpleFormat.parse(s);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @InverseMethod("stringToInt")
    public static String intToString(int i) {
        if (i == -1) {
            return "";
        }
        return String.valueOf(i);
    }

    public static int stringToInt(String s) {
        if (s.equals("")) {
            return -1;
        }
        return Integer.parseInt(s);
    }

    public static String getImage(String path) {
        if (path.isEmpty()) {
            return defaultImagePath;
        }
        File file = new File(path);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return defaultImagePath;
    }

    public static String arrListToString(ArrayList<Integer> arr) {
        String toReturn = "";
        Iterator<Integer> it = arr.iterator();
        while (it.hasNext()) {
            toReturn = toReturn + it.next().toString() + ",";
        }
        return toReturn;
    }

    public static ArrayList<Integer> stringToArrList(String s) {
        ArrayList<Integer> arr = new ArrayList<>();
        for (String part : s.split(",")) {
            arr.add(Integer.valueOf(Integer.parseInt(part)));
        }
        return arr;
    }

    public static int longToInt(long l) {
        return (int) l;
    }
}
