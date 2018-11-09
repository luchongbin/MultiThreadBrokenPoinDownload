package com.aspsine.multithreaddownload.architecture;

import com.aspsine.multithreaddownload.DownloadException;
import com.aspsine.multithreaddownload.DownloadInfo;
/**
 * Created by luchongbin on 2018/10/30.
 */
public interface ConnectTask extends Runnable {

    interface OnConnectListener {
        void onConnecting();

        void onConnected(long time, long length, boolean isAcceptRanges);

        void onConnectPaused();

        void onConnectCanceled();

        void onConnectFailed(DownloadException de);
    }

    void pause();

    void cancel();

    boolean isConnecting();

    boolean isConnected();

    boolean isPaused();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();
}
