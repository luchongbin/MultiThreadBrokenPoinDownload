package com.aspsine.multithreaddownload.architecture;

/**
 * Created by luchongbin on 2018/10/30.
 */
public interface DownloadStatusDelivery {

    void post(DownloadStatus status);

}
