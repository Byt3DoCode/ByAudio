package com.byt3.byaudio.controller;

import static com.byt3.byaudio.utils.functions.CHANNEL_ID;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class ByAudioApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel(){
        NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Player Service", NotificationManager.IMPORTANCE_LOW);
        serviceChannel.setName("ByAudio");
        serviceChannel.setSound(null, null);
        serviceChannel.enableVibration(false);
        serviceChannel.enableLights(false);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }
}
