package com.pribumitech.pusherapp.utils;

import com.pusher.android.PusherAndroid;
import com.pusher.android.PusherAndroidOptions;
import com.pusher.client.util.HttpAuthorizer;

public class PusherOdk {
    private static PusherOdk ourInstance;
    public PusherAndroid PusherApp = null;
    public PusherAndroidOptions pusherOptions = null;

    private PusherOdk() {
        final HttpAuthorizer authorizer = new HttpAuthorizer("http://pusher.app/pusherauth");
        pusherOptions = new PusherAndroidOptions();
        pusherOptions.setAuthorizer(authorizer);
        pusherOptions.setEncrypted(true);
        PusherApp = new PusherAndroid("7c1167c4c590503e5f67", pusherOptions);
    }

    /**
     * Singleton Instance
     *
     * @return RetrofitString
     */
    public static synchronized PusherOdk getInstance() {
        if (ourInstance == null) {
            ourInstance = new PusherOdk();
        }
        return ourInstance;
    }

    /**
     * Get Wordpress API
     *
     * @return ApiServices
     */
    public PusherAndroid getPusherApp() {
        if (this.PusherApp == null) {
            this.PusherApp = new PusherAndroid("7c1167c4c590503e5f67", pusherOptions);
        }
        return this.PusherApp;
    }
}
