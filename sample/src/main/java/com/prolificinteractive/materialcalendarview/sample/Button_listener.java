package com.prolificinteractive.materialcalendarview.sample;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Button_listener extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(intent.getExtras().getInt("id"));
    }
}
