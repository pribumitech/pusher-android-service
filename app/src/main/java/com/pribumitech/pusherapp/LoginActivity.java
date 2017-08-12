package com.pribumitech.pusherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pribumitech.pusherapp.utils.PusherOdk;
import com.pusher.client.channel.PrivateChannel;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PusherOdk.getInstance().PusherApp.connect();

        Button btn = (Button) findViewById(R.id.btn_login);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("text", "Hellooo World!");
                    PrivateChannel privateChannel = PusherOdk.getInstance().PusherApp.getPrivateChannel("private-test-channel");
                    privateChannel.trigger("client-test-event", object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
