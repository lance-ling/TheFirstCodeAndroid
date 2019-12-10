package com.lingsh.androidlifecycletest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String DATA_KEY = "data_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            String string = savedInstanceState.getString(DATA_KEY);
            Log.d(TAG, "onCreate: save: " + string);
        }

        Button normalButton = findViewById(R.id.choose_normal_button);
        Button dialogButton = findViewById(R.id.choose_dialog_button);

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NormalActivity.class);
                startActivity(intent);
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");

        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String tempString = "Something you just typed";
        outState.putString(DATA_KEY, tempString);
    }
}
