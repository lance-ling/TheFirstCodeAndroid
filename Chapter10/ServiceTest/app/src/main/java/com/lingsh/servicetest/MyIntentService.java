package com.lingsh.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Intent服务被创建");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "打印当前线程ID：" + Thread.currentThread().getId());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Intent服务被销毁");
    }
}
