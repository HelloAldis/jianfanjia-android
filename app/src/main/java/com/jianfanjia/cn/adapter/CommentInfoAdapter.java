package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.PlanInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ItemClickListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.DateFormatTool;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-07 17:46
 */
public class CommentInfoAdapter extends BaseRecycleAdapter<PlanInfo> {

    private static final int PLAN_TYPE = 0;//方案的评论
    private static final int NODE_TYPE = 1;//节点的评论

    private ItemClickListener itemClickListener;

    public CommentInfoAdapter(Context context, RecyclerView recyclerView, ItemClickListener itemClickListener) {
        super(context, recyclerView);

        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
         if(super.getItemViewType(position) == TYPE_NORMAL_ITEM){
             return mDatas.get(position).getComment_count();
         }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent,int viewType) {
        switch (viewType){
            case PLAN_TYPE:
                break;
            case NODE_TYPE:
                break;
        }
        return new DesignerPlanViewHolder(parent);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder,final int position) {
        PlanInfo info = mDatas.get(position);
        DesignerPlanViewHolder holder = (DesignerPlanViewHolder) viewHolder;
        holder.numText.setText("方案" + (position + 1));
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
