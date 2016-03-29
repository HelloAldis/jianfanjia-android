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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.cn.tools.StringUtils;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-07 17:46
 */
public class MyCommentInfoAdapter extends BaseRecycleAdapter<UserMessage> {

    public static final int PLAN_TYPE = 0;//方案的评论
    public static final int NODE_TYPE = 1;//节点的评论

    private OnItemCallback onItemCallback;

    public MyCommentInfoAdapter(Context context, RecyclerView recyclerView, OnItemCallback onItemCallback) {
        super(context, recyclerView);

        this.onItemCallback = onItemCallback;
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getItemViewType(position) == TYPE_NORMAL_ITEM) {
            if (mDatas.get(position).getMessage_type().equals(Constant.TYPE_PLAN_COMMENT_MSG)) {
                return PLAN_TYPE;
            }
            if (mDatas.get(position).getMessage_type().equals(Constant.TYPE_SECTION_COMMENT_MSG)) {
                return NODE_TYPE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case PLAN_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.list_item_commentinfo_type1, null);
                return new PlanCommentViewHolder(view);
            case NODE_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.list_item_commentinfo_type2, null);
                return new ProcessCommentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        UserMessage noticeInfo = mDatas.get(position);
        switch (getItemViewType(position)) {
            case PLAN_TYPE:
                PlanCommentViewHolder planHolder = (PlanCommentViewHolder) viewHolder;
                onBindPlanCommentViewHolder(noticeInfo, planHolder);
                break;
            case NODE_TYPE:
                ProcessCommentViewHolder processHolder = (ProcessCommentViewHolder) viewHolder;
                onBindProcessCommentViewHolder(noticeInfo, processHolder);
                break;
        }
    }

    private void onBindPlanCommentViewHolder(final UserMessage noticeInfo, PlanCommentViewHolder holder) {

        //设计师的名字
        holder.nameView.setText(noticeInfo.getDesigner().getUsername());

        holder.cellText.setText(noticeInfo.getRequirement().getCell());

        holder.numText.setText(noticeInfo.getPlan().getName());
        //设计师的头像
        String imageid = noticeInfo.getDesigner().getImageid();
        LogTool.d(this.getClass().getName(), "imageid=" + imageid);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }

        //评论时间
        holder.dateText.setText(StringUtils.covertLongToStringHasMini(noticeInfo.getCreate_at()));
        //评论内容
        holder.contentText.setText(noticeInfo.getContent());

        //方案状态
        String status = noticeInfo.getPlan().getStatus();
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
                if (onItemCallback != null) {
                    onItemCallback.onResponse(noticeInfo, PLAN_TYPE);
                }
            }
        });

        //方案图片
        List<String> imgList = noticeInfo.getPlan().getImages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.item_plan_listview.setLayoutManager(linearLayoutManager);
        DesignerPlanRecyclerViewAdapter adapter = new DesignerPlanRecyclerViewAdapter(context, imgList, new ViewPagerClickListener() {
            @Override
            public void onClickItem(int pos) {
                if (onItemCallback != null) {
                    onItemCallback.showDetail(noticeInfo, PLAN_TYPE);
                }
            }
        });
        holder.item_plan_listview.setAdapter(adapter);

        holder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCallback != null) {
                    onItemCallback.showDetail(noticeInfo, PLAN_TYPE);
                }
            }
        });

    }

    private void onBindProcessCommentViewHolder(final UserMessage noticeInfo, ProcessCommentViewHolder holder) {
        String imageid = noticeInfo.getDesigner().getImageid();
        LogTool.d(this.getClass().getName(), "imageid=" + imageid);
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }

        //设计师的名字
        holder.nameView.setText(noticeInfo.getDesigner().getUsername());
        //评论时间
        holder.dateText.setText(StringUtils.covertLongToStringHasMini(noticeInfo.getCreate_at()));
        //评论内容
        holder.contentText.setText(noticeInfo.getContent());

        //回复
        holder.responseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCallback != null) {
                    onItemCallback.onResponse(noticeInfo, NODE_TYPE);
                }
            }
        });

        holder.cellName.setText(noticeInfo.getProcess().getCell());
        holder.nodeName.setText(MyApplication.getInstance()
                .getStringById(noticeInfo.getItem()));
        switch (noticeInfo.getProcess().getSectionInfoByName(noticeInfo.getSection()).
                getSectionItemInfoByName(noticeInfo.getItem())
                .getStatus()) {
            case Constant.FINISHED:
                holder.itemStatus
                        .setImageResource(R.mipmap.icon_home_finish);
                holder.nodeStatus.setText(context.getResources()
                        .getString(R.string.site_example_node_finish));
                holder.itemBackground
                        .setBackgroundResource(R.mipmap.list_item_text_bg2);
                break;
            case Constant.NO_START:

                holder.itemStatus
                        .setImageResource(R.drawable.site_listview_item_notstart_circle);
                holder.nodeStatus.setText(context.getResources()
                        .getString(R.string.site_example_node_not_start));
                holder.itemBackground
                        .setBackgroundResource(R.mipmap.list_item_text_bg1);
                break;
            case Constant.DOING:

                holder.itemStatus
                        .setImageResource(R.mipmap.icon_home_working);
                holder.nodeStatus.setText(context.getResources()
                        .getString(R.string.site_example_node_working));
                holder.itemBackground
                        .setBackgroundResource(R.mipmap.list_item_text_bg2);
                break;
            default:
                break;
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCallback != null) {
                    onItemCallback.showDetail(noticeInfo, NODE_TYPE);
                }
            }
        });
    }

    private static class PlanCommentViewHolder extends RecyclerViewHolderBase {

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

        public PlanCommentViewHolder(View itemView) {
            super(itemView);
            this.itemHeadView = (ImageView) itemView.findViewById(R.id.ltm_cominfo_head);
            this.nameView = (TextView) itemView.findViewById(R.id.ltm_cominfo_designer_name);
            this.dateText = (TextView) itemView.findViewById(R.id.ltm_cominfo_designer_date);
            this.responseView = (TextView) itemView.findViewById(R.id.ltm_cominfo_response);
            this.contentText = (TextView) itemView.findViewById(R.id.ltm_cominfo_content);
            this.cellText = (TextView) itemView.findViewById(R.id.cell_name);
            this.numText = (TextView) itemView.findViewById(R.id.numText);
            this.statusText = (TextView) itemView.findViewById(R.id.statusText);
            this.item_plan_listview = (RecyclerView) itemView.findViewById(R.id.item_plan_listview);
            this.contentLayout = (LinearLayout) itemView.findViewById(R.id.content_layout);
        }

    }

    private static class ProcessCommentViewHolder extends RecyclerViewHolderBase {

        public ImageView itemHeadView;
        public TextView nameView;
        public TextView dateText;
        public TextView responseView;
        public TextView contentText;
        public TextView nodeName;
        public TextView nodeStatus;
        public TextView cellName;
        public ImageView itemStatus;
        public RelativeLayout itemBackground;
        public RelativeLayout itemLayout;

        public ProcessCommentViewHolder(View itemView) {
            super(itemView);
            this.itemHeadView = (ImageView) itemView.findViewById(R.id.ltm_cominfo_head);
            this.nameView = (TextView) itemView.findViewById(R.id.ltm_cominfo_designer_name);
            this.dateText = (TextView) itemView.findViewById(R.id.ltm_cominfo_designer_date);
            this.responseView = (TextView) itemView.findViewById(R.id.ltm_cominfo_response);
            this.contentText = (TextView) itemView.findViewById(R.id.ltm_cominfo_content);
            this.cellName = (TextView) itemView.findViewById(R.id.cell_name);
            this.nodeStatus = (TextView) itemView.findViewById(R.id.node_status);
            this.nodeName = (TextView) itemView.findViewById(R.id.node_name);
            this.itemBackground = (RelativeLayout) itemView.findViewById(R.id.node_layout);
            this.itemStatus = (ImageView) itemView.findViewById(R.id.item_status);
            this.itemLayout = (RelativeLayout) itemView.findViewById(R.id.item_layout);

        }
    }

    public interface OnItemCallback {
        void onResponse(UserMessage noticeInfo, int viewType);

        void showDetail(UserMessage noticeInfo, int viewType);
    }


}
