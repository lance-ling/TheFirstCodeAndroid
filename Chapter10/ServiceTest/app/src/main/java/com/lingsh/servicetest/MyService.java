package com.lingsh.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "MyService";

    private DownloadBinder mBinder = new DownloadBinder();

    public MyService() {
    }

    class DownloadBinder extends Binder {
        public void startDownload() {
            Log.d(TAG, "开始下载");
        }

        public int getProgress() {
            Log.d(TAG, "获取进度");
            return 0;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "服务创建了");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "服务开始命令 运行: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "服务被销毁了");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
