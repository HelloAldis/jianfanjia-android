package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jianfanjia.cn.designer.interf.cutom_annotation.ReqItemFinder;
import com.jianfanjia.cn.designer.interf.cutom_annotation.ReqItemFinderImp;
import com.jianfanjia.cn.designer.view.custom_annotation_view.RequirementItemLovestyleView;
import com.jianfanjia.cn.designer.view.custom_annotation_view.RequirementItemLovestyleView_;

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
public class RequirementItemLoveStyleAdapter extends BaseAdapter{

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

        RequirementItemLovestyleView requirementItemLovestyleView;
        if (convertView == null) {
            requirementItemLovestyleView = RequirementItemLovestyleView_.build(context);
        } else {
            requirementItemLovestyleView = (RequirementItemLovestyleView) convertView;
        }

        requirementItemLovestyleView.bind(getItem(position));

        return requirementItemLovestyleView;
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
