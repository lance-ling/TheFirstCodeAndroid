package com.lingsh.androidtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 用于给当前Activity加载一个布局
        setContentView(R.layout.first_layout);

        Button clickButton = findViewById(R.id.click_button);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this,
                        R.string.click_button_action,
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button access2NextButton1 = findViewById(R.id.access2next_button1);
        access2NextButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用显式Intent 明确指出: 从哪里跳转到哪里
                // 跳转页面到SecondActivity
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        Button access2NextButton2 = findViewById(R.id.access2next_button2);
        access2NextButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用隐式Intent 只是指出大致的范围 范围由ACTION和CATEGORY圈定
                // 个人设置了 com.lingsh.activitytest.ACTION_START的ACTION属性
                Intent intent = new Intent("com.lingsh.activitytest.ACTION_START");
                intent.addCategory("com.lingsh.activitytest.MY_CATEGORY");
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add_button:
                Toast.makeText(FirstActivity.this, R.string.menu_add_option, Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_remove_button:
                Toast.makeText(FirstActivity.this, R.string.menu_remove_option, Toast.LENGTH_SHORT).show();
                break;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }
}
