package com.lingsh.audiovideotest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class PlayVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_WRITE = 1;
    private VideoView mVideoView;

    public static void startForActivity(Context context) {
        Intent intent = new Intent(context, PlayVideoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        // 1. 创建VideoView对象， 通过setVideoPath()方法设置视频文件的路径
        mVideoView = (VideoView) findViewById(R.id.video_view);
        Button start = (Button) findViewById(R.id.start);
        Button pause = (Button) findViewById(R.id.pause);
        Button resume = (Button) findViewById(R.id.resume);

        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        resume.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE);
        } else {
            initVideoView();
        }

        // 2. 直接使用VideoV

    }

    private void initVideoView() {
        File file = new File(Environment.getExternalStorageDirectory(), "movie.mp4");
        mVideoView.setVideoPath(file.getPath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thx for you grant", Toast.LENGTH_SHORT).show();
                initVideoView();
            } else {
                Toast.makeText(this, "Sorry, you deny", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        // 2. 直接使用VideoView类的控制方法 对视频进行操作
        //  1. start() 开始播放
        //  2. pause() 暂停播放
        //  3. resume() 重新播放
        switch (v.getId()) {
            case R.id.start:
                if (!mVideoView.isPlaying()) {
                    mVideoView.start();
                }
                break;
            case R.id.pause:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                }
                break;
            case R.id.resume:
                if (mVideoView.isPlaying()) {
                    mVideoView.resume();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.suspend();
        }
    }
}
