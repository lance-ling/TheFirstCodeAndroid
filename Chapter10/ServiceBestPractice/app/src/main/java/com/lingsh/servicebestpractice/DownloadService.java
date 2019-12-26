package com.lingsh.servicebestpractice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.io.File;

/**
 * 为下载任务服务的Service
 * <p>
 * 1. 用于保证DownloadTask一直在后台运行
 * 2. 实现下载监听器
 * 3. 内部类继承Binder 创建外部控制下载任务的入口
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/26 15:25
 **/

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    /**
     * 下载任务的状态描述
     */
    private static final String DESC_DOWNLOAD_ING = "Downloading...";
    private static final String DESC_DOWNLOAD_SUCCESS = "Download Success";
    private static final String DESC_DOWNLOAD_FAILED = "Download Failed";
    private static final String DESC_DOWNLOAD_PAUSED = "Download Paused";
    private static final String DESC_DOWNLOAD_CANCELED = "Download Canceled";

    private DownloadTask downloadTask;

    private String downloadUrl;

    private DownloadListener listener = new DownloadListener() {

        /**
         * 当下载成功或失败 进度应该置-1 关闭进度条
         */
        private static final int NEG_PROGRESS = -1;

        @Override
        public void onProgress(int progress) {
            Log.d(TAG, "监听下载进度: progress = [" + progress + "]");
            getNotificationManager().notify(1, getNotification(DESC_DOWNLOAD_ING, progress));
        }

        @Override
        public void onSuccess() {
            Log.d(TAG, "下载成功");
            downloadTask = null;
            // 下载成功时 将前台服务通知关闭 并创建一个下载成功的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotification(DESC_DOWNLOAD_SUCCESS, NEG_PROGRESS));
            toast(DESC_DOWNLOAD_SUCCESS);
        }

        @Override
        public void onFailed() {
            Log.d(TAG, "下载失败");
            downloadTask = null;
            // 下载失败时 将前台服务通知关闭 并创建一个下载失败的通知
            stopForeground(true);
            getNotificationManager().notify(1, getNotification(DESC_DOWNLOAD_FAILED, NEG_PROGRESS));
            toast(DESC_DOWNLOAD_FAILED);
        }

        @Override
        public void onPaused() {
            Log.d(TAG, "下载暂停");
            downloadTask = null;
            toast(DESC_DOWNLOAD_PAUSED);
        }

        @Override
        public void onCanceled() {
            Log.d(TAG, "下载取消");
            downloadTask = null;
            stopForeground(true);
            toast(DESC_DOWNLOAD_CANCELED);
        }
    };

    private DownloadBinder downloadBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    class DownloadBinder extends Binder {

        /**
         * 接收外部传入的下载地址
         *
         * @param url 文件下载地址
         */
        public void startDownload(String url) {
            Log.d(TAG, "开始下载: url = [" + url + "]");

            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1, getNotification(DESC_DOWNLOAD_ING, 0));
                toast(DESC_DOWNLOAD_ING);
            }
        }

        /**
         * 暂停下载 只有在下载任务存在的情况下
         */
        public void pauseDownload() {
            Log.d(TAG, "暂停下载");
            if (downloadTask != null) {
                downloadTask.pauseDownload();
            }
        }

        /**
         * 取消下载
         */
        public void cancelDownload() {
            Log.d(TAG, "取消下载");

            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }
            if (downloadUrl != null) {
                // 取消下载时需将文件删除 并将通知关闭
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                toast(DESC_DOWNLOAD_CANCELED);
            }
        }

    }

    /* ------------------------------------------------------------------------ */
    // 工具方法

    /**
     * 弹出消息
     *
     * @param msg 被显示消息
     */
    private void toast(String msg) {
        Toast.makeText(DownloadService.this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示指定标题的进度条通知
     *
     * @param title    显示标题
     * @param progress 进度条 小于0就不显示进度条
     * @return Notification对象
     */
    private Notification getNotification(String title, int progress) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .setContentTitle(title);
        if (progress > 0) {
            // 当progress大于或等于0时 才需要显示下载速度
            builder
                    .setContentText(progress + "%")
                    .setProgress(100, progress, false);
        }

        return builder.build();
    }

    /**
     * 获取通知管理器
     *
     * @return 通知管理器
     */
    private NotificationManager getNotificationManager() {
        return ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));
    }
}
