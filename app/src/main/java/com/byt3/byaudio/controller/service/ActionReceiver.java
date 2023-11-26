package com.byt3.byaudio.controller.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int action = intent.getIntExtra("actionFromService", 0);
        Intent serviceIntent = new Intent(context, PlayerService.class);
        serviceIntent.putExtra("actionToService", action);
        context.startService(serviceIntent);
    }
}
