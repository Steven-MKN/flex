package com.flexapp.flex.repos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.flexapp.flex.firebasemodels.Meal;
import com.flexapp.flex.firebasemodels.Today;
import com.flexapp.flex.firebasemodels.Users;
import com.flexapp.flex.utils.Constants;
import com.flexapp.flex.utils.Converters;
import com.flexapp.flex.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class MainRepository {
    private static MainRepository instance;
    public String TAG = getClass().getName();
    public FirebaseUser currentUser;
    public DatabaseReference database;
    public FirebaseAuth mAuth;
    public Utils.MEASURING_SYSTEM measuringSystem;
    public SharedPreferences sharedPreferences;

    public static class SP {
        public static final String CALORIES_LIST = "calories_list";
        public static final String GENDER = "gender";
        public static final String HEIGHT = "height";
        public static final String HEIGHT_LIST = "height_list";
        public static final String LAST_UPDATE_DATE = "history_up_to_date";
        public static final String MEASURING_SYSTEM = "measuring_system";
        public static final String WEIGHT = "weight";
        public static final String WEIGHT_LIST = "weight_list";

        private SP() {
        }
    }

    private MainRepository() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseAuth instance2 = FirebaseAuth.getInstance();
        this.mAuth = instance2;
        this.currentUser = instance2.getCurrentUser();
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }

    public void initRepo(SharedPreferences sp) {
        this.sharedPreferences = sp;
        this.measuringSystem = sp.getString(SP.MEASURING_SYSTEM, "m").equals("m") ? Utils.MEASURING_SYSTEM.METRIC : Utils.MEASURING_SYSTEM.IMPERIAL;
    }

    public FirebaseUser getCurrentUser() {
        Log.v(TAG, "getCurrentUser...");
        return currentUser;
    }

    public Utils.MEASURING_SYSTEM getMeasuringSystem() {
        Utils.MEASURING_SYSTEM measuring_system = measuringSystem;
        if (measuring_system == null) {
            return Utils.MEASURING_SYSTEM.METRIC;
        }
        return measuring_system;
    }

    public void logout(final Context context) {
        Log.v(TAG, "logout...");
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    FirebaseUser unused = currentUser = null;
                    context.sendBroadcast(new Intent(Constants.INTENT_SIGN_OUT_ACTION));
                }
            }
        });
        mAuth.signOut();
    }

    public void signUpUser(String email, String password, final Context context) {
        Log.v(TAG, "signUpUser...");
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    MainRepository mainRepository = MainRepository.this;
                    FirebaseUser unused = mainRepository.currentUser = mainRepository.mAuth.getCurrentUser();
                    context.sendBroadcast(new Intent(Constants.INTENT_REGISTER_ACTION));
                    return;
                }
                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                String access$100 = TAG;
                Log.i(access$100, "runnable running" + task.getException().getMessage());
                Intent intent = new Intent(Constants.INTENT_REGISTER_ACTION);
                intent.putExtra(Constants.MESSAGE, task.getException().getMessage());
                context.sendBroadcast(intent);
            }
        });
    }

    public void loginUser(String email, String password, final Context context) {
        Log.v(TAG, "loginUser...");
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmailAndPassword:success");
                    MainRepository mainRepository = MainRepository.this;
                    FirebaseUser unused = mainRepository.currentUser = mainRepository.mAuth.getCurrentUser();
                    Intent intent = new Intent(Constants.INTENT_LOGIN_ACTION);
                    intent.putExtra(Constants.IS_SUCCESSFUL, true);
                    context.sendBroadcast(intent);
                    return;
                }
                Log.d(TAG, "signInWithEmailAndPassword:failure");
                Intent intent2 = new Intent(Constants.INTENT_LOGIN_ACTION);
                intent2.putExtra(Constants.IS_SUCCESSFUL, false);
                intent2.putExtra(Constants.MESSAGE, task.getException().getMessage());
                context.sendBroadcast(intent2);
            }
        });
    }

    public void forgotPassword(String email, final Context context) {
        if (email == null && currentUser == null) {
            Intent intent = new Intent(Constants.INTENT_RESET_PASSWORD_ACTION);
            intent.putExtra(Constants.IS_SUCCESSFUL, false);
            context.sendBroadcast(intent);
            return;
        }
        if (email == null) {
            email = currentUser.getEmail();
        }
        Log.v(TAG, "forgotPassword...");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "sendPasswordResetEmail:success");
                    Intent intent = new Intent(Constants.INTENT_RESET_PASSWORD_ACTION);
                    intent.putExtra(Constants.IS_SUCCESSFUL, true);
                    context.sendBroadcast(intent);
                    return;
                }
                Log.d(TAG, "sendPasswordResetEmail:failed");
                Intent intent2 = new Intent(Constants.INTENT_RESET_PASSWORD_ACTION);
                intent2.putExtra(Constants.IS_SUCCESSFUL, false);
                intent2.putExtra(Constants.MESSAGE, task.getException().getMessage());
                context.sendBroadcast(intent2);
            }
        });
    }

    public void getTargets(final Context context) {
        Log.v(TAG, "getTargets...");
        database.child(currentUser.getUid()).child(Constants.TARGETS).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Intent intent = new Intent(Constants.INTENT_GET_TARGETS_ACTION);
                int weight = 0;
                int height = 0;
                int calories = 0;
                if (snapshot.exists()) {
                    try {
                        weight = Converters.longToInt(((Long) snapshot.child("weight").getValue()).longValue());
                        height = Converters.longToInt(((Long) snapshot.child("height").getValue()).longValue());
                        calories = Converters.longToInt(((Long) snapshot.child(Constants.CALORIES).getValue()).longValue());
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                }
                intent.putExtra(Constants.IS_SUCCESSFUL, true);
                intent.putExtra("weight", weight);
                intent.putExtra("height", height);
                intent.putExtra(Constants.CALORIES, calories);
                context.sendBroadcast(intent);
            }

            public void onCancelled(DatabaseError error) {
                Intent intent = new Intent(Constants.INTENT_GET_TARGETS_ACTION);
                intent.putExtra(Constants.IS_SUCCESSFUL, false);
                context.sendBroadcast(intent);
            }
        });
    }

    public void getTodayData(Date date, final Context context) {
        String str = TAG;
        Log.v(str, "getTodayData TodayViewModel..." + date.toString());
        database.child(currentUser.getUid()).child(Converters.dateToString(date)).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Meal m;
                Log.i(TAG, "getTodayDoc:successful");
                Log.i(TAG, snapshot.toString());
                Today today = new Today();
                if (snapshot.hasChild("weight")) {
                    long w = ((Long) snapshot.child("weight").getValue()).longValue();
                    today.setWeight((int) w);
                    sharedPreferences.edit().putInt("weight", (int) w).apply();
                } else {
                    today.setWeight(sharedPreferences.getInt("weight", 0));
                }
                if (snapshot.hasChild("height")) {
                    long h = ((Long) snapshot.child("height").getValue()).longValue();
                    today.setHeight((int) h);
                    sharedPreferences.edit().putInt("height", (int) h).apply();
                } else {
                    today.setHeight(sharedPreferences.getInt("height", 0));
                }
                today.setDate(Converters.stringToDate(snapshot.getKey()));
                ArrayList<Meal> mealArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals(Constants.CALORIES) && !dataSnapshot.getKey().equals("weight") && !dataSnapshot.getKey().equals("height") && (m = (Meal) dataSnapshot.getValue(Meal.class)) != null) {
                        m.setId(dataSnapshot.getKey());
                        mealArrayList.add(m);
                    }
                }
                today.setMeals(mealArrayList);
                Log.d(TAG, today.toString());
                Intent intent = new Intent(Constants.INTENT_GET_TODAY_DATA_ACTION);
                intent.putExtra(Constants.IS_SUCCESSFUL, true);
                intent.putExtra(Today.class.getName(), today);
                context.sendBroadcast(intent);
            }

            public void onCancelled(DatabaseError error) {
                Log.i(TAG, "getTodayDoc:failed");
                Intent intent = new Intent(Constants.INTENT_GET_TODAY_DATA_ACTION);
                intent.putExtra(Constants.IS_SUCCESSFUL, false);
                context.sendBroadcast(intent);
            }
        });
    }

    public void setWeightAndHeight(int weight, int height) {
        Log.v(TAG, "setWeight...");
        int spWeight = sharedPreferences.getInt("weight", -1);
        int spHeight = sharedPreferences.getInt("height", -1);
        if (spWeight == weight && spHeight == height) {
            Log.i(TAG, "weight and height unchanged, not saving...");
            return;
        }
        Log.i(TAG, "weight and height changed, saving...");
        sharedPreferences.edit().putInt("weight", weight).putInt("height", height).apply();
        HashMap<String, Object> map = new HashMap<>();
        map.put("weight", Integer.valueOf(weight));
        map.put("height", Integer.valueOf(height));
        database.child(currentUser.getUid()).child(Converters.dateToString(Utils.getTodayDate())).updateChildren(map);
        database.child(currentUser.getUid()).child(Constants.MATRIX).child(Converters.dateToString(Utils.getTodayDate())).updateChildren(map);
    }

    public void addTodayMeal(Meal meal, Date date, Context context) {
        Log.v(TAG, "addTodayMean...");
        if (meal.getId() != null) {
            updateMeal(meal, date, context);
        } else {
            addNewMeal(meal, date, context);
        }
    }

    public void getMeal(Date date, String id, final Context context) {
        Log.v(TAG, "getMeal...");
        database.child(currentUser.getUid()).child(Converters.dateToString(date)).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "getMealDoc:successful");
                Intent intent = new Intent(Constants.INTENT_GET_MEAL_ACTION);
                if (snapshot.exists()) {
                    Meal meal = (Meal) snapshot.getValue(Meal.class);
                    intent.putExtra(Constants.IS_SUCCESSFUL, true);
                    if (meal != null) {
                        meal.setMeasuringSystem(measuringSystem);
                        intent.putExtra(Meal.class.getName(), meal);
                    }
                } else {
                    intent.putExtra(Constants.IS_SUCCESSFUL, false);
                }
                context.sendBroadcast(intent);
            }

            public void onCancelled(DatabaseError error) {
                Intent intent = new Intent(Constants.INTENT_GET_MEAL_ACTION);
                intent.putExtra(Constants.IS_SUCCESSFUL, false);
                context.sendBroadcast(intent);
            }
        });
    }

    public void setSettings(Users user, final Context context) {
        Log.v(TAG, "setSettings...");
        String str = "m";
        SharedPreferences.Editor putString = sharedPreferences.edit().putString(SP.MEASURING_SYSTEM, user.getMeasuringSystem() == Utils.MEASURING_SYSTEM.METRIC ? str : "i");
        if (user.getGender() != Utils.GENDER.MALE) {
            str = "f";
        }
        putString.putString(SP.GENDER, str).apply();
        measuringSystem = user.getMeasuringSystem();
        database.child(currentUser.getUid()).child(Constants.SETTINGS).updateChildren(user.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                Intent intent = new Intent(Constants.INTENT_SAVE_SETTINGS_ACTION);
                if (task.isSuccessful()) {
                    intent.putExtra(Constants.IS_SUCCESSFUL, true);
                } else {
                    intent.putExtra(Constants.IS_SUCCESSFUL, false);
                }
                context.sendBroadcast(intent);
            }
        });
    }

    public void getSettings(final Context context) {
        Log.v(TAG, "getSettings...");
        database.child(currentUser.getUid()).child(Constants.SETTINGS).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Intent intent = new Intent(Constants.INTENT_GET_USER_DATA_ACTION);
                if (snapshot.exists()) {
                    Log.v(TAG, "getSettings:successful");
                    intent.putExtra(Constants.IS_SUCCESSFUL, true);
                    Users user = (Users) snapshot.getValue(Users.class);
                    if (user != null) {
                        intent.putExtra(Users.class.getName(), user);
                    }
                    context.sendBroadcast(intent);
                } else {
                    Log.v(TAG, "getSettings:empty");
                    intent.putExtra(Constants.IS_SUCCESSFUL, false);
                }
                context.sendBroadcast(intent);
            }

            public void onCancelled(DatabaseError error) {
                Log.v(TAG, "getSettings:failed");
                Intent intent = new Intent(Constants.INTENT_GET_USER_DATA_ACTION);
                intent.putExtra(Constants.IS_SUCCESSFUL, false);
                context.sendBroadcast(intent);
            }
        });
    }

    public void getNutritionalInfo(final Context context) {
        database.child(currentUser.getUid()).child(Constants.MATRIX).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Intent intent = new Intent(Constants.INTENT_GET_NUTRITIONAL_INFO_ACTION);
                if (snapshot != null) {
                    Log.d(TAG, snapshot.toString());
                    ArrayList<String> dates = new ArrayList<>();
                    ArrayList<Integer> calories = new ArrayList<>();
                    ArrayList<Integer> sugar = new ArrayList<>();
                    ArrayList<Integer> fats = new ArrayList<>();
                    ArrayList<Integer> protein = new ArrayList<>();
                    ArrayList<Integer> weight = new ArrayList<>();
                    ArrayList<Integer> height = new ArrayList<>();
                    for (DataSnapshot days : snapshot.getChildren()) {
                        dates.add(days.getKey());
                        long j = 0;
                        calories.add(Integer.valueOf(Converters.longToInt(days.child(Constants.CALORIES).exists() ? ((Long) days.child(Constants.CALORIES).getValue()).longValue() : 0)));
                        fats.add(Integer.valueOf(Converters.longToInt(days.child(Constants.FATS).exists() ? ((Long) days.child(Constants.FATS).getValue()).longValue() : 0)));
                        sugar.add(Integer.valueOf(Converters.longToInt(days.child(Constants.SUGAR).exists() ? ((Long) days.child(Constants.SUGAR).getValue()).longValue() : 0)));
                        protein.add(Integer.valueOf(Converters.longToInt(days.child(Constants.PROTEIN).exists() ? ((Long) days.child(Constants.PROTEIN).getValue()).longValue() : 0)));
                        weight.add(Integer.valueOf(Converters.longToInt(days.child("weight").exists() ? ((Long) days.child("weight").getValue()).longValue() : 0)));
                        if (days.child("height").exists()) {
                            j = ((Long) days.child("height").getValue()).longValue();
                        }
                        height.add(Integer.valueOf(Converters.longToInt(j)));
                    }
                    refactorValues(weight);
                    refactorValues(height);
                    ArrayList<Integer> arrayList = weight;
                    MatrixData matrixData = new MatrixData(dates, calories, sugar, fats, protein, weight, height);
                    intent.putExtra(matrixData.getClass().getName(), matrixData);
                    Log.d(TAG, matrixData.dates.toString());
                    Log.d(TAG, matrixData.calories.toString());
                    intent.putExtra(Constants.IS_SUCCESSFUL, true);
                } else {
                    intent.putExtra(Constants.IS_SUCCESSFUL, false);
                }
                context.sendBroadcast(intent);
            }

            public void onCancelled(DatabaseError error) {
                Intent intent = new Intent(Constants.INTENT_GET_NUTRITIONAL_INFO_ACTION);
                intent.putExtra(Constants.IS_SUCCESSFUL, false);
                context.sendBroadcast(intent);
            }
        });
    }

    /* access modifiers changed from: private */
    public void refactorValues(ArrayList<Integer> list) {
        int value = 0;
        Iterator<Integer> it = list.iterator();
        while (true) {
            if (it.hasNext()) {
                int i = it.next().intValue();
                if (i != 0) {
                    value = i;
                    break;
                }
            } else {
                break;
            }
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (list.get(i2).intValue() == 0) {
                list.set(i2, Integer.valueOf(value));
            } else {
                value = list.get(i2).intValue();
            }
        }
    }

    private void updateMeal(final Meal meal, final Date date, final Context context) {
        String id = String.copyValueOf(meal.getId().toCharArray());
        meal.setId(null);
        database.child(currentUser.getUid()).child(Converters.dateToString(date)).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    updateNutritionalInfoMatrix(meal, snapshot.getValue(Meal.class), date);
                    return;
                }
                try {
                    updateNutritionalInfoMatrix(meal, null, date);
                } catch (Exception e){
                    Log.e(TAG, e.getMessage());
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
        database.child(currentUser.getUid()).child(Converters.dateToString(date)).child(id).setValue(meal).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                Intent intent = new Intent(Constants.INTENT_SAVE_MEAL_ACTION);
                if (task.isSuccessful()) {
                    Log.d(TAG, "updateMealToData:successful");
                    intent.putExtra(Constants.IS_SUCCESSFUL, true);
                } else {
                    Log.d(TAG, "updateMealToData:failed");
                    intent.putExtra(Constants.IS_SUCCESSFUL, false);
                }
                context.sendBroadcast(intent);
            }
        });
    }

    private void addNewMeal(Meal meal, Date date, final Context context) {
        database.child(currentUser.getUid()).child(Converters.dateToString(date)).push().setValue(meal).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(Task<Void> task) {
                Intent intent = new Intent(Constants.INTENT_SAVE_MEAL_ACTION);
                if (task.isSuccessful()) {
                    Log.d(TAG, "writeMealToData:successful");
                    intent.putExtra(Constants.IS_SUCCESSFUL, true);
                } else {
                    Log.d(TAG, "writeMealToData:failed");
                    intent.putExtra(Constants.IS_SUCCESSFUL, false);
                }
                context.sendBroadcast(intent);
            }
        });
        try {
            updateNutritionalInfoMatrix(meal, null, date);
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    public void updateNutritionalInfoMatrix(final Meal meal, final Meal oldMeal, final Date date) {
        database.child(currentUser.getUid()).child(Constants.MATRIX).child(Converters.dateToString(date)).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    long fats;
                    long sugar;
                    long calories;
                    long protein;
                    long calories2;
                    long sugar2;
                    long oldProtein;
                    if (snapshot.exists()) {
                        HashMap<String, Object> oldMap = (HashMap) snapshot.getValue();
                        long calories3 = ((Long) oldMap.get(Constants.CALORIES)).longValue();
                        Meal meal = oldMeal;
                        long oldCalories = meal == null ? 0 : (long) meal.getCalories();
                        long oldCalories2 = oldCalories;
                        long calories4 = (calories3 - oldCalories) + ((long) meal.getCalories());
                        long sugar3 = ((Long) oldMap.get(Constants.SUGAR)).longValue();
                        Meal meal2 = oldMeal;
                        if (meal2 == null) {
                            calories = calories4;
                            calories2 = 0;
                        } else {
                            calories = calories4;
                            calories2 = (long) meal2.getSugar();
                        }
                        long j = sugar3;
                        long sugar4 = (sugar3 - calories2) + ((long) meal.getSugar());
                        long fats2 = ((Long) oldMap.get(Constants.FATS)).longValue();
                        Meal meal3 = oldMeal;
                        if (meal3 == null) {
                            sugar = sugar4;
                            sugar2 = 0;
                        } else {
                            sugar = sugar4;
                            sugar2 = (long) meal3.getFats();
                        }
                        long j2 = fats2;
                        long fats3 = (fats2 - sugar2) + ((long) meal.getFats());
                        long protein2 = ((Long) oldMap.get(Constants.PROTEIN)).longValue();
                        Meal meal4 = oldMeal;
                        if (meal4 == null) {
                            fats = fats3;
                            HashMap<String, Object> hashMap = oldMap;
                            oldProtein = 0;
                        } else {
                            HashMap<String, Object> hashMap2 = oldMap;
                            fats = fats3;
                            oldProtein = (long) meal4.getProteins();
                        }
                        long j3 = protein2;
                        protein = (protein2 - oldProtein) + ((long) meal.getProteins());
                        long j4 = oldCalories2;
                    } else {
                        calories = (long) meal.getCalories();
                        sugar = (long) meal.getSugar();
                        protein = (long) meal.getProteins();
                        fats = (long) meal.getFats();
                    }
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(Constants.CALORIES, Long.valueOf(calories));
                    map.put(Constants.SUGAR, Long.valueOf(sugar));
                    map.put(Constants.FATS, Long.valueOf(fats));
                    map.put(Constants.PROTEIN, Long.valueOf(protein));
                    database.child(currentUser.getUid()).child(Constants.MATRIX).child(Converters.dateToString(date)).updateChildren(map);
                } catch (Exception e){
                    Log.e(TAG, e.getMessage());
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public static class MatrixData implements Serializable {
        public ArrayList<Integer> calories;
        public ArrayList<String> dates;
        public ArrayList<Integer> fats;
        public ArrayList<Integer> height;
        public ArrayList<Integer> protein;
        public ArrayList<Integer> sugar;
        public ArrayList<Integer> weight;

        public MatrixData() {
        }

        public MatrixData(ArrayList<String> dates2, ArrayList<Integer> calories2, ArrayList<Integer> sugar2, ArrayList<Integer> fats2, ArrayList<Integer> protein2, ArrayList<Integer> weight2, ArrayList<Integer> height2) {
            this.dates = dates2;
            this.calories = calories2;
            this.sugar = sugar2;
            this.fats = fats2;
            this.protein = protein2;
            this.weight = weight2;
            this.height = height2;
        }
    }
}
