package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.interf.GetItemCallback;

import java.util.List;

/**
 * Name: PopWindowAdapter
 * User: fengliang
 * Date: 2015-12-14
 * Time: 16:07
 */
public class PopWindowAdapter extends BaseListAdapter<String> {
    private GetItemCallback callback;

    public PopWindowAdapter(Context context, List<String> list, GetItemCallback callback) {
        super(context, list);
        this.callback = callback;
    }

    @Override
    public View initView(final int position, View convertView) {
        final String title = list.get(position);
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
                callback.onItemCallback(position, title);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        public TextView mName = null;
    }
}  
