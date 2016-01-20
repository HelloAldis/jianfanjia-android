package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.bean.Process;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.tools.StringUtils;

import java.util.List;

/**
 * Name: MySiteAdapter
 * User: fengliang
 * Date: 2016-01-18
 * Time: 14:21
 */
public class MySiteAdapter extends BaseRecyclerViewAdapter<Process> {

    public MySiteAdapter(Context context, List<Process> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Process> list) {
        Process process = list.get(position);
        MySiteViewHolder holder = (MySiteViewHolder) viewHolder;
        holder.itemCellView.setText(process.getCell());
        holder.itemPubTimeView.setText(StringUtils.covertLongToString(process.getLastupdate()));
        holder.itemUpdateTimeView.setText(StringUtils.covertLongToString(process.getLastupdate()));
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
        public TextView itemUpdateTimeView;
        public RelativeLayout contractLayout;
        public RelativeLayout planLayout;
        public RelativeLayout gotoLayout;


        public MySiteViewHolder(View itemView) {
            super(itemView);
            itemHeadView = (ImageView) itemView.findViewById(R.id.ltm_req_owner_head);
            itemCellView = (TextView) itemView.findViewById(R.id.ltm_req_cell);
            itemNodeView = (TextView) itemView.findViewById(R.id.ltm_req_status);
            item_process_listview = (RecyclerView) itemView.findViewById(R.id.item_site_section_listview);
            itemPubTimeView = (TextView) itemView.findViewById(R.id.ltm_req_starttime_cont);
            itemUpdateTimeView = (TextView) itemView.findViewById(R.id.ltm_req_updatetime_cont);
            contractLayout = (RelativeLayout) itemView.findViewById(R.id.contractLayout);
            planLayout = (RelativeLayout) itemView.findViewById(R.id.planLayout);
            gotoLayout = (RelativeLayout) itemView.findViewById(R.id.gotoLayout);
        }
    }
}
