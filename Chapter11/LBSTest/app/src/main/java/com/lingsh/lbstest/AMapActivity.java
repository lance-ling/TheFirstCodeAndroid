package com.lingsh.lbstest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.MapView;

public class AMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap);

        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
    }
}
