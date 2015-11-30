package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseListAdapter;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.NotifyDelayInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.DelayInfoListener;
import com.jianfanjia.cn.tools.DateFormatTool;

import java.util.List;

/**
 * @param
 * @author zhanghao
 * @class DelayNotifyAdapter
 * @date 2015-8-26 15:57
 */
public class DelayNotifyAdapter extends BaseListAdapter<NotifyDelayInfo> {
    private DelayInfoListener listener;

    public DelayNotifyAdapter(Context context, List<NotifyDelayInfo> delayList) {
        super(context, delayList);
    }

    public DelayNotifyAdapter(Context context, List<NotifyDelayInfo> delayList,
                              DelayInfoListener listener) {
        super(context, delayList);
        this.listener = listener;
    }

    @Override
    public View initView(final int position, View convertView) {
        ViewHolder viewHolder = null;
        NotifyDelayInfo info = list.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_tip_delay,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.itemCellView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_cell_name);
            viewHolder.itemNodeView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_node);
            viewHolder.itemNewTimeView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_new_time);
            viewHolder.itemPubTimeView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_time);
            viewHolder.itemStatusView = (TextView) convertView
                    .findViewById(R.id.list_item_tip_delay_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.itemCellView.setText(info.getProcess().getCell());
        viewHolder.itemNodeView.setText(MyApplication.getInstance()
                .getStringById(info.getSection()) + "阶段");
        viewHolder.itemNewTimeView.setText("对方已申请改期验收至" + DateFormatTool.longToString(info.getNew_date()));
        viewHolder.itemPubTimeView.setText(DateFormatTool
                .toLocalTimeString(info.getRequest_date()));
        final String status = info.getStatus();
        if (status.equals(Constant.NO_START)) {
            viewHolder.itemStatusView.setText("未开工");
        } else if (status.equals(Constant.DOING)) {
            viewHolder.itemStatusView.setText("进行中");
        } else if (status.equals(Constant.FINISHED)) {
            viewHolder.itemStatusView.setText("已完成");
        } else if (status.equals(Constant.YANQI_BE_DOING)) {
            viewHolder.itemStatusView.setText("未处理,点击前往处理");
        } else if (status.equals(Constant.YANQI_AGREE)) {
            viewHolder.itemStatusView.setText("已同意");
        } else if (status.equals(Constant.YANQI_REFUSE)) {
            viewHolder.itemStatusView.setText("已拒绝");
        }
        viewHolder.itemStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(position, status);
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView itemCellView;
        TextView itemNodeView;// 延迟节点
        TextView itemNewTimeView;// 延期时间
        TextView itemPubTimeView;// 发布时间
        TextView itemStatusView;
    }


}