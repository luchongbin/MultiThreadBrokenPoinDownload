package com.luchongbin.multithreeddownloallibrary.core;


import android.os.Process;
import android.text.TextUtils;


import com.luchongbin.multithreeddownloallibrary.Constants;
import com.luchongbin.multithreeddownloallibrary.DownloadException;
import com.luchongbin.multithreeddownloallibrary.DownloadInfo;
import com.luchongbin.multithreeddownloallibrary.architecture.DownloadStatus;
import com.luchongbin.multithreeddownloallibrary.architecture.DownloadTask;
import com.luchongbin.multithreeddownloallibrary.db.ThreadInfo;
import com.luchongbin.multithreeddownloallibrary.util.IOCloseUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by luchongbin on 2018/10/30.
 */
public abstract class DownloadTaskImpl implements DownloadTask {

    private String mTag;

    private final DownloadInfo mDownloadInfo;
    private final ThreadInfo mThreadInfo;
    private final OnDownloadListener mOnDownloadListener;

    private volatile int mStatus;

    private volatile int mCommend = 0;

    public DownloadTaskImpl(DownloadInfo downloadInfo, ThreadInfo threadInfo, OnDownloadListener listener) {
        this.mDownloadInfo = downloadInfo;
        this.mThreadInfo = threadInfo;
        this.mOnDownloadListener = listener;

        this.mTag = getTag();
        if (TextUtils.isEmpty(mTag)) {
            mTag = this.getClass().getSimpleName();
        }
    }

    @Override
    public void cancel() {
        mCommend = DownloadStatus.STATUS_CANCELED;
    }

    @Override
    public void pause() {
        mCommend = DownloadStatus.STATUS_PAUSED;
    }

    @Override
    public boolean isDownloading() {
        return mStatus == DownloadStatus.STATUS_PROGRESS;
    }

    @Override
    public boolean isComplete() {
        return mStatus == DownloadStatus.STATUS_COMPLETED;
    }

    @Override
    public boolean isPaused() {
        return mStatus == DownloadStatus.STATUS_PAUSED;
    }

    @Override
    public boolean isCanceled() {
        return mStatus == DownloadStatus.STATUS_CANCELED;
    }

    @Override
    public boolean isFailed() {
        return mStatus == DownloadStatus.STATUS_FAILED;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        insertIntoDB(mThreadInfo);
        try {
            mStatus = DownloadStatus.STATUS_PROGRESS;
            executeDownload();
            synchronized (mOnDownloadListener) {
                mStatus = DownloadStatus.STATUS_COMPLETED;
                mOnDownloadListener.onDownloadCompleted();
            }
        } catch (DownloadException e) {
            handleDownloadException(e);
        }
    }

    private void handleDownloadException(DownloadException e) {
        switch (e.getErrorCode()) {
            case DownloadStatus.STATUS_FAILED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_FAILED;
                    mOnDownloadListener.onDownloadFailed(e);
                }
                break;
            case DownloadStatus.STATUS_PAUSED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_PAUSED;
                    mOnDownloadListener.onDownloadPaused();
                }
                break;
            case DownloadStatus.STATUS_CANCELED:
                synchronized (mOnDownloadListener) {
                    mStatus = DownloadStatus.STATUS_CANCELED;
                    mOnDownloadListener.onDownloadCanceled();
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown state");
        }
    }

    private void executeDownload() throws DownloadException {
        final URL url;
        try {
            url = new URL(mThreadInfo.getUri());
        } catch (MalformedURLException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "Bad url.", e);
        }

        HttpURLConnection httpConnection = null;
        try {
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(Constants.HTTP.CONNECT_TIME_OUT);
            httpConnection.setReadTimeout(Constants.HTTP.READ_TIME_OUT);
            httpConnection.setRequestMethod(Constants.HTTP.GET);
            setHttpHeader(getHttpHeaders(mThreadInfo), httpConnection);
            final int responseCode = httpConnection.getResponseCode();
            if (responseCode == getResponseCode()) {
                transferData(httpConnection);
            } else {
                throw new DownloadException(DownloadStatus.STATUS_FAILED, "UnSupported response code:" + responseCode);
            }
        } catch (ProtocolException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "Protocol error", e);
        } catch (IOException e) {
            throw new DownloadException(DownloadStatus.STATUS_FAILED, "IO error", e);
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    private void setHttpHeader(Map<String, String> headers, URLConnection connection) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }
    }

    private void transferData(HttpURLConnection httpConnection) throws DownloadException {
        InputStream inputStream = null;
        RandomAccessFile raf = null;
        try {
            try {
                inputStream = httpConnection.getInputStream();
            } catch (IOException e) {
                throw new DownloadException(DownloadStatus.STATUS_FAILED, "http get inputStream error", e);
            }
            final long offset = mThreadInfo.getStart() + mThreadInfo.getFinished();
            try {
                raf = getFile(mDownloadInfo.getDir(), mDownloadInfo.getName(), offset);
            } catch (IOException e) {
                throw new DownloadException(DownloadStatus.STATUS_FAILED, "File error", e);
            }
            transferData(inputStream, raf);
        } finally {
            try {
                IOCloseUtils.close(inputStream);
                IOCloseUtils.close(raf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void transferData(InputStream inputStream, RandomAccessFile raf) throws DownloadException {
        final byte[] buffer = new byte[1024 * 8];
        while (true) {
            checkPausedOrCanceled();
            int len = -1;
            try {
                len = inputStream.read(buffer);
                if (len == -1) {
                    break;
                }
                raf.write(buffer, 0, len);
                mThreadInfo.setFinished(mThreadInfo.getFinished() + len);
                synchronized (mOnDownloadListener) {
                    mDownloadInfo.setFinished(mDownloadInfo.getFinished() + len);
                    mOnDownloadListener.onDownloadProgress(mDownloadInfo.getFinished(), mDownloadInfo.getLength());
                }
            } catch (IOException e) {
                updateDB(mThreadInfo);
                throw new DownloadException(DownloadStatus.STATUS_FAILED, e);
            }
        }
    }

    private void checkPausedOrCanceled() throws DownloadException {
        if (mCommend == DownloadStatus.STATUS_CANCELED) {
            // cancel
            throw new DownloadException(DownloadStatus.STATUS_CANCELED, "Download canceled!");
        } else if (mCommend == DownloadStatus.STATUS_PAUSED) {
            // pause
            updateDB(mThreadInfo);
            throw new DownloadException(DownloadStatus.STATUS_PAUSED, "Download paused!");
        }
    }


    protected abstract void insertIntoDB(ThreadInfo info);

    protected abstract int getResponseCode();

    protected abstract void updateDB(ThreadInfo info);

    protected abstract Map<String, String> getHttpHeaders(ThreadInfo info);

    protected abstract RandomAccessFile getFile(File dir, String name, long offset) throws IOException;

    protected abstract String getTag();
}