package com.lingsh.lbstest;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button D2Map = (Button) findViewById(R.id.open_2d_map);
        Button location = (Button) findViewById(R.id.open_location);
        Button haLocation = (Button) findViewById(R.id.open_location_ha);
        Button datailMap = (Button) findViewById(R.id.open_detail_map);

        D2Map.setOnClickListener(this);
        location.setOnClickListener(this);
        haLocation.setOnClickListener(this);
        datailMap.setOnClickListener(this);

        cp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_2d_map:
                AMapActivity.toThisActivity(this);
                break;
            case R.id.open_location:
                LocationActivity.toThisActivity(this, 1);
                break;
            case R.id.open_location_ha:
                LocationActivity.toThisActivity(this, 2);
                break;
            case R.id.open_detail_map:
                DetailMapActivity.toThisActivity(this);
                break;
            default:
                break;
        }
    }

    private void cp() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for you grant", Toast.LENGTH_SHORT).show();
            } else {
                cp();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
