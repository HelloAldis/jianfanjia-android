package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.MyCommentList;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.LogTool;

import java.util.List;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-07 17:46
 */
public class MyCommentInfoAdapter extends BaseRecycleAdapter<MyCommentList.MyCommentInfo> {

    public static final int PLAN_TYPE = 0;//方案的评论
    public static final int NODE_TYPE = 1;//节点的评论

    private OnItemCallback onItemCallback;

    public MyCommentInfoAdapter(Context context, RecyclerView recyclerView, OnItemCallback onItemCallback) {
        super(context, recyclerView);

        this.onItemCallback = onItemCallback;
    }

    @Override
    public int getItemViewType(int position) {
         if(super.getItemViewType(position) == TYPE_NORMAL_ITEM){
             return Integer.parseInt(mDatas.get(position).getTopictype());
         }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent,int viewType) {
        View view = null;
        switch (viewType){
            case PLAN_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.list_item_commentinfo_type1,null);
                return new PlanCommentViewHolder(view);
            case NODE_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.list_item_commentinfo_type2,null);
                return new ProcessCommentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder,final int position) {
        MyCommentList.MyCommentInfo myCommentInfo = mDatas.get(position);
        switch (getItemViewType(position)){
            case PLAN_TYPE:
                PlanCommentViewHolder planHolder = (PlanCommentViewHolder)viewHolder;
                onBindPlanCommentViewHolder(myCommentInfo,planHolder);
                break;
            case NODE_TYPE:
                ProcessCommentViewHolder processHolder = (ProcessCommentViewHolder)viewHolder;
                onBindProcessCommentViewHolder(myCommentInfo,processHolder);
                break;
        }
    }

    private void onBindPlanCommentViewHolder(final MyCommentList.MyCommentInfo myCommentInfo,PlanCommentViewHolder holder){

        //设计师的名字
        holder.nameView.setText(myCommentInfo.getByUser().getUsername());

        //设计师的头像
        String imageid = myCommentInfo.getByUser().getImageid();
        LogTool.d(this.getClass().getName(), "imageid=" + imageid);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }

        //评论时间
        holder.dateText.setText(DateFormatTool.longToString(myCommentInfo.getCreate_at()));
        //评论内容
        holder.contentText.setText(myCommentInfo.getContent());

        //方案状态
        String status = myCommentInfo.getStatus();
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

        //回复
        holder.responseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemCallback != null){
                    onItemCallback.onResponse(myCommentInfo,PLAN_TYPE);
                }
            }
        });

        //方案图片
        List<String> imgList = myCommentInfo.getPlanInfo().getImages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.item_plan_listview.setLayoutManager(linearLayoutManager);
        DesignerPlanRecyclerViewAdapter adapter = new DesignerPlanRecyclerViewAdapter(context, imgList,null);
        holder.item_plan_listview.setAdapter(adapter);

        holder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemCallback != null){
                    onItemCallback.showDetail(myCommentInfo,PLAN_TYPE);
                }
            }
        });

    }

    private void onBindProcessCommentViewHolder(MyCommentList.MyCommentInfo myCommentInfo,ProcessCommentViewHolder holder){
        String imageid = myCommentInfo.getByUser().getImageid();
        LogTool.d(this.getClass().getName(), "imageid=" + imageid);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }
    }

    private static class PlanCommentViewHolder extends RecyclerViewHolderBase{

        public LinearLayout contentLayout;
        public ImageView itemHeadView;
        public TextView nameView;
        public TextView responseView;
        public TextView dateText;
        public TextView contentText;
        public TextView cellText;
        public TextView numText;
        public TextView statusText;
        public RecyclerView item_plan_listview;

        public PlanCommentViewHolder(View itemView){
            super(itemView);
            this.itemHeadView = (ImageView)itemView.findViewById(R.id.ltm_cominfo_head);
            this.nameView = (TextView)itemView.findViewById(R.id.ltm_cominfo_designer_name);
            this.dateText = (TextView)itemView.findViewById(R.id.ltm_cominfo_designer_date);
            this.responseView = (TextView)itemView.findViewById(R.id.ltm_cominfo_response);
            this.contentText = (TextView)itemView.findViewById(R.id.ltm_cominfo_content);
            this.cellText = (TextView)itemView.findViewById(R.id.cell_name);
            this.numText = (TextView)itemView.findViewById(R.id.numText);
            this.statusText = (TextView)itemView.findViewById(R.id.statusText);
            this.item_plan_listview = (RecyclerView)itemView.findViewById(R.id.item_plan_listview);
            this.contentLayout = (LinearLayout)itemView.findViewById(R.id.content_layout);
        }

    }

    private static class ProcessCommentViewHolder extends RecyclerViewHolderBase{

        public ImageView itemHeadView;
        public TextView nameView;
        public TextView dateText;
        public TextView responseView;
        public TextView contentText;

        public ProcessCommentViewHolder(View itemView){
            super(itemView);
            this.itemHeadView = (ImageView)itemView.findViewById(R.id.ltm_cominfo_head);
            this.nameView = (TextView)itemView.findViewById(R.id.ltm_cominfo_designer_name);
            this.dateText = (TextView)itemView.findViewById(R.id.ltm_cominfo_designer_date);
            this.responseView = (TextView)itemView.findViewById(R.id.ltm_cominfo_response);
            this.contentText = (TextView)itemView.findViewById(R.id.ltm_cominfo_content);

        }
    }

    public interface OnItemCallback{
        void onResponse(MyCommentList.MyCommentInfo myCommentInfo,int viewType);
        void showDetail(MyCommentList.MyCommentInfo myCommentInfo,int viewType);
    }


}
