package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;

import java.util.List;

/**
 * Name: PopWindowAdapter
 * User: fengliang
 * Date: 2015-12-14
 * Time: 16:07
 */
public class PopWindowAdapter extends BaseListAdapter<String> {

    public PopWindowAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        String title = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gird_item_pop_item, null);
            holder = new ViewHolder();
            holder.mName = (TextView) convertView.findViewById(R.id.title_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mName.setText(title);

        return convertView;
    }

    private static class ViewHolder {
        public TextView mName = null;
    }
}  
