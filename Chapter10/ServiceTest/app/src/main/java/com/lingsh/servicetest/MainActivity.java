package com.lingsh.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final Class<MyService> SERVICE_CLASS = MyService.class;
    private static final Class<MyForegroundService> FOREGROUND_SERVICE_CLASS = MyForegroundService.class;
    private static final Class<MyIntentService> INTENT_SERVICE_CLASS = MyIntentService.class;
    private Intent mIntent;

    private MyService.DownloadBinder mDownloadBinder;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "与服务绑定 建立连接 ");
            mDownloadBinder = (MyService.DownloadBinder) service;
            mDownloadBinder.startDownload();
            mDownloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "与服务解绑 关闭连接 ");
        }
    };
    private Intent mForeIntent;
    private Intent mIntentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntent = new Intent(this, SERVICE_CLASS);

        Button start = (Button) findViewById(R.id.start_service);
        Button stop = (Button) findViewById(R.id.stop_service);
        Button bind = (Button) findViewById(R.id.bind_service);
        Button unbind = (Button) findViewById(R.id.unbind_service);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        bind.setOnClickListener(this);
        unbind.setOnClickListener(this);


        Button test = (Button) findViewById(R.id.test_action);
        test.setOnClickListener(this);

        Button startForeground = (Button) findViewById(R.id.start_foreground_service);
        Button startIntent = (Button) findViewById(R.id.start_intent_service);

        mForeIntent = new Intent(this, FOREGROUND_SERVICE_CLASS);
        mIntentIntent = new Intent(this, INTENT_SERVICE_CLASS);

        startForeground.setOnClickListener(this);
        startIntent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service:
                startService(mIntent);
                break;
            case R.id.stop_service:
                stopService(mIntent);
                break;
            case R.id.bind_service:
                bindService(mIntent, mConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(mConnection);
                break;
            case R.id.test_action:
                if (mDownloadBinder != null) {
                    mDownloadBinder.getProgress();
                }
                break;
            case R.id.start_foreground_service:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(mForeIntent);
                } else {
                    startService(mForeIntent);
                }
                break;
            case R.id.start_intent_service:
                Log.d(TAG, "主线程ID：" + Thread.currentThread().getId());
                startService(mIntentIntent);
                break;
            default:
                break;
        }
    }
}
