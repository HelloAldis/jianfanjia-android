package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.designer.base.RecyclerViewAdapterBase;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType0;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType1;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType2;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType3;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType4;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType5;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType6;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType7;
import com.jianfanjia.cn.designer.view.custom_annotation_view.MyPlanViewType8;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class MyHandledRequirementAdapter extends RecyclerViewAdapterBase<Requirement> {
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
    private LayoutInflater inflater;

    public MyHandledRequirementAdapter(Context context, ClickCallBack cickCallBack) {
        this.context = context;
        this.clickCallBack = cickCallBack;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        Requirement requirementInfo = items.get(position);
        String status = requirementInfo.getPlan().getStatus();
        return Integer.parseInt(status);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PLAN_TYPE0:
                return MyPlanViewType0.build(context);
            case PLAN_TYPE2:
                return MyPlanViewType2.build(context);
            case PLAN_TYPE1:
                return MyPlanViewType1.build(context);
            case PLAN_TYPE3:
                return MyPlanViewType3.build(context);
            case PLAN_TYPE4:
                return MyPlanViewType4.build(context);
            case PLAN_TYPE5:
                return MyPlanViewType5.build(context);
            case PLAN_TYPE6:
                return MyPlanViewType6.build(context);
            case PLAN_TYPE7:
                return MyPlanViewType7.build(context);
            case PLAN_TYPE8:
                return MyPlanViewType8.build(context);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Requirement requirementInfo = items.get(position);
        switch (getItemViewType(position)) {
            case PLAN_TYPE0:
                MyPlanViewType0 view0 = (MyPlanViewType0) viewHolder;
                view0.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE2:
                MyPlanViewType2 view2 = (MyPlanViewType2) viewHolder;
                view2.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE1:
                MyPlanViewType1 view1 = (MyPlanViewType1) viewHolder;
                view1.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE3:
                MyPlanViewType3 view3 = (MyPlanViewType3) viewHolder;
                view3.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE4:
                MyPlanViewType4 view4 = (MyPlanViewType4) viewHolder;
                view4.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE5:
                MyPlanViewType5 view5 = (MyPlanViewType5) viewHolder;
                view5.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE6:
                MyPlanViewType6 view6 = (MyPlanViewType6) viewHolder;
                view6.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE7:
                MyPlanViewType7 view7 = (MyPlanViewType7) viewHolder;
                view7.bind(requirementInfo, clickCallBack, position);
                break;
            case PLAN_TYPE8:
                MyPlanViewType8 view8 = (MyPlanViewType8) viewHolder;
                view8.bind(requirementInfo, clickCallBack, position);
                break;
        }
    }

    protected void cacularView(ViewGroup parent,View view){
        LogTool.d(this.getClass().getName(), "parent height =" + parent.getHeight() + " parent width =" + parent.getWidth());
//        view.setLayoutParams();
        view.setLayoutParams(new ViewGroup.LayoutParams(parent.getWidth(),parent.getHeight()));
    }

}
