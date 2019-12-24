package com.lingsh.networktest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OkHttpActivity extends AppCompatActivity {

    private TextView mContainer;

    public static void jump(Context from) {
        from.startActivity(new Intent(from, OkHttpActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);

        Button ret = (Button) findViewById(R.id.ret);
        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpActivity.this.finish();
            }
        });

        mContainer = (TextView) findViewById(R.id.text_container);
        sendReqWithHttpUrlConnection();
    }

    private void sendReqWithHttpUrlConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String content = NetUtils.netByOkHttp();
                showResponse(content);
            }
        }).start();
    }

    private void showResponse(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContainer.setText(content);
            }
        });
    }
}
