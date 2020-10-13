package com.flexapp.flex;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.customview.widget.Openable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.flexapp.flex.repos.MainRepository;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        MainActivity.super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkMenuToNavCnt();
        linkToolbarToNavCnt();
        setupRepo();
    }

    public void linkMenuToNavCnt() {
        Fragment mainFragHost = getSupportFragmentManager().findFragmentById(R.id.main_frag_holder);
        NavigationUI.setupWithNavController((NavigationView) findViewById(R.id.nav_view), NavHostFragment.findNavController(mainFragHost));
    }

    public void linkToolbarToNavCnt() {
        Fragment mainFragHost = getSupportFragmentManager().findFragmentById(R.id.main_frag_holder);
        HashSet<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(Integer.valueOf(R.id.todayFragment));
        topLevelDestinations.add(Integer.valueOf(R.id.reportsFragment));
        topLevelDestinations.add(Integer.valueOf(R.id.settingsFragment));
        NavigationUI.setupWithNavController((Toolbar) findViewById(R.id.toolbar), NavHostFragment.findNavController(mainFragHost), new AppBarConfiguration.Builder(topLevelDestinations).setOpenableLayout((Openable) findViewById(R.id.layout_drawer)).build());
    }

    public void setupRepo() {
        MainRepository.getInstance().initRepo(getPreferences(MODE_PRIVATE));
    }
}
