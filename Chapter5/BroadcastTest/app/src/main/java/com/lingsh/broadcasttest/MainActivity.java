package com.lingsh.broadcasttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private IntentFilter mIntentFilter;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private AirPlaneModeChangeReceiver mAirPlaneModeChangeReceiver;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);
        unregisterReceiver(mAirPlaneModeChangeReceiver);
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
}
