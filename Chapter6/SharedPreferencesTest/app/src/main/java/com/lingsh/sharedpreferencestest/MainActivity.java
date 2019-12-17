package com.lingsh.sharedpreferencestest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final String DATA_SF = "data";
    public static final String KEY_NAME = "name";
    public static final String KEY_AGE = "age";
    public static final String KEY_GENDER = "gender";
    private EditText mName;
    private EditText mAge;
    private EditText mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mName = findViewById(R.id.name);
        mAge = findViewById(R.id.age);
        mGender = findViewById(R.id.gender);

        Button save = findViewById(R.id.save_button);
        Button load = findViewById(R.id.load_button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(DATA_SF, MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                // name
                String nameString = mName.getText().toString();
                edit.putString(KEY_NAME, nameString);

                // age
                int ageInt = Integer.parseInt(mAge.getText().toString());
                edit.putInt(KEY_AGE, ageInt);

                // gender
                boolean genderBoolean = true;
                String genderString = mGender.getText().toString();
                if ("0".equals(genderString)) {
                    genderBoolean = false;
                } else if (!"1".equals(genderString)) {
                    Toast.makeText(MainActivity.this, "Gender set Default 1", Toast.LENGTH_SHORT).show();
                }
                edit.putBoolean(KEY_GENDER, genderBoolean);

                edit.apply();

                Toast.makeText(MainActivity.this, "Data have save to SharedPreferences", Toast.LENGTH_SHORT).show();
            }
        });

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(DATA_SF, MODE_PRIVATE);
                String name = sp.getString(KEY_NAME, "default");
                int age = sp.getInt(KEY_AGE, 100);
                boolean gender = sp.getBoolean(KEY_GENDER, true);

                mName.setText(name);
                mAge.setText(age);
                mGender.setText(gender ? "1" : "0");
                Toast.makeText(MainActivity.this, "Get Data From SP", Toast.LENGTH_SHORT).show();
                Log.d(TAG, String.format("name:%s age:%d gender:%s", name, age, gender));
            }
        });
    }
}
