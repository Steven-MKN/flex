package com.flexapp.flex;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.flexapp.flex.repos.MainRepository;

public class StartActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        StartActivity.super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [android.content.Context, com.flexapp.flex.StartActivity, androidx.appcompat.app.AppCompatActivity] */
    /* access modifiers changed from: protected */
    public void onStart() {
        StartActivity.super.onStart();
        if (MainRepository.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
