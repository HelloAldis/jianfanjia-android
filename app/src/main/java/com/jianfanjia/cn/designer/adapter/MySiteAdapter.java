package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.bean.MySiteInfo;

import java.util.List;

/**
 * Name: MySiteAdapter
 * User: fengliang
 * Date: 2016-01-18
 * Time: 14:21
 */
public class MySiteAdapter extends BaseRecyclerViewAdapter<MySiteInfo> {

    public MySiteAdapter(Context context, List<MySiteInfo> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<MySiteInfo> list) {
        MySiteInfo info = list.get(position);
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_site_info,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new MySiteViewHolder(view);
    }

    private static class MySiteViewHolder extends RecyclerViewHolderBase {
        public ImageView itemHeadView;
        public TextView itemCellView;
        public TextView itemNodeView;
        public RecyclerView item_process_listview;
        public TextView itemPubTimeView;
        public TextView itemContractView;
        public TextView itemPlanView;
        public TextView ltm_req_gotopro;


        public MySiteViewHolder(View itemView) {
            super(itemView);
            itemHeadView = (ImageView) itemView.findViewById(R.id.list_item_head_img);
            itemCellView = (TextView) itemView.findViewById(R.id.cellText);
            itemNodeView = (TextView) itemView.findViewById(R.id.nodeText);
            item_process_listview = (RecyclerView) itemView.findViewById(R.id.item_site_section_listview);
            itemPubTimeView = (TextView) itemView.findViewById(R.id.dateText);
            itemContractView = (TextView) itemView.findViewById(R.id.contractText);
            itemPlanView = (TextView) itemView.findViewById(R.id.planText);
            ltm_req_gotopro = (TextView) itemView.findViewById(R.id.ltm_req_gotopro);
        }
    }
}
