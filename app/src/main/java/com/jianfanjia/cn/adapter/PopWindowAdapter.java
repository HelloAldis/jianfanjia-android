package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.interf.OnItemClickListener;

import java.util.List;

/**
 * Name: PopWindowAdapter
 * User: fengliang
 * Date: 2015-12-14
 * Time: 16:07
 */
public class PopWindowAdapter extends BaseListAdapter<String> {
    private OnItemClickListener listener;

    public PopWindowAdapter(Context context, List<String> list, OnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public View initView(final int position, View convertView) {
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
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(position);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        public TextView mName = null;
    }
}  
