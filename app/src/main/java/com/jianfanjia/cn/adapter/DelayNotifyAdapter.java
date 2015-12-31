package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
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
public class DelayNotifyAdapter extends BaseRecyclerViewAdapter<NotifyDelayInfo> {
    private DelayInfoListener listener;

    public DelayNotifyAdapter(Context context, List<NotifyDelayInfo> list, DelayInfoListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<NotifyDelayInfo> list) {
        NotifyDelayInfo info = list.get(position);
        DelayNotifyViewHolder holder = (DelayNotifyViewHolder) viewHolder;
        final String role = info.getRequest_role();
        holder.itemCellView.setText(info.getProcess().getCell());
        holder.itemNodeView.setText(MyApplication.getInstance()
                .getStringById(info.getSection()) + "阶段");
        if (role.equals(Constant.IDENTITY_OWNER)) {
            holder.itemNewTimeView.setText("您已申请改期验收至" + DateFormatTool.longToString(info.getNew_date()));
        } else {
            holder.itemNewTimeView.setText("对方已申请改期验收至" + DateFormatTool.longToString(info.getNew_date()));
        }
        holder.itemPubTimeView.setText(DateFormatTool.toLocalTimeString(info.getRequest_date()));
        final String status = info.getStatus();
        if (status.equals(Constant.NO_START)) {
            holder.itemStatusView.setText("未开工");
        } else if (status.equals(Constant.DOING)) {
            holder.itemStatusView.setText("进行中");
        } else if (status.equals(Constant.FINISHED)) {
            holder.itemStatusView.setText("已完成");
        } else if (status.equals(Constant.YANQI_BE_DOING)) {
            if (role.equals(Constant.IDENTITY_OWNER)) {
                holder.itemStatusView.setText("等待对方确认");
            } else {
                holder.itemStatusView.setText("未处理,点击前往处理");
            }
        } else if (status.equals(Constant.YANQI_AGREE)) {
            holder.itemStatusView.setText("已同意");
        } else if (status.equals(Constant.YANQI_REFUSE)) {
            holder.itemStatusView.setText("已拒绝");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onClick(position, status, role);
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_tip_delay,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DelayNotifyViewHolder(view);
    }

    private static class DelayNotifyViewHolder extends RecyclerViewHolderBase {
        public TextView itemCellView;
        public TextView itemNodeView;// 延迟节点
        public TextView itemNewTimeView;// 延期时间
        public TextView itemPubTimeView;// 发布时间
        public TextView itemStatusView;

        public DelayNotifyViewHolder(View itemView) {
            super(itemView);
            itemCellView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_cell_name);
            itemNodeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_delay_node);
            itemNewTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_delay_new_time);
            itemPubTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_delay_time);
            itemStatusView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_delay_status);
        }
    }
}