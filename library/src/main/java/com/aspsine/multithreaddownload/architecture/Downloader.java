package com.aspsine.multithreaddownload.architecture;

/**
 * Created by luchongbin on 2018/10/30.
 */
public interface Downloader {

    interface OnDownloaderDestroyedListener {
        void onDestroyed(String key, Downloader downloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();

}
