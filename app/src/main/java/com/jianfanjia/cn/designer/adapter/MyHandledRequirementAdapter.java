package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.designer.adapter.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.view.baseview.ViewWrapper;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType0_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType1_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType2_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType3_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType4_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType5_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType6_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType7_;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType8_;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class MyHandledRequirementAdapter extends RecyclerViewAdapterBase<RequirementInfo, View> {
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
        RequirementInfo requirementInfo = items.get(position);
        String status = requirementInfo.getPlan().getStatus();
        return Integer.parseInt(status);
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case PLAN_TYPE0:
                return MyPlanViewType0_.build(context);
            case PLAN_TYPE2:
                return MyPlanViewType2_.build(context);
            case PLAN_TYPE1:
                return MyPlanViewType1_.build(context);
            case PLAN_TYPE3:
                return MyPlanViewType3_.build(context);
            case PLAN_TYPE4:
                return MyPlanViewType4_.build(context);
            case PLAN_TYPE5:
                return MyPlanViewType5_.build(context);
            case PLAN_TYPE6:
                return MyPlanViewType6_.build(context);
            case PLAN_TYPE7:
                return MyPlanViewType7_.build(context);
            case PLAN_TYPE8:
                return MyPlanViewType8_.build(context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<View> viewHolder, int position) {
        RequirementInfo requirementInfo = items.get(position);
        switch (getItemViewType(position)) {
            case PLAN_TYPE0:
                MyPlanViewType0_ view0 = (MyPlanViewType0_) viewHolder.getView();
                view0.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE2:
                MyPlanViewType2_ view2 = (MyPlanViewType2_) viewHolder.getView();
                view2.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE1:
                MyPlanViewType1_ view1 = (MyPlanViewType1_) viewHolder.getView();
                view1.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE3:
                MyPlanViewType3_ view3 = (MyPlanViewType3_) viewHolder.getView();
                view3.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE4:
                MyPlanViewType4_ view4 = (MyPlanViewType4_) viewHolder.getView();
                view4.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE5:
                MyPlanViewType5_ view5 = (MyPlanViewType5_) viewHolder.getView();
                view5.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE6:
                MyPlanViewType6_ view6 = (MyPlanViewType6_) viewHolder.getView();
                view6.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE7:
                MyPlanViewType7_ view7 = (MyPlanViewType7_) viewHolder.getView();
                view7.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE8:
                MyPlanViewType8_ view8 = (MyPlanViewType8_) viewHolder.getView();
                view8.bind(requirementInfo, clickCallBack, position);
                break;
        }
    }
}
