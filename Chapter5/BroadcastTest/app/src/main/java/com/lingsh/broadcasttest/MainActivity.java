package com.lingsh.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {

    private IntentFilter mIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private AirPlaneModeChangeReceiver mAirPlaneModeChangeReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;
    private LocalReceiver mLocalReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver, mIntentFilter);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        mAirPlaneModeChangeReceiver = new AirPlaneModeChangeReceiver();
        registerReceiver(mAirPlaneModeChangeReceiver, mIntentFilter);

        // 发送标准广播
        Button sendNormalBroadcastButton = findViewById(R.id.send_normal_broadcast);
        sendNormalBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.lingsh.broadcasttest.MY_BROADCAST");
                sendBroadcast(intent);
            }
        });

        // 发送有序广播
        Button sendOrderedBroadcastButton = findViewById(R.id.send_ordered_broadcast);
        sendOrderedBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.lingsh.broadcasttest.MY_BROADCAST");
                sendOrderedBroadcast(intent, null);
            }
        });

        // 发送本地广播
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        Button sendLocalBroadcastButton = findViewById(R.id.send_local_broadcast);
        sendLocalBroadcastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.lingsh.broadcasttest.LOCAL_BROADCAST");
                mLocalBroadcastManager.sendBroadcast(intent);
            }
        });

        // 接收本地广播
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.lingsh.broadcasttest.LOCAL_BROADCAST");
        mLocalReceiver = new LocalReceiver();
        mLocalBroadcastManager.registerReceiver(mLocalReceiver, localIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);
        unregisterReceiver(mAirPlaneModeChangeReceiver);
        mLocalBroadcastManager.unregisterReceiver(mLocalReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                // 需要权限 ACCESS_NETWORK_STATE
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()) {
                    Toast.makeText(context, "network is available", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "network changes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class AirPlaneModeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "air plane mode changes", Toast.LENGTH_SHORT).show();
        }
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "received local broadcast", Toast.LENGTH_SHORT).show();
        }
    }
}
