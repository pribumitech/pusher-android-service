package com.pribumitech.pusherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pribumitech.pusherapp.utils.PusherOdk;
import com.pribumitech.pusherapp.utils.RetrofitString;
import com.pusher.android.PusherAndroid;
import com.pusher.android.PusherAndroidOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * https://github.com/pusher/pusher-test-android
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PusherAndroid pusher = null;
    Channel channel = null;
    private CompositeSubscription mCompositeSubscription = null;
    private BroadcastReceiver broadcastReceiver;
    private TextView netStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.netStatus = (TextView) findViewById(R.id.net_status);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mCompositeSubscription = new CompositeSubscription();

        this.broadcastReceiver = new NetworkInfoReceiver();
        final IntentFilter filterInet = new IntentFilter();
        filterInet.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filterInet.addCategory("com.pribumitech.pusherapp.MainActivity");
        this.registerReceiver(broadcastReceiver, filterInet);

        //Intent intent = new Intent(this, NotifBroadcastReceiver.class);
        //startService(intent);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_send_msg) {
            Intent i = new Intent(MainActivity.this, SendActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_receive_msg) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateNetStatus(final String connectionType) {
        final boolean connected = connectionType.length() > 0;

        final String text = connected ? "Connected (" + connectionType + ")" : "Disconnected";
        final int bgResource = connected ? R.drawable.rect_green : R.drawable.rect_red;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                netStatus.setBackgroundDrawable(getResources().getDrawable(bgResource));
                netStatus.setText(text);
                netStatus.invalidate();
                //triggerEventBtn.setEnabled(connected);
                //triggerEventBtn.invalidate();
            }
        });
    }

    public class NetworkInfoReceiver extends BroadcastReceiver {
        private String currentlyConnectedType = "";

        public NetworkInfoReceiver() {
            super();
        }

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ConnectivityManager mgr =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            String connectionType = "";

            for (final NetworkInfo info : mgr.getAllNetworkInfo()) {
                if (info.isConnected()) {
                    connectionType = info.getTypeName();
                    break;
                }
            }

            if (!currentlyConnectedType.equals(connectionType)) {
                currentlyConnectedType = connectionType;
                updateNetStatus(connectionType);
            }
        }
    }
}
