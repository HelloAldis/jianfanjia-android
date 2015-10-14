package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DesignerListInfo;

import java.util.List;

/**
 * Name: DesignerListAdapter
 * User: fengliang
 * Date: 2015-10-14
 * Time: 14:03
 */
public class DesignerListAdapter extends BaseListAdapter<DesignerListInfo> {


    public DesignerListAdapter(Context context, List<DesignerListInfo> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
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
        return convertView;
    }

    private static class ViewHolder {
        ImageView itemProductView;
        ImageView itemHeadView;
        TextView itemXiaoQuText;
        TextView itemProduceText;
    }

}
