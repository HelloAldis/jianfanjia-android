package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType0;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType1;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType2;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType3;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType4;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType5;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType6;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType7;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType8;
import com.jianfanjia.cn.view.custom_annotation_view.MyDesignerViewType9;

/**
 * Description: com.jianfanjia.cn.base.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class MyDesignerAdapter extends RecyclerViewAdapterBase<Designer> {
    public static final int PLAN_TYPE0 = 0;
    public static final int PLAN_TYPE1 = 1;
    public static final int PLAN_TYPE2 = 2;
    public static final int PLAN_TYPE3 = 3;
    public static final int PLAN_TYPE4 = 4;
    public static final int PLAN_TYPE5 = 5;
    public static final int PLAN_TYPE6 = 6;
    public static final int PLAN_TYPE7 = 7;
    public static final int PLAN_TYPE8 = 8;
    public static final int PLAN_TYPE9 = 9;

    Context context;
    private String workType;
    private ClickCallBack clickCallBack;

    public MyDesignerAdapter(Context context, String workType, ClickCallBack cickCallBack) {
        this.context = context;
        this.workType = workType;
        this.clickCallBack = cickCallBack;
    }

    @Override
    public int getItemViewType(int position) {
        Designer orderDesignerInfo = items.get(position);
        String status = orderDesignerInfo.getPlan().getStatus();
        return Integer.parseInt(status);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PLAN_TYPE0:
                return MyDesignerViewType0.build(context);
            case PLAN_TYPE1:
                return MyDesignerViewType1.build(context);
            case PLAN_TYPE2:
                return MyDesignerViewType2.build(context);
            case PLAN_TYPE3:
                return MyDesignerViewType3.build(context);
            case PLAN_TYPE4:
                return MyDesignerViewType4.build(context);
            case PLAN_TYPE5:
                return MyDesignerViewType5.build(context, workType);
            case PLAN_TYPE6:
                return MyDesignerViewType6.build(context);
            case PLAN_TYPE7:
                return MyDesignerViewType7.build(context);
            case PLAN_TYPE8:
                return MyDesignerViewType8.build(context);
            case PLAN_TYPE9:
                return MyDesignerViewType9.build(context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Designer orderDesignerInfo = items.get(position);
        switch (getItemViewType(position)) {
            case PLAN_TYPE0:
                MyDesignerViewType0 view0 = (MyDesignerViewType0) holder;
                view0.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE1:
                MyDesignerViewType1 view1 = (MyDesignerViewType1) holder;
                view1.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE2:
                MyDesignerViewType2 view2 = (MyDesignerViewType2) holder;
                view2.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE3:
                MyDesignerViewType3 view3 = (MyDesignerViewType3) holder;
                view3.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE4:
                MyDesignerViewType4 view4 = (MyDesignerViewType4) holder;
                view4.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE5:
                MyDesignerViewType5 view5 = (MyDesignerViewType5) holder;
                view5.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE6:
                MyDesignerViewType6 view6 = (MyDesignerViewType6) holder;
                view6.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE7:
                MyDesignerViewType7 view7 = (MyDesignerViewType7) holder;
                view7.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE8:
                MyDesignerViewType8 view8 = (MyDesignerViewType8) holder;
                view8.bind(orderDesignerInfo, clickCallBack, position);
                break;
            case PLAN_TYPE9:
                MyDesignerViewType9 view9 = (MyDesignerViewType9) holder;
                view9.bind(orderDesignerInfo, clickCallBack, position);
                break;
        }
    }
}
