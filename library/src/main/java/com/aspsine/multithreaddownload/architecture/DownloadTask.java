package com.aspsine.multithreaddownload.architecture;

import com.aspsine.multithreaddownload.DownloadException;

/**
 * Created by luchongbin on 2018/10/30.
 */
public interface DownloadTask extends Runnable {

    interface OnDownloadListener {
        void onDownloadConnecting();

        void onDownloadProgress(long finished, long length);

        void onDownloadCompleted();

        void onDownloadPaused();

        void onDownloadCanceled();

        void onDownloadFailed(DownloadException de);
    }

    void cancel();

    void pause();

    boolean isDownloading();

    boolean isComplete();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();
}
