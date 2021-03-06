package com.pribumitech.pusherapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.pribumitech.pusherapp.services.BackgroundService;
import com.pribumitech.pusherapp.services.NotifBroadcastReceiver;

public class ApplicationLoader extends Application {

    public static ApplicationLoader Instance = null;
    public static Context applicationContext;
    @SuppressLint("StaticFieldLeak")
    public static volatile Handler applicationHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;
        applicationContext = getApplicationContext();
        applicationHandler = new Handler(applicationContext.getMainLooper());

        final WakefulBroadcastReceiver mReceiver = new NotifBroadcastReceiver();
        final IntentFilter filter = new IntentFilter();
        filter.addAction("customer_order");
        //filter.addCategory("com.pribumitech.pusherapp.MainActivity");
        applicationContext.registerReceiver(mReceiver, filter);

        applicationContext.startService(new Intent(applicationContext, NotifBroadcastReceiver.class));
        applicationContext.startService(new Intent(applicationContext, BackgroundService.class));

        if (android.os.Build.VERSION.SDK_INT >= 19) {

            PendingIntent pintent =
                    PendingIntent.getService(applicationContext, 0, new Intent(applicationContext, NotifBroadcastReceiver.class), 0);
            AlarmManager alarm = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pintent);

            PendingIntent pintent2 =
                    PendingIntent.getService(applicationContext, 0, new Intent(applicationContext, BackgroundService.class), 0);
            AlarmManager alarm2 = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
            alarm2.cancel(pintent2);
        }
    }




}
