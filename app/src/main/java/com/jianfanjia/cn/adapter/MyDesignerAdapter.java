package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.adapter.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.view.baseview.ViewWrapper;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType1_;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType2_;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType3_;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class MyDesignerAdapter extends RecyclerViewAdapterBase<OrderDesignerInfo, View> {

    public static final int PLAN_TYPE1 = 0;
    public static final int PLAN_TYPE2 = 1;
    public static final int PLAN_TYPE3 = 2;

    Context context;
    private ClickCallBack clickCallBack;

    public MyDesignerAdapter(Context context, ClickCallBack cickCallBack){
        this.context = context;
        this.clickCallBack = cickCallBack;
    }

    @Override
    public int getItemViewType(int position) {
        OrderDesignerInfo orderDesignerInfo = items.get(position);
        String status = orderDesignerInfo.getPlan().getStatus();
        if(status.equals(Global.PLAN_STATUS0)){
            return PLAN_TYPE3;
        }else if(status.equals(Global.PLAN_STATUS2)){
            return PLAN_TYPE2;
        }else{
            return PLAN_TYPE1;
        }
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case PLAN_TYPE3:
                return MyDesignerViewType3_.build(context);
            case PLAN_TYPE2:
                return MyDesignerViewType2_.build(context);
            case PLAN_TYPE1:
                return MyDesignerViewType1_.build(context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<View> viewHolder, int position) {
        OrderDesignerInfo orderDesignerInfo = items.get(position);
        switch (getItemViewType(position)){
            case PLAN_TYPE3:
                MyDesignerViewType3_ view3 = (MyDesignerViewType3_)viewHolder.getView();
                view3.bind(orderDesignerInfo,clickCallBack,position);
                break;
            case PLAN_TYPE2:
                MyDesignerViewType2_ view2 = (MyDesignerViewType2_)viewHolder.getView();
                view2.bind(orderDesignerInfo,clickCallBack,position);
                break;
            case PLAN_TYPE1:
                MyDesignerViewType1_ view1 = (MyDesignerViewType1_)viewHolder.getView();
                view1.bind(orderDesignerInfo,clickCallBack,position);
                break;
        }

    }

}
