package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.designer.adapter.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.designer.bean.OrderDesignerInfo;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.view.baseview.ViewWrapper;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class MyUnHandleRequirementAdapter extends RecyclerViewAdapterBase<OrderDesignerInfo, View> {

    Context context;
    private ClickCallBack clickCallBack;

    public MyUnHandleRequirementAdapter(Context context, ClickCallBack cickCallBack) {
        this.context = context;
        this.clickCallBack = cickCallBack;
    }

    @Override
    public int getItemViewType(int position) {
        OrderDesignerInfo orderDesignerInfo = items.get(position);
        String status = orderDesignerInfo.getPlan().getStatus();
        return Integer.parseInt(status);
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<View> viewHolder, int position) {

    }
}
