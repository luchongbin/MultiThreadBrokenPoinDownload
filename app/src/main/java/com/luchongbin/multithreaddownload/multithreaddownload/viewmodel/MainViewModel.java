package com.luchongbin.multithreaddownload.multithreaddownload.viewmodel;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.luchongbin.multithreaddownload.multithreaddownload.Adapter.RecycleViewTestAdapter;
import com.luchongbin.multithreaddownload.multithreaddownload.DataSource;
import com.luchongbin.multithreaddownload.multithreaddownload.databinding.ActivityMainBinding;
import com.luchongbin.multithreaddownload.multithreaddownload.model.DataSourceModel;
import com.luchongbin.multithreeddownloallibrary.CallBack;
import com.luchongbin.multithreeddownloallibrary.DownloadException;
import com.luchongbin.multithreeddownloallibrary.DownloadInfo;
import com.luchongbin.multithreeddownloallibrary.DownloadManager;
import com.luchongbin.multithreeddownloallibrary.DownloadRequest;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2018/11/10/010.
 */

public class MainViewModel {
    private Context mContext;
    private ActivityMainBinding mBinding;
    private RecycleViewTestAdapter mRecycleViewTestAdapter;
    private List<DataSourceModel> list;
    private File mDownloadDir;
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public MainViewModel(Context mContext, ActivityMainBinding binding) {
        this.mContext = mContext;
        this.mBinding = binding;
        mDownloadDir = new File(Environment.getExternalStorageDirectory(), "Download");

    }

    public void initData(MainViewModel mainViewModel) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.xRecyclerView.setLayoutManager(layoutManager);
        list = DataSource.getInstance().getData();
        for (DataSourceModel info : list) {
            DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(info.getUrl());
            setDataSourceModel(info,DataSourceModel.STATUS_PAUSED);
            if (downloadInfo != null) {
                int mProgress = downloadInfo.getProgress();
                info.setProgress(mProgress);
                info.setDownloadPerSize(getDownloadPerSize(downloadInfo.getFinished(), downloadInfo.getLength()));

            }else{
                setDataSourceModel(info,DataSourceModel.STATUS_NOT_DOWNLOAD);
            }
        }
        mRecycleViewTestAdapter = new RecycleViewTestAdapter(mContext, mainViewModel, list);
        mBinding.xRecyclerView.setAdapter(mRecycleViewTestAdapter);

    }

    public void itemButton(DataSourceModel mDataSourceModel) {
        if (mDataSourceModel.getStatus() == mDataSourceModel.STATUS_DOWNLOADING || mDataSourceModel.getStatus() == mDataSourceModel.STATUS_CONNECTING) {
            pause(mDataSourceModel.getUrl());//暂停
        } else if (mDataSourceModel.getStatus() == mDataSourceModel.STATUS_COMPLETE) {
//            install(appInfo);  下载完成要做的事
        } else if (mDataSourceModel.getStatus() == mDataSourceModel.STATUS_INSTALLED) {
//            unInstall(appInfo); //可以卸载
        } else {
            download(mDataSourceModel.getUrl(),mDataSourceModel);
        }

    }
    private void pause(String tag) {
        DownloadManager.getInstance().pause(tag);
    }

    private void download(String tag,DataSourceModel mDataSourceModel) {
        final DownloadRequest request = new DownloadRequest.Builder()
                .setName(mDataSourceModel.getName()+".apk")
                .setUri(mDataSourceModel.getUrl())
                .setFolder(mDownloadDir)
                .build();
        DownloadManager.getInstance().download(request, tag, new DownloadCallBack(mDataSourceModel));//可以使用URL作为TAG
    }

    public  class DownloadCallBack implements CallBack {
        private DataSourceModel mDataSourceModel;
        public DownloadCallBack(DataSourceModel mDataSourceModel) {
            this.mDataSourceModel = mDataSourceModel;
        }

        @Override
        public void onStarted() {

        }

        @Override
        public void onConnecting() {
            setDataSourceModel(mDataSourceModel,DataSourceModel.STATUS_CONNECTING);
        }

        @Override
        public void onConnected(long total, boolean isRangeSupport) {
            setDataSourceModel(mDataSourceModel,DataSourceModel.STATUS_DOWNLOADING);
        }

        @Override
        public void onProgress(long finished, long total, int progress) {
            String downloadPerSize = getDownloadPerSize(finished, total);
            mDataSourceModel.setProgress(progress);
            mDataSourceModel.setDownloadPerSize(downloadPerSize);
            setDataSourceModel(mDataSourceModel,DataSourceModel.STATUS_DOWNLOADING);
        }

        @Override
        public void onCompleted() {
            setDataSourceModel(mDataSourceModel,DataSourceModel.STATUS_COMPLETE);
        }

        @Override
        public void onDownloadPaused() {
            setDataSourceModel(mDataSourceModel,DataSourceModel.STATUS_PAUSED);

        }

        @Override
        public void onDownloadCanceled() {
            setDataSourceModel(mDataSourceModel,DataSourceModel.STATUS_NOT_DOWNLOAD);
            mDataSourceModel.setDownloadPerSize("");

        }

        @Override
        public void onFailed(DownloadException e) {
            setDataSourceModel(mDataSourceModel,DataSourceModel.STATUS_DOWNLOAD_ERROR);
            mDataSourceModel.setDownloadPerSize("");
        }
    }
    private void setDataSourceModel(DataSourceModel dataSourceModel,int mStatus){
        dataSourceModel.setStatus(mStatus);
        dataSourceModel.setTvStatus(dataSourceModel.getStatusText());
        dataSourceModel.setBtnDownload(dataSourceModel.getButtonText());
    }


    private String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }
}
