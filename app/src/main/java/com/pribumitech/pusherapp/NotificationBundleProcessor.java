package com.pribumitech.pusherapp;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class NotificationBundleProcessor {

    static JSONObject bundleAsJSONObject(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();

        for (String key : keys) {
            try {
                json.put(key, bundle.get(key));
            } catch (JSONException e) {

            }
        }
        return json;
    }

    static OSNotificationPayload OSNotificationPayloadFrom(JSONObject currentJsonPayload) {
        OSNotificationPayload notification = new OSNotificationPayload();

        return notification;
    }
}
