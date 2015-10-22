package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Name: DesignerPlanAdapter
 * User: fengliang
 * Date: 2015-10-22
 * Time: 17:54
 */
public class DesignerPlanAdapter extends BaseListAdapter<List<HashMap<String, Object>>> {

    public DesignerPlanAdapter(Context context, List<List<HashMap<String, Object>>> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder holder = null;
        List<HashMap<String, Object>> arrayListForEveryGridView = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_plan_info,
                    null);
            holder = new ViewHolder();
            holder.vp = (ViewPager) convertView.findViewById(R.id.viewpager);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlanViewAdapter adapter = new PlanViewAdapter(context, arrayListForEveryGridView);
        holder.vp.setAdapter(adapter);

        return convertView;
    }

    public static class ViewHolder {
        ViewPager vp;
    }
}
