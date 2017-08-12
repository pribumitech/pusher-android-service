package com.pribumitech.pusherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Penerima jika ada notifikasi terpakai (jika HP BOOTING)
 * Please JANGAN DIRUBAH
 */
public class NotifBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName cn = new ComponentName(ApplicationLoader.applicationContext.getPackageName(), NotificationExtenderService.class.getName());
        startWakefulService(context, intent.setComponent(cn));
    }
}

