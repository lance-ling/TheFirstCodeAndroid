package com.lingsh.lbstest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button D2Map = (Button) findViewById(R.id.open_2d_map);
        Button location = (Button) findViewById(R.id.open_location);

        D2Map.setOnClickListener(this);
        location.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_2d_map:
                toActivity(AMapActivity.class);
                break;
            case R.id.open_location:
                toActivity(LocationActivity.class);
                break;
            default:
                break;
        }
    }

    private void toActivity(Class cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
