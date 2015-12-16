package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.bean.Process;
import com.jianfanjia.cn.designer.bean.User;
import com.jianfanjia.cn.designer.config.Constant;

import java.util.List;

/**
 * @author zhanghao
 * @class MyOwerInfoAdapter
 * @date 2015-8-26 15:57
 */
public class MyOwerInfoAdapter extends BaseListAdapter<Process> {

    public MyOwerInfoAdapter(Context context, List<Process> caigouList) {
        super(context, caigouList);
    }

    @Override
    public View initView(int position, View convertView) {
        ViewHolder viewHolder = null;
        Process info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.list_item_designer_ower, null);
            viewHolder = new ViewHolder();
            viewHolder.itemNameView = (TextView) convertView
                    .findViewById(R.id.list_item_site_owername);
            viewHolder.itemAdressView = (TextView) convertView
                    .findViewById(R.id.list_item_site_address);
            viewHolder.itemStageView = (TextView) convertView
                    .findViewById(R.id.list_item_site_currentpro);
            viewHolder.itemOwerHeadView = (ImageView) convertView
                    .findViewById(R.id.list_item_site_owerhead);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User user = info.getUser();
        viewHolder.itemNameView.setText(user.getUsername());
        viewHolder.itemAdressView.setText(info.getCell());
        viewHolder.itemStageView.setText(MyApplication.getInstance()
                .getStringById(info.getGoing_on()) + "阶段");
        String imageId = user.getImageid();
        if (imageId != null) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageId, viewHolder.itemOwerHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, viewHolder.itemOwerHeadView);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView itemNameView;// 业主名称
        TextView itemAdressView;// 业主工地地址
        TextView itemStageView;// 所处阶段
        ImageView itemOwerHeadView;// 业主头像
    }

}
