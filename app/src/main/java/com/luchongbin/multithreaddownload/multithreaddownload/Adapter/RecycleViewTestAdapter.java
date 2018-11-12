package com.luchongbin.multithreaddownload.multithreaddownload.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.luchongbin.multithreaddownload.multithreaddownload.R;
import com.luchongbin.multithreaddownload.multithreaddownload.databinding.ItemAppBinding;
import com.luchongbin.multithreaddownload.multithreaddownload.model.DataSourceModel;
import com.luchongbin.multithreaddownload.multithreaddownload.viewmodel.MainViewModel;

import java.util.List;

/**
 * Created by Administrator on 2018/10/22/022.
 */

public class RecycleViewTestAdapter extends RecyclerView.Adapter<RecyclerViewHolder>  {
    private Context context;
    private List<DataSourceModel> list;
    private MainViewModel mainViewModel;
    public RecycleViewTestAdapter(Context context, MainViewModel mainViewModel, List<DataSourceModel> list){
        this.context = context;
        this.list = list;
        this.mainViewModel = mainViewModel;
    }
    @Override
    public int getItemCount() {
        return  list == null ? 0 : list.size();
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAppBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_app, parent, false);
        return new RecyclerViewHolder(itemBinding);
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        ItemAppBinding itemBinding = (ItemAppBinding)holder.getBinding();
        DataSourceModel mDataSourceModel = list.get(position);
        itemBinding.setMDataSourceModel(mDataSourceModel);
        itemBinding.setMainViewModel(mainViewModel);
        itemBinding.btnDownload.setTag(position);
        // 立刻执行绑定
        itemBinding.executePendingBindings();
//        setListener(holder,myMessage);
    }
//    private void setListener(RecyclerViewHolder holder,final MyMessage myMessage){
////        holder.itemView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent intent= new Intent(context, MessageDetailsActivity.class);
////                intent.putExtra(CommonUtils.MessageDetails.MYMESSAGE,myMessage);
////                context.startActivity(intent);
////            }
////        });
//    }
}
