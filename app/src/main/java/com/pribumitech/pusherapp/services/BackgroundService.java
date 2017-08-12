package com.pribumitech.pusherapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pribumitech.pusherapp.utils.PusherOdk;
import com.pusher.client.channel.PrivateChannelEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class BackgroundService extends Service {

    private Handler handler = new Handler(Looper.getMainLooper());

    Thread readthread;

    private Runnable checkRunnable = new Runnable() {
        @Override
        public void run() {
            check();
        }
    };

    public BackgroundService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        check();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);

        PusherOdk.getInstance().PusherApp.connect();
        if (PusherOdk.getInstance()
                .PusherApp.getPrivateChannel("private-test-channel") == null) {
            subsribe();
        }
        return START_STICKY;
    }

    private void subsribe() {

        PusherOdk.getInstance()
                .PusherApp.subscribePrivate("private-test-channel",
                new PrivateChannelEventListener() {
                    @Override
                    public void onAuthenticationFailure(String s, Exception e) {
                        Log.d("PUSHER", "Channel subscription authorization failed");
                    }

                    @Override
                    public void onSubscriptionSucceeded(String channelName) {
                        Log.d("PUSHER", "Channel subscription " + channelName);
                    }

                    @Override
                    public void onEvent(final String s, final String s2, final String s3) {

                        readthread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent broadcastIntent = new Intent();
                                try {
                                    JSONObject object = new JSONObject(s3);

                                    //kunci agar unik
                                    broadcastIntent.setAction("customer_order");
                                    broadcastIntent.putExtra("json_payload", s3);
                                    broadcastIntent.putExtra("message", object.getString("text"));
                                    sendBroadcast(broadcastIntent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("Pusher", "conversion failed");
                                }
                            }
                        });

                        readthread.start();
                    }
                }, "client-test-event");
    }

    private void check() {
        handler.removeCallbacks(checkRunnable);
        handler.postDelayed(checkRunnable, 1500);
        //ConnectionsManager connectionsManager = ConnectionsManager.Instance;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("tmessages", "onDestroy");
    }
}
