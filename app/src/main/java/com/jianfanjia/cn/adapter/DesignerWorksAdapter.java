package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.DesignerWorksInfo;

import java.util.List;

/**
 * Name: DesignerWorksAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 13:44
 */
public class DesignerWorksAdapter extends BaseListAdapter<DesignerWorksInfo> {

    public DesignerWorksAdapter(Context context, List<DesignerWorksInfo> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder viewHolder = null;
        DesignerWorksInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_designer_works,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemwWorksView = (ImageView) convertView
                    .findViewById(R.id.list_item_works_img);
            viewHolder.itemXiaoQuText = (TextView) convertView
                    .findViewById(R.id.list_item_works_xiaoqu_text);
            viewHolder.itemProduceText = (TextView) convertView
                    .findViewById(R.id.list_item_works_produce_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemwWorksView.setImageResource(R.mipmap.bg_home_banner2);
        viewHolder.itemXiaoQuText.setText(info.getXiaoquName());
        viewHolder.itemProduceText.setText(info.getProduce());
        return convertView;
    }

    private static class ViewHolder {
        ImageView itemwWorksView;
        TextView itemXiaoQuText;
        TextView itemProduceText;
    }
}
