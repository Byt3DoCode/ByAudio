package com.byt3.byaudio.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.adapter.ViewPagerAdapter;
import com.byt3.byaudio.model.AppDatabase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager;

    @Override
    protected void onStart() {
        super.onStart();
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, 5);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(true);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_discovery).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_player).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_queue).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.nav_search).setChecked(true);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.nav_playlist).setChecked(true);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_discovery) {
                viewPager.setCurrentItem(0);
            } else if (itemId == R.id.nav_player) {
                viewPager.setCurrentItem(1);
            } else if (itemId == R.id.nav_queue) {
                viewPager.setCurrentItem(2);
            } else if (itemId == R.id.nav_search) {
                viewPager.setCurrentItem(3);
            } else if (itemId == R.id.nav_playlist) {
                viewPager.setCurrentItem(4);
            }
            return true;
        });
    }
}