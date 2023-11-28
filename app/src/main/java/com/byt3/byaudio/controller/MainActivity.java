package com.byt3.byaudio.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.byt3.byaudio.R;
import com.byt3.byaudio.controller.adapter.ViewPagerAdapter;
import com.byt3.byaudio.controller.service.PlayerService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager;
//    Intent serviceIntent = new Intent(this, PlayerService.class);
//    public static PlayerService playerService;
//    boolean isServiceConnected;
//    ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            PlayerService.MyBinder binder = (PlayerService.MyBinder) iBinder;
//            playerService = binder.getplayerService();
//            isServiceConnected = true;
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            isServiceConnected = false;
//        }
//    };

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
                        break;
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
                viewPager.setCurrentItem(0, false);
            } else if (itemId == R.id.nav_player) {
                viewPager.setCurrentItem(1, false);
            } else if (itemId == R.id.nav_queue) {
                viewPager.setCurrentItem(2, false);
            } else if (itemId == R.id.nav_search) {
                viewPager.setCurrentItem(3, false);
            } else if (itemId == R.id.nav_playlist) {
                viewPager.setCurrentItem(4, false);
            }
            return true;
        });

        if (getIntent().getBooleanExtra("fromService", false))
            viewPager.setCurrentItem(1);

//        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (isServiceConnected){
//            unbindService(serviceConnection);
//            isServiceConnected = false;
//        }
    }
}