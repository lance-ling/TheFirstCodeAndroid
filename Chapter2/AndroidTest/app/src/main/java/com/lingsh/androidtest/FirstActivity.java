package com.lingsh.androidtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "FirstActivity";
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 启动模式
         *  - standard 默认标准模式 每次启动activity都是全新的 完全不顾栈中已经创建过启动的activity且在栈顶(当前)
         *  - singleTop 当栈顶已有所需创建的activity 则直接复用 (很好地解决重复创建栈顶activity的问题)
         *  - singleTask 当栈内已有所需创建的activity 则将其置入栈顶(即其上的activity全部弹出) 没有则自建
         *  - singleInstance 给被设置该模式的activity单独设置一个返回栈 解决共享activity实例问题
         */
        Log.d(TAG, "onCreate: " + this.toString());
        Log.d(TAG, "onCreate: Task id is " + getTaskId());
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

        Button call2SecondButton = findViewById(R.id.call2second_button);
        call2SecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
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

        Button openBrowserButton = findViewById(R.id.open_browser_button);
        openBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });

        Button dialogTelButton = findViewById(R.id.dialog_tel_button);
        dialogTelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });

        Button transMsg2NextButton = findViewById(R.id.trans_msg2next_button);
        transMsg2NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transMsg = "TRANS MSG: FirstActivity --> ";
                Intent intent = ThirdActivity.bindExtraData(FirstActivity.this, transMsg);
                startActivity(intent);
            }
        });


        Button transMsg2Next4MsgButton = findViewById(R.id.trans_msg2next4msg_button);
        transMsg2Next4MsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transMsg = "TRANS MSG2: FirstActivity --> ";
                Intent intent = ThirdActivity.bindExtraData(FirstActivity.this, transMsg);
                startActivityForResult(intent, REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String stringExtra = data.getStringExtra(ThirdActivity.RET_LABEL);
                    Log.d(TAG, "onActivityResult: " + stringExtra);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
