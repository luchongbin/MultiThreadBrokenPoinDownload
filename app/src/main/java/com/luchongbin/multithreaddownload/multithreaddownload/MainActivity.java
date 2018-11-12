package com.luchongbin.multithreaddownload.multithreaddownload;

import android.Manifest;
import android.app.Instrumentation;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.Toast;
import com.luchongbin.multithreaddownload.multithreaddownload.databinding.ActivityMainBinding;
import com.luchongbin.multithreaddownload.multithreaddownload.viewmodel.MainViewModel;
import com.luchongbin.multithreeddownloallibrary.DownloadManager;

public class MainActivity extends FragmentActivity {
    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setPermissions();
        mainViewModel = new MainViewModel(this,binding);
        mainViewModel.initData(mainViewModel);

    }
    @Override
    public void onPause() {
        super.onPause();
        DownloadManager.getInstance().pauseAll();
    }
    private void setPermissions(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "权限已被拒绝", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
