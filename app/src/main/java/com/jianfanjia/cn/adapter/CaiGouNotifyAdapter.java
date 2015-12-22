package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.bean.NotifyMessage;
import com.jianfanjia.cn.tools.DateFormatTool;

import java.util.List;

/**
 * @param <NotifyCaiGouInfo>
 * @author zhanghao
 * @class CaiGouNotifyAdapter
 * @date 2015-8-26 15:57
 */
public class CaiGouNotifyAdapter extends BaseRecyclerViewAdapter<NotifyMessage> {

    public CaiGouNotifyAdapter(Context context, List<NotifyMessage> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<NotifyMessage> list) {
        NotifyMessage message = list.get(position);
        CaiGouNotifyViewHolder holder = (CaiGouNotifyViewHolder) viewHolder;
        holder.itemCellView.setText(message.getCell());
        holder.itemNameView.setText(context.getResources().getString(R.string.list_item_caigou_example));
        holder.itemContentView.setText(message.getContent());
        holder.itemNodeView.setText(MyApplication.getInstance()
                .getStringById(message.getSection()) + "阶段");
        holder.itemPubTimeView.setText(DateFormatTool
                .toLocalTimeString(message.getTime()));
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_tip_caigou,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new CaiGouNotifyViewHolder(view);
    }

    private static class CaiGouNotifyViewHolder extends RecyclerViewHolderBase {
        public TextView itemCellView;
        public TextView itemNameView;// 采购工序视图
        public TextView itemContentView;// 采购内容视图
        public TextView itemNodeView;// 采购节点
        public TextView itemPubTimeView;// 发布时间

        public CaiGouNotifyViewHolder(View itemView) {
            super(itemView);
            itemCellView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_cell_name);
            itemContentView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_caigou_content);
            itemNameView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_caigou_name);
            itemNodeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_caigou_node);
            itemPubTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_caigou_time);
        }
    }
}