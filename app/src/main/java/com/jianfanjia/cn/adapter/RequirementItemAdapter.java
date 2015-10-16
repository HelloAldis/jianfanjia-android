package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinder;
import com.jianfanjia.cn.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.view.custom_annotation_view.RequirementItemView;
import com.jianfanjia.cn.view.custom_annotation_view.RequirementItemView_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-16 10:00
 */
@EBean
public class RequirementItemAdapter extends BaseAdapter{

    List<ReqItemFinderImp.ItemMap> itemMaps;

    @Bean(ReqItemFinderImp.class)
    ReqItemFinder reqItemFinder;

    @RootContext
    Context context;

    public void changeShow(int requsetcode){
        itemMaps = reqItemFinder.findAll(requsetcode);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RequirementItemView requirementItemView;
        if (convertView == null) {
            requirementItemView = RequirementItemView_.build(context);
        } else {
            requirementItemView = (RequirementItemView) convertView;
        }

        requirementItemView.bind(getItem(position));

        return requirementItemView;
    }

    @Override
    public int getCount() {
        return itemMaps == null ? 0 : itemMaps.size();
    }

    @Override
    public ReqItemFinderImp.ItemMap getItem(int position) {
        return itemMaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
