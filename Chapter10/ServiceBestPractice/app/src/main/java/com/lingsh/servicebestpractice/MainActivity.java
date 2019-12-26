package com.lingsh.servicebestpractice;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public static final String DEFAULT_URL = "https://mirrors.tuna.tsinghua.edu.cn/mongodb/apt/ubuntu/dists/bionic/mongodb-org/stable/multiverse/binary-amd64/mongodb-org-tools_4.0.14_amd64.deb";
    public static final int WES_REQUEST_CODE = 1;

    private DownloadService.DownloadBinder mBinder;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "服务绑定");
            mBinder = ((DownloadService.DownloadBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "服务解绑");
        }
    };
    private TextView mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = (Button) findViewById(R.id.download_start);
        Button pause = (Button) findViewById(R.id.download_pause);
        Button cancel = (Button) findViewById(R.id.download_cancel);
        mUrl = (TextView) findViewById(R.id.download_url);

        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WES_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "权限申请结果: requestCode = [" + requestCode + "], " +
                "permissions = [" + Arrays.toString(permissions) + "], " +
                "grantResults = [" + Arrays.toString(grantResults) + "]");
        if (requestCode == WES_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "感谢你的权限给予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "抱歉，没有权限无法进行下载任务！", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download_start:
                String url = mUrl.getText().toString();
                if ("".equals(url)) {
                    url = DEFAULT_URL;
                }
                mBinder.startDownload(url);
                break;
            case R.id.download_pause:
                mBinder.pauseDownload();
                break;
            case R.id.download_cancel:
                mBinder.cancelDownload();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        Log.d(TAG, "关闭应用 并解绑服务");
    }
}
