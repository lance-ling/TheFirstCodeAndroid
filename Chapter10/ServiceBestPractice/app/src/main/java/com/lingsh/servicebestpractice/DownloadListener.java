package com.lingsh.servicebestpractice;

/**
 * 监听下载任务的状态
 * <p>
 * 1. 监听下载任务的进度
 * 2. 监听下载任务的操作状态 成功/失败/暂停/取消
 *
 * @author lingsh
 * @version 1.0
 * @date 2019/12/26 14:19
 **/


public interface DownloadListener {

    /**
     * 监听下载的进度
     *
     * @param progress 下载任务进度 必须不小于0的整数
     */
    void onProgress(int progress);

    /**
     * 监听下载成功的状态
     */
    void onSuccess();

    /**
     * 监听下载失败的状态
     */
    void onFailed();

    /**
     * 监听下载暂停的状态
     */
    void onPaused();

    /**
     * 监听下载取消的状态
     */
    void onCanceled();
}
