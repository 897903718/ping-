package com.arialyy.frame.http.inf;

/**
 * 下载监听
 */
public interface IDownloadListener {
    /**
     * 下载监听
     */
    public void onProgress(int currentPosition);

    /**
     * 文件大小监听
     */
    public void onFileSize(int fileSize);

    /**
     * 单一线程的结束位置
     */
    public void onFinish(int finishPosition);

    /**
     * 开始
     */
    public void onStart(int startPosition);

    /**
     * 暂停
     */
    public void onPause(int pausePosition);

    /**
     * 停止
     */
    public void onStop(int stopPosition);

    /**
     * 下载完成
     */
    public void onComplete();

}