package com.byt3.byaudio.controller.service;

import static java.security.AccessController.getContext;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class PlayerService extends Service {
    public static final String CHANNEL_ID = "1001";
    public static final String START = "START";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent.getAction().equals("START"))
//            startForeground();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
