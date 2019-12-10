package com.lingsh.androidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";
    private static final String EXTRA_LABEL = "com.lingsh.androidtest.ThirdActivity";
    public static final String RET_LABEL = "com.lingsh.androidtest.ThirdActivity.return";

    public static Intent bindExtraData(Context parent, String data) {
        Intent intent = new Intent(parent, ThirdActivity.class);
        intent.putExtra(EXTRA_LABEL, data);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_layout);

        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(EXTRA_LABEL);
        Log.d(TAG, "onCreate: " + stringExtra);

        TextView infoDisplay = findViewById(R.id.info_display);
        infoDisplay.append("\n" + stringExtra);

        Button returnData2PreButton = findViewById(R.id.return_data2pre_button);
        returnData2PreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String retData = "RET MSG: ThirdActivity --> ";
                Intent intent = new Intent();
                intent.putExtra(RET_LABEL, retData);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
