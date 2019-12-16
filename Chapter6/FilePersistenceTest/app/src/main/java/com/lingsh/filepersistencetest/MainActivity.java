package com.lingsh.filepersistencetest;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    public static final String DATA_PATH = "data";
    private EditText mEditEdit;
    private Button mSaveButton;
    private Button mLoadButton;
    private TextView mShowLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditEdit = findViewById(R.id.edit);
        mSaveButton = findViewById(R.id.save_button);
        mLoadButton = findViewById(R.id.load_button);
        mShowLoad = findViewById(R.id.show_load);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mEditEdit.getText().toString();
                if (!"".equals(string)) {
                    save(string);
                    Toast.makeText(MainActivity.this, "save data to file", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = load();
                mShowLoad.setText(string);
                Toast.makeText(MainActivity.this, "load data from file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String load() {
        FileInputStream input;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            input = openFileInput(DATA_PATH);
            reader = new BufferedReader(new InputStreamReader(input));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content.toString();
    }

    public void save(String data) {
        FileOutputStream output;
        BufferedWriter writer = null;
        try {
            output = openFileOutput(DATA_PATH, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
