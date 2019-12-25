package com.lingsh.mutlithreadtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private static final int UPDATE_TEXT = 1;
    private TextView mDisplay;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == UPDATE_TEXT) {
                mDisplay.setText(getRandomOrderString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button change = (Button) findViewById(R.id.change_text);
        mDisplay = (TextView) findViewById(R.id.display_text);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        mHandler.sendMessage(message);
                    }
                }).start();
            }
        });

    }

    Random mRandom = new Random();
    String[] origin = new String[]{
            "hello ", "world ", "nice ", "to ", "meet ", "you "
    };
    int len = origin.length;

    public String getRandomOrderString() {
        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = i;
        }
        StringBuilder res = new StringBuilder();
        int idx = len - 1;
        for (; idx > 0; ) {
            int random = mRandom.nextInt(idx + 1);
            res.append(origin[a[random]]);
            a[random] = a[idx];
            idx--;
        }
        res.append(origin[a[idx]]);

        return res.toString();
    }
}
