package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jianfanjia.cn.adapter.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.view.baseview.ViewWrapper;
import com.jianfanjia.cn.view.custom_annotation_view.ProcessView;
import com.jianfanjia.cn.view.custom_annotation_view.ProcessView_;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class ProcessNewAdapter extends RecyclerViewAdapterBase<ProcessInfo, ProcessView> {

    Context context;
    private ClickCallBack clickCallBack;

    public ProcessNewAdapter(Context context, ClickCallBack cickCallBack){
        this.context = context;
        this.clickCallBack = cickCallBack;
    }

    @Override
    protected ProcessView onCreateItemView(ViewGroup parent, int viewType) {
        return ProcessView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ProcessView> viewHolder, int position) {
        ProcessView view = viewHolder.getView();
        ProcessInfo processInfo = items.get(position);

        view.bind(processInfo,clickCallBack,position);
    }

}
