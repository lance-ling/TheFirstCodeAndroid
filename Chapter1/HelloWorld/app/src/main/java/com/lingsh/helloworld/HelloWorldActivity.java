package com.lingsh.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class HelloWorldActivity extends AppCompatActivity {

    private static final String TAG = "HelloWorldActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 日志的五个级别 verbose < debug < info < warn < error
        Log.v(TAG, "onCreate: verbose");
        Log.d(TAG, "onCreate: debug");
        Log.i(TAG, "onCreate: info");
        Log.w(TAG, "onCreate: warn");
        Log.e(TAG, "onCreate: error");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_world_layout);
    }
}
