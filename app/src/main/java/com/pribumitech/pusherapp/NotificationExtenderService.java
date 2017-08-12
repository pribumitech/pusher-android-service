package com.pribumitech.pusherapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class yang menangani Notifikasi
 */
public class NotificationExtenderService extends IntentService {

    //protected abstract boolean onNotificationProcessing(OSNotificationReceivedResult notification);

    public NotificationExtenderService() {
        super("NotificationExtenderService");
        setIntentRedelivery(true);
    }

    private void processIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.d("ERROR", "No extras sent to NotificationExtenderService in its Intent!\n" + intent);
            //OneSignal.Log(OneSignal.LOG_LEVEL.ERROR, "No extras sent to NotificationExtenderService in its Intent!\n" + intent);
            return;
        }
        String messagesNotif = bundle.getString("message");
        String jsonStrPayload = bundle.getString("json_payload");
        if (messagesNotif == null) {
            return;
        }

        JSONObject object = null;
        try {
            object = new JSONObject(jsonStrPayload);
            processJsonObject(object);
        } catch (JSONException e) {
            Log.d("ERROR DECODE", e.getMessage());
        }

        Log.d("PUSHER_RECEIVER", messagesNotif);

        buildNotification(intent, messagesNotif);
    }

    private void processJsonObject(JSONObject currentJsonPayload) {
        OSNotificationReceivedResult receivedResult = new OSNotificationReceivedResult();
        receivedResult.payload = NotificationBundleProcessor.OSNotificationPayloadFrom(currentJsonPayload);
    }

    private void buildNotification(Intent intent, String messagesNotif) {

        NotificationManager mNotificationManager =
                (NotificationManager) ApplicationLoader.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(ApplicationLoader.applicationContext)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_stat_onesignal_default) // Small Icon required or notification doesn't display
                .setContentTitle(ApplicationLoader.applicationContext.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messagesNotif))
                .setContentText(messagesNotif)
                .setTicker(messagesNotif);

        notifBuilder.setVibrate(new long[]{0, 100, 0, 100});
        PendingIntent contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notifBuilder.setContentIntent(contentIntent);
        mNotificationManager.cancel(1);

        Notification notification = notifBuilder.build();
        notification.ledARGB = 0xff00ff00;
        notification.ledOnMS = 1000;
        notification.ledOffMS = 1000;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;

        try {
            mNotificationManager.notify(1, notification);
        } catch (Exception e) {
            //FileLog.e("tmessages", e);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        processIntent(intent);
    }
}
