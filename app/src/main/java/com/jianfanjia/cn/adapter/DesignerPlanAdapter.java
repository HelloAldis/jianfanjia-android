package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.PlandetailInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ItemClickListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.DateFormatTool;

import java.util.List;

/**
 * Name: DesignerPlanAdapter
 * User: fengliang
 * Date: 2015-10-22
 * Time: 17:54
 */
public class DesignerPlanAdapter extends BaseRecyclerViewAdapter<PlandetailInfo> {
    private ItemClickListener itemClickListener;

    public DesignerPlanAdapter(Context context, List<PlandetailInfo> list, ItemClickListener itemClickListener) {
        super(context, list);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<PlandetailInfo> list) {
        PlandetailInfo info = list.get(position);
        DesignerPlanViewHolder holder = (DesignerPlanViewHolder) viewHolder;
        holder.numText.setText(TextUtils.isEmpty(info.getName()) ? "" : info.getName());
        holder.dateText.setText(DateFormatTool.longToString(info.getLast_status_update_time()));
        holder.commentText.setText("留言(" + info.getComment_count() + ")");
        String status = info.getStatus();
        if (status.equals(Global.PLAN_STATUS3)) {
            holder.statusText.setTextColor(context.getResources().getColor(R.color.orange_color));
            holder.statusText.setText("沟通中");
        } else if (status.equals(Global.PLAN_STATUS4)) {
            holder.statusText.setTextColor(context.getResources().getColor(R.color.grey_color));
            holder.statusText.setText("未中标");
        } else if (status.equals(Global.PLAN_STATUS5)) {
            holder.statusText.setTextColor(context.getResources().getColor(R.color.orange_color));
            holder.statusText.setText("已中标");
        }
        List<String> imgList = info.getImages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.item_plan_listview.setLayoutManager(linearLayoutManager);
        DesignerPlanRecyclerViewAdapter adapter = new DesignerPlanRecyclerViewAdapter(context, imgList, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int pos) {
                if (null != itemClickListener) {
                    itemClickListener.onCallBack(position, pos);
                }
            }
        });
        holder.item_plan_listview.setAdapter(adapter);
        holder.commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onItemCallBack(position, Constant.PLAN_COMMENT_ITEM);
                }
            }
        });
        holder.previewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onItemCallBack(position, Constant.PLAN_PREVIEW_ITEM);
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_plan_info,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DesignerPlanViewHolder(view);
    }

    private static class DesignerPlanViewHolder extends RecyclerViewHolderBase {
        public TextView numText;
        public TextView statusText;
        public RecyclerView item_plan_listview;
        public TextView dateText;
        public TextView commentText;
        public TextView previewText;

        public DesignerPlanViewHolder(View itemView) {
            super(itemView);
            numText = (TextView) itemView.findViewById(R.id.numText);
            statusText = (TextView) itemView.findViewById(R.id.statusText);
            item_plan_listview = (RecyclerView) itemView.findViewById(R.id.item_plan_listview);
            dateText = (TextView) itemView.findViewById(R.id.dateText);
            previewText = (TextView) itemView.findViewById(R.id.previewText);
            commentText = (TextView) itemView.findViewById(R.id.commentText);
        }
    }
}
