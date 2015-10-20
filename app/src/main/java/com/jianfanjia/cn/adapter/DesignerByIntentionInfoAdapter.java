package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DesignerByIntentionInfo;

import java.util.List;

/**
 * Name: DesignerByIntentionInfoAdapter
 * User: fengliang
 * Date: 2015-10-19
 * Time: 14:54
 */
public class DesignerByIntentionInfoAdapter extends BaseListAdapter<DesignerByIntentionInfo> {

    public DesignerByIntentionInfoAdapter(Context context, List<DesignerByIntentionInfo> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder viewHolder = null;
        DesignerByIntentionInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_designer_by_intention_info,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemwHeadView = (ImageView) convertView
                    .findViewById(R.id.list_item_head_img);
            viewHolder.itemNameText = (TextView) convertView
                    .findViewById(R.id.list_item_name_text);
            viewHolder.itemLevelText = (TextView) convertView
                    .findViewById(R.id.list_item_level_text);
            viewHolder.itemCheck = (CheckBox) convertView
                    .findViewById(R.id.list_item_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemwHeadView.setImageResource(R.mipmap.bg_home_banner2);
        viewHolder.itemNameText.setText(info.getName());
        viewHolder.itemLevelText.setText(info.getStarLevel());

        return convertView;
    }

    private static class ViewHolder {
        ImageView itemwHeadView;
        TextView itemNameText;
        TextView itemLevelText;
        CheckBox itemCheck;
    }
}
