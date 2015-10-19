package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.DesignerCaseInfo;

import java.util.List;

/**
 * Name: DesignerCaseAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 15:14
 */
public class DesignerCaseAdapter extends BaseListAdapter<DesignerCaseInfo> {

    public DesignerCaseAdapter(Context context, List<DesignerCaseInfo> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder viewHolder = null;
        DesignerCaseInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_designer_case,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemwCaseView = (ImageView) convertView
                    .findViewById(R.id.list_item_case_img);
            viewHolder.itemTitleText = (TextView) convertView
                    .findViewById(R.id.list_item_case_title_text);
            viewHolder.itemProduceText = (TextView) convertView
                    .findViewById(R.id.list_item_case_produce_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemwCaseView.setImageResource(R.mipmap.bg_home_banner2);
        viewHolder.itemTitleText.setText(info.getTitle());
        viewHolder.itemProduceText.setText(info.getProduceInfo());
        return convertView;
    }

    private static class ViewHolder {
        ImageView itemwCaseView;
        TextView itemTitleText;
        TextView itemProduceText;
    }
}
