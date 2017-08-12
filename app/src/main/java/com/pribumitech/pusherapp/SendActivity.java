package com.pribumitech.pusherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pribumitech.pusherapp.utils.PusherOdk;
import com.pusher.client.channel.PrivateChannel;

import org.json.JSONException;
import org.json.JSONObject;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        PusherOdk.getInstance().PusherApp.connect();
        final EditText txtMsg = (EditText) findViewById(R.id.ed_messages);
        Button btn = (Button) findViewById(R.id.btn_send);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    if(txtMsg.getText().toString().equals("")){
                        Toast.makeText(SendActivity.this,"Message tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    object.put("text", txtMsg.getText().toString());
                    PrivateChannel privateChannel = PusherOdk.getInstance().PusherApp.getPrivateChannel("private-test-channel");
                    privateChannel.trigger("client-test-event", object.toString());
                    txtMsg.setText("");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
