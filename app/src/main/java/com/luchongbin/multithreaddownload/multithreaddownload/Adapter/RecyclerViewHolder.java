package com.luchongbin.multithreaddownload.multithreaddownload.Adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2018/7/26/026.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    //这里只要给RecyclerView.ViewHolder返回一个view就可以，所以我们将构造方法中传入databinding
    ViewDataBinding itemMvvmBinding;

    public RecyclerViewHolder(ViewDataBinding itemMvvmBinding) {
        super(itemMvvmBinding.getRoot());
        this.itemMvvmBinding = itemMvvmBinding;
    }

    public ViewDataBinding getBinding() {
        return itemMvvmBinding;
    }

    public void setBinding(ViewDataBinding itemMvvmBinding) {
        this.itemMvvmBinding = itemMvvmBinding;
    }
}
