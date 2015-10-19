package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jianfanjia.cn.adapter.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.view.baseview.ViewWrapper;
import com.jianfanjia.cn.view.custom_annotation_view.RequirementView;
import com.jianfanjia.cn.view.custom_annotation_view.RequirementView_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Description: com.jianfanjia.cn.adapter.base
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 19:15
 */
@EBean
public class RequirementNewAdapter extends RecyclerViewAdapterBase<RequirementInfo, RequirementView> {

    @RootContext
    Context context;

    @Override
    protected RequirementView onCreateItemView(ViewGroup parent, int viewType) {
        return RequirementView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<RequirementView> viewHolder, int position) {
        RequirementView view = viewHolder.getView();
        RequirementInfo requirementInfo = items.get(position);

        view.bind(requirementInfo);
    }

}
