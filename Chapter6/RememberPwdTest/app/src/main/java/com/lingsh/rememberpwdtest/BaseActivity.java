package com.lingsh.rememberpwdtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author: lingsh
 * @date: 2019/12/17 14:15
 * @description:
 * @version: 1.0
 **/

public class BaseActivity extends AppCompatActivity {

    private ForceOfflineReceiver mForceOfflineReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lingsh.broadcastbestpractice.FORCE_OFFLINE");
        mForceOfflineReceiver = new ForceOfflineReceiver();
        registerReceiver(mForceOfflineReceiver, intentFilter);
    }


    @Override
    protected void onPause() {
        super.onPause();

        // 只需要处在栈顶的Activity接收到广播 非栈顶没有必要接收
        // 当一个Activity失去栈顶位置时就会自动取消广播接收器的注册
        if (mForceOfflineReceiver != null) {
            unregisterReceiver(mForceOfflineReceiver);
            mForceOfflineReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    class ForceOfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("warning");
            builder.setMessage("You are forced to be offline. Please try to login again!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.finishAll();
                    Intent intent1 = new Intent(context, LoginActivity.class);
                    context.startActivity(intent1);
                }
            });
            builder.show();
        }
    }
}
