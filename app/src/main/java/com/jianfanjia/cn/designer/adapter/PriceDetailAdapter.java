package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.designer.bean.PriceDetail;

import java.util.List;

/**
 * Name: PriceDetailAdapter
 * User: fengliang
 * Date: 2015-10-23
 * Time: 16:14
 */
public class PriceDetailAdapter extends BaseListAdapter<PriceDetail> {

    public PriceDetailAdapter(Context context, List<PriceDetail> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder holder = null;
        PriceDetail detail = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_price_item,
                    null);
            holder = new ViewHolder();
            holder.itemTitle = (TextView) convertView.findViewById(R.id.titleText);
            holder.itemDes = (TextView) convertView.findViewById(R.id.desText);
            holder.itemContent = (TextView) convertView.findViewById(R.id.contentText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemTitle.setText(detail.getItem());
        holder.itemDes.setText(detail.getDescription());
        holder.itemContent.setText("" + detail.getPrice() + "元");
        return convertView;
    }

    private static class ViewHolder {
        TextView itemTitle;
        TextView itemDes;
        TextView itemContent;
    }
}
