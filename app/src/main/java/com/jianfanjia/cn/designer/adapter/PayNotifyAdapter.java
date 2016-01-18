package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.bean.NotifyMessage;
import com.jianfanjia.cn.designer.tools.DateFormatTool;

import java.util.List;

/**
 * @param <NotifypayInfo>
 * @author zhanghao
 * @class CaiGouNotifyAdapter.clase
 * @date 2015-8-26 15:57
 */
public class PayNotifyAdapter extends BaseRecyclerViewAdapter<NotifyMessage> {

    public PayNotifyAdapter(Context context, List<NotifyMessage> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<NotifyMessage> list) {
        NotifyMessage message = list.get(position);
        PayNotifyViewHolder holder = (PayNotifyViewHolder) viewHolder;
        holder.itemCellView.setText(message.getCell());
        holder.itemNameView.setText(context.getResources().getString(R.string.list_item_fukuan_example));
        holder.itemNodeView.setText(MyApplication.getInstance()
                .getStringById(message.getSection()) + "阶段");
        holder.itemPubTimeView.setText(DateFormatTool
                .toLocalTimeString(message.getTime()));
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_tip_pay,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new PayNotifyViewHolder(view);
    }

    private static class PayNotifyViewHolder extends RecyclerViewHolderBase {
        TextView itemCellView;
        TextView itemNameView;// 延迟工序
        TextView itemNodeView;// 延迟节点
        TextView itemPubTimeView;// 发布时间

        public PayNotifyViewHolder(View itemView) {
            super(itemView);
            itemCellView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_cell_name);
            itemNameView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_pay_name);
            itemNodeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_pay_node);
            itemPubTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_pay_time);
        }
    }
}