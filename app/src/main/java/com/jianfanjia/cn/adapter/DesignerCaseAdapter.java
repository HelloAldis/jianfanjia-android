package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.ImageInfo;
import com.jianfanjia.cn.config.Url_New;

import java.util.List;

/**
 * Name: DesignerCaseAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 15:14
 */
public class DesignerCaseAdapter extends BaseListAdapter<ImageInfo> {

    public DesignerCaseAdapter(Context context, List<ImageInfo> list) {
        super(context, list);
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder viewHolder = null;
        ImageInfo info = list.get(position);
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
        imageLoader.displayImage(Url_New.GET_IMAGE + info.getImageid(), viewHolder.itemwCaseView, options);
        viewHolder.itemTitleText.setText(info.getSection());
        viewHolder.itemProduceText.setText(info.getDescription());
        return convertView;
    }

    private static class ViewHolder {
        ImageView itemwCaseView;
        TextView itemTitleText;
        TextView itemProduceText;
    }
}
