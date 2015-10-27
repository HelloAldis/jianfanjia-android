package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.tools.DateFormatTool;

import java.util.List;

/**
 * @param <NotifyCaiGouInfo>
 * @author zhanghao
 * @class DelayNotifyAdapter
 * @date 2015-8-26 15:57
 */
public class DelayNotifyAdapter extends BaseListAdapter<NotifyDelayInfo> {

    public DelayNotifyAdapter(Context context, List<NotifyDelayInfo> delayList) {
        super(context, delayList);
    }


    @Override
    public View initView(int position, View convertView) {
        ViewHolder viewHolder = null;
        NotifyDelayInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_tip_delay,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemCellView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_cell_name);
            viewHolder.itemNameView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_name);
            viewHolder.itemNodeView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_node);
            viewHolder.itemPubTimeView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_time);
            viewHolder.itemStatusView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        viewHolder.itemContentView.setText(DateFormatTool.longToString(info
//                .getNew_date()));
//		viewHolder.itemNodeView.setText(MyApplication.getInstance()
//				.getStringById(info.getSection()) + "阶段");
//        viewHolder.itemPubTimeView.setText(DateFormatTool
//                .toLocalTimeString(info.getRequest_date()));
//        viewHolder.itemAgreeText.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//            }
//
//        });
//        viewHolder.itemRefuseText.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//            }
//
//        });
        return convertView;
    }

    private static class ViewHolder {
        TextView itemCellView;
        TextView itemNameView;// 延迟标题
        TextView itemNodeView;// 延迟节点
        TextView itemPubTimeView;// 发布时间
        TextView itemStatusView;
    }


}