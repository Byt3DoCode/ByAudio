package com.byt3.byaudio.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.byt3.byaudio.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                HandleItem(item);
                return true;
            }
        });
    }

    private void HandleItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_discovery:
                //viewPager.setCurrentItem(1);
                break;
            case R.id.nav_player:
                //viewPager.setCurrentItem(2);
                break;
            case R.id.nav_playlist:
                //viewPager.setCurrentItem(3);
                break;
            case R.id.nav_queue:
                //viewPager.setCurrentItem(4);
                break;
            case R.id.nav_search:
                //viewPager.setCurrentItem(5);
                break;
        }
    }
}