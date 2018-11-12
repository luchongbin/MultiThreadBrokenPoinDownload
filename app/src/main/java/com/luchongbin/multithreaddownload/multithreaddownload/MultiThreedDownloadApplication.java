package com.luchongbin.multithreaddownload.multithreaddownload;

import android.app.Application;
import android.content.Context;

import com.luchongbin.multithreeddownloallibrary.DownloadConfiguration;
import com.luchongbin.multithreeddownloallibrary.DownloadManager;


/**
 * Created by luchongbin.
 */
public class MultiThreedDownloadApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        initDownloader();
        CrashHandler.getInstance(sContext);
    }

    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }

    public static Context getContext() {
        return sContext;
    }


}
