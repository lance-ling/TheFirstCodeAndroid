package com.lingsh.audiovideotest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class PlayAudioActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_WRITE = 1;
    private MediaPlayer mMediaPlayer;

    public static void startForActivity(Context context) {
        Intent intent = new Intent(context, PlayAudioActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);

        Button start = (Button) findViewById(R.id.start);
        Button pause = (Button) findViewById(R.id.pause);
        Button reset = (Button) findViewById(R.id.reset);

        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        reset.setOnClickListener(this);

        // 1. 创建MediaPlayer实例
        mMediaPlayer = new MediaPlayer();
        // 2. 在onCreate() 进行运行时权限处理 动态申请WRITE_EXTRANAL_STORAGE权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE);
        } else {
            initMediaPalyer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE) {
            // 3. 判断用户是否授权
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  1. 同意，初始化MediaPlayer对象
                Toast.makeText(this, "Thx for you grant", Toast.LENGTH_SHORT).show();
                initMediaPalyer();
            } else {
                //  2. 拒绝，直接退出finish()
                Toast.makeText(this, "Sorry, you deny", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initMediaPalyer() {
        // 4. 创建File对象指定音频文件的路径
        // getExternalStorageDirectory Android 10.0(Q) 被遗弃
        File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
        // 5. 调用MediaPlayer对象的setDataSource()和prepare()方法，为播放前做好准备
        try {
            mMediaPlayer.setDataSource(file.getPath());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // 6. 判断是否正在播放 调用MediaPlayer对象的isPlaying()方法
        switch (v.getId()) {
            case R.id.start:
                //  1. 开始播放 在没有播放时
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                }
                break;
            case R.id.pause:
                //  2. 暂停播放 在播放时
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                }
                break;
            case R.id.reset:
                //  3. 停止播放并初始化 在播放时
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.reset();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // 7. 在onDestroy() 停止播放并释放MediaPlayer对象
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        super.onDestroy();
    }
}
