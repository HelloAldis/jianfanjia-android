package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jianfanjia.cn.designer.adapter.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.designer.bean.RequirementInfo;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.view.baseview.ViewWrapper;
import com.jianfanjia.cn.designer.view.custom_annotation_view.RequirementView;
import com.jianfanjia.cn.designer.view.custom_annotation_view.RequirementView_;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
public class RequirementNewAdapter extends RecyclerViewAdapterBase<RequirementInfo, RequirementView> {
    private Context context;
    private ClickCallBack clickCallBack;

    public RequirementNewAdapter(Context context, ClickCallBack cickCallBack) {
        this.context = context;
        this.clickCallBack = cickCallBack;
    }

    @Override
    protected RequirementView onCreateItemView(ViewGroup parent, int viewType) {
        return RequirementView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<RequirementView> viewHolder, int position) {
        RequirementView view = viewHolder.getView();
        RequirementInfo requirementInfo = items.get(position);
        view.bind(requirementInfo, clickCallBack, position);
    }

}
