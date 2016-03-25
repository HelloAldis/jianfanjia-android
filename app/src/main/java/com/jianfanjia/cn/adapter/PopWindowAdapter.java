package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;

/**
 * Name: PopWindowAdapter
 * User: fengliang
 * Date: 2015-12-14
 * Time: 16:07
 */
public class PopWindowAdapter extends BaseListAdapter<String> {
    private int mSelectedPos = -1;

    public PopWindowAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View initView(final int position, View convertView) {
        final String title = list.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gird_item_pop_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mName.setText(title);
        if (mSelectedPos == position) {
            holder.mName.setTextColor(context.getResources().getColor(R.color.font_white));
            holder.mName.setBackgroundResource(R.drawable.text_bg_border);
        } else {
            holder.mName.setTextColor(context.getResources().getColor(R.color.home_page_text_color));
            holder.mName.setBackgroundResource(R.drawable.text_bg_normal_border);
        }
        return convertView;
    }

    public void setSelectedPos(int position) {
        this.mSelectedPos = position;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.title_item)
        TextView mName;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}  
