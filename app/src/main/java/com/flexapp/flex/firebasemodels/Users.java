package com.flexapp.flex.firebasemodels;

import com.flexapp.flex.repos.MainRepository;
import com.flexapp.flex.utils.Utils;
import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@IgnoreExtraProperties
public class Users implements Serializable {
    private Date dob;
    private Utils.GENDER gender;
    private Utils.MEASURING_SYSTEM measuringSystem;
    private String name;

    public Users() {
        this.dob = Calendar.getInstance().getTime();
        this.name = "";
        this.gender = Utils.GENDER.MALE;
        this.measuringSystem = Utils.MEASURING_SYSTEM.METRIC;
    }

    public Users(Date dob2, String name2, Utils.GENDER gender2, Utils.MEASURING_SYSTEM measuringSystem2) {
        this.dob = dob2;
        this.name = name2;
        this.gender = gender2;
        this.measuringSystem = measuringSystem2;
    }

    public Date getDob() {
        return this.dob;
    }

    public void setDob(Date dob2) {
        this.dob = dob2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public Utils.GENDER getGender() {
        return this.gender;
    }

    public void setGenderObj(Utils.GENDER gender2) {
        this.gender = gender2;
    }

    public void setGender(String g) {
        if (g.equals("f")) {
            this.gender = Utils.GENDER.FEMALE;
        } else {
            this.gender = Utils.GENDER.MALE;
        }
    }

    public Utils.MEASURING_SYSTEM getMeasuringSystem() {
        return this.measuringSystem;
    }

    public void setMeasuringSystem(String m) {
        if (m.equals("i")) {
            this.measuringSystem = Utils.MEASURING_SYSTEM.IMPERIAL;
        } else {
            this.measuringSystem = Utils.MEASURING_SYSTEM.METRIC;
        }
    }

    public void setMeasuringSystemObj(Utils.MEASURING_SYSTEM measuringSystem2) {
        this.measuringSystem = measuringSystem2;
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("dob", this.dob);
        map.put("name", this.name);
        String str = "m";
        map.put(MainRepository.SP.GENDER, this.gender == Utils.GENDER.MALE ? str : "f");
        if (this.measuringSystem != Utils.MEASURING_SYSTEM.METRIC) {
            str = "i";
        }
        map.put("measuringSystem", str);
        return map;
    }

    public String toString() {
        return "Users{dob=" + this.dob + ", name='" + this.name + '\'' + ", gender='" + this.gender + '\'' + ", measuringSystem=" + this.measuringSystem + '}';
    }
}
