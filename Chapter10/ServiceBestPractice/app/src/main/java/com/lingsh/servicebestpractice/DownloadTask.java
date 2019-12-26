package com.lingsh.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载任务 (异步多线程)
 * <p>
 * 1. 异步进行下载任务
 * 2. 更新下载进度
 * <p>
 * 泛型：
 * String:  在进行后台任务时 需要传入的参数类型 (此处需要传入目标网址的字符串)
 * Integer: 用于表示进度的类型 使用数字表示下载进度 调用publishProgress()-->onProgressUpdate()进行更新进度
 * Integer: 完成后台任务的返回类型 使用onPostExecute()接收处理
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/26 14:26
 **/


public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    /**
     * 下载完成返回：成功
     */
    public static final int TYPE_SUCCESS = 0;
    /**
     * 下载完成返回：失败
     */
    public static final int TYPE_FAILED = 1;
    /**
     * 下载完成返回：暂停
     */
    public static final int TYPE_PAUSED = 2;
    /**
     * 下载完成返回：取消
     */
    public static final int TYPE_CANCELED = 3;

    private static final String TAG = "DownloadTask";

    /**
     * 是否取消下载任务
     * <p>
     * true: 已取消下载任务
     * 默认为false
     */
    private boolean isCanceled = false;
    /**
     * 是否暂停下载任务
     * <p>
     * false: 已暂停下载任务
     * 默认为false
     */
    private boolean isPaused = false;
    /**
     * 上一次下载任务的进度
     */
    private int lastProgress;

    /**
     * 下载任务监听器
     */
    private DownloadListener mListener;

    /**
     * 通过构造器 注册下载任务的监听器
     *
     * @param listener 下载任务监听器
     */
    public DownloadTask(DownloadListener listener) {
        mListener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        Log.d(TAG, "开始进行后台任务: strings = [" + Arrays.toString(strings) + "]");

        InputStream is = null;
        RandomAccessFile saved = null;
        File file = null;
        try {
            // 记录已经下载的文件长度
            // 用于判断下载进度和断点续传
            long downloadedLength = 0;
            String downloadUrl = strings[0];

            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            // SD卡Download目录
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            Log.d(TAG, "存储路径：" + directory + " 文件：" + fileName);
            file = new File(directory + fileName);
            // 准备续传
            if (file.exists()) {
                downloadedLength = file.length();
            }

            long contentLength = getContentLength(downloadUrl);
            // 判断在下载地址上所获取的文件大小
            if (contentLength == 0) {
                // 文件大小为0
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                // 已下载字节和文件总字节相等 说明已经下载完成了
                return TYPE_SUCCESS;
            }

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // 断点下载 指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                saved = new RandomAccessFile(file, "rw");
                // 跳过已下载的字节
                saved.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = (is.read(b))) != -1) {
                    if (isCanceled) {
                        // 取消下载任务
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        // 暂停下载任务
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        saved.write(b, 0, len);
                        // 计算已下载的百分比
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (saved != null) {
                    saved.close();
                }
                // 取消下载任务 删除已下载文件
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return TYPE_FAILED;
    }

    /**
     * 获取待下载文件的总长度
     *
     * @param downloadUrl 文件的下载地址
     * @return 待下载文件的总长度
     */
    private long getContentLength(String downloadUrl) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(downloadUrl).build();
            Response response = client.newCall(request).execute();
            if (request != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.body().close();
                Log.d(TAG, "文件路径 = [" + downloadUrl + "] 文件大小 = " + contentLength);
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(TAG, "更新进度条: values = [" + Arrays.toString(values) + "]");
        int progress = values[0];
        if (progress > lastProgress) {
            mListener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer state) {
        Log.d(TAG, "任务执行结果: state = [" + state + "]");
        switch (state) {
            case TYPE_SUCCESS:
                mListener.onSuccess();
                break;
            case TYPE_FAILED:
                mListener.onFailed();
                break;
            case TYPE_PAUSED:
                mListener.onPaused();
                break;
            case TYPE_CANCELED:
                mListener.onCanceled();
                break;
            default:
                break;
        }
    }

    /**
     * 用户暂停下载
     */
    public void pauseDownload() {
        isPaused = true;
    }

    /**
     * 用户取消下载
     */
    public void cancelDownload() {
        isCanceled = true;
    }
}
