package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.designer.adapter.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.designer.bean.OrderDesignerInfo;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.view.baseview.ViewWrapper;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType0_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType1_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType2_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType3_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType4_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType5_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType6_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType7_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyDesignerViewType8_;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class MyHandledRequirementAdapter extends RecyclerViewAdapterBase<OrderDesignerInfo, View> {
    public static final int PLAN_TYPE0 = 0;
    public static final int PLAN_TYPE1 = 1;
    public static final int PLAN_TYPE2 = 2;
    public static final int PLAN_TYPE3 = 3;
    public static final int PLAN_TYPE4 = 4;
    public static final int PLAN_TYPE5 = 5;
    public static final int PLAN_TYPE6 = 6;
    public static final int PLAN_TYPE7 = 7;
    public static final int PLAN_TYPE8 = 8;

    Context context;
    private ClickCallBack clickCallBack;

    public MyHandledRequirementAdapter(Context context, ClickCallBack cickCallBack) {
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
            case PLAN_TYPE0:
                return MyDesignerViewType0_.build(context);
            case PLAN_TYPE2:
                return MyDesignerViewType2_.build(context);
            case PLAN_TYPE1:
                return MyDesignerViewType1_.build(context);
            case PLAN_TYPE3:
                return MyDesignerViewType3_.build(context);
            case PLAN_TYPE4:
                return MyDesignerViewType4_.build(context);
            case PLAN_TYPE5:
                return MyDesignerViewType5_.build(context);
            case PLAN_TYPE6:
                return MyDesignerViewType6_.build(context);
            case PLAN_TYPE7:
                return MyDesignerViewType7_.build(context);
            case PLAN_TYPE8:
                return MyDesignerViewType8_.build(context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<View> viewHolder, int position) {
        OrderDesignerInfo orderDesignerInfo = items.get(position);
        switch (getItemViewType(position)) {
            case PLAN_TYPE0:
                MyDesignerViewType0_ view0 = (MyDesignerViewType0_) viewHolder.getView();
                view0.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE2:
                MyDesignerViewType2_ view2 = (MyDesignerViewType2_) viewHolder.getView();
                view2.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE1:
                MyDesignerViewType1_ view1 = (MyDesignerViewType1_) viewHolder.getView();
                view1.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE3:
                MyDesignerViewType3_ view3 = (MyDesignerViewType3_) viewHolder.getView();
                view3.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE4:
                MyDesignerViewType4_ view4 = (MyDesignerViewType4_) viewHolder.getView();
                view4.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE5:
                MyDesignerViewType5_ view5 = (MyDesignerViewType5_) viewHolder.getView();
                view5.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE6:
                MyDesignerViewType6_ view6 = (MyDesignerViewType6_) viewHolder.getView();
                view6.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE7:
                MyDesignerViewType7_ view7 = (MyDesignerViewType7_) viewHolder.getView();
                view7.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE8:
                MyDesignerViewType8_ view8 = (MyDesignerViewType8_) viewHolder.getView();
                view8.bind(orderDesignerInfo, clickCallBack, position);
                break;
        }
    }
}
