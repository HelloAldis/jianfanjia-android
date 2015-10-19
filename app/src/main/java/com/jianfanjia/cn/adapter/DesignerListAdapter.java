package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.DesignerListInfo;
import com.jianfanjia.cn.interf.ListItemClickListener;

import java.util.List;

/**
 * Name: DesignerListAdapter
 * User: fengliang
 * Date: 2015-10-14
 * Time: 14:03
 */
public class DesignerListAdapter extends BaseListAdapter<DesignerListInfo> {
    private ListItemClickListener listener;

    public DesignerListAdapter(Context context, List<DesignerListInfo> list) {
        super(context, list);
    }

    public DesignerListAdapter(Context context, List<DesignerListInfo> list, ListItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }


    @Override
    public View initView(final int position, View convertView) {
        ViewHolder viewHolder = null;
        DesignerListInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_designer_info,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemProductView = (ImageView) convertView
                    .findViewById(R.id.list_item_product_img);
            viewHolder.itemHeadView = (ImageView) convertView
                    .findViewById(R.id.list_item_head_img);
            viewHolder.itemXiaoQuText = (TextView) convertView
                    .findViewById(R.id.list_item_xiaoqu_text);
            viewHolder.itemProduceText = (TextView) convertView
                    .findViewById(R.id.list_item_produce_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemProductView.setImageResource(R.mipmap.bg_home_banner2);
        viewHolder.itemHeadView.setImageResource(R.mipmap.pix_default);
        viewHolder.itemXiaoQuText.setText(info.getXiaoquInfo());
        viewHolder.itemProduceText.setText(info.getProduceInfo());
        viewHolder.itemProductView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMaxClick(position);
            }
        });

        viewHolder.itemHeadView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMinClick(position);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView itemProductView;
        ImageView itemHeadView;
        TextView itemXiaoQuText;
        TextView itemProduceText;
    }

}
