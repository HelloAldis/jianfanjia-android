package com.jianfanjia.cn.designer.adapter;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.ProcessSectionItem;
import com.jianfanjia.api.model.SuperVisor;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseRecycleAdapter;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.business.ProcessBusiness;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.interf.ViewPagerClickListener;
import com.jianfanjia.cn.designer.tools.StringUtils;
import com.jianfanjia.common.tool.LogTool;

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
        UserMessage userMessage = mDatas.get(position);
        switch (getItemViewType(position)) {
            case PLAN_TYPE:
                PlanCommentViewHolder planHolder = (PlanCommentViewHolder) viewHolder;
                onBindPlanCommentViewHolder(userMessage, planHolder);
                break;
            case NODE_TYPE:
                ProcessCommentViewHolder processHolder = (ProcessCommentViewHolder) viewHolder;
                onBindProcessCommentViewHolder(userMessage, processHolder);
                break;
        }
    }

    private void onBindPlanCommentViewHolder(final UserMessage userMessage, PlanCommentViewHolder holder) {

        User user = userMessage.getUser();
        SuperVisor superVisor = userMessage.getSupervisor();
        String imageid = null;
        if(user != null){
            imageid = user.getImageid();
            String username = user.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(username) ? context.getString(R.string.ower) : username);
        }else if(superVisor != null){
            imageid = superVisor.getImageid();
            String superVisorName = superVisor.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(superVisorName) ? context.getString(R.string.supervisor) : superVisorName);
        }

        //设计师的头像
        if (!TextUtils.isEmpty(imageid)) {
            LogTool.d(this.getClass().getName(), "imageid=" + imageid);
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }


        holder.cellText.setText(userMessage.getRequirement().getBasic_address());

        holder.numText.setText(userMessage.getPlan().getName());

        //评论时间
        holder.dateText.setText(StringUtils.covertLongToStringHasMini(userMessage.getCreate_at()));
        //评论内容
        holder.contentText.setText(userMessage.getContent());

        //方案状态
        String status = userMessage.getPlan().getStatus();
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
                    onItemCallback.onResponse(userMessage, PLAN_TYPE);
                }
            }
        });

        //方案图片
        List<String> imgList = userMessage.getPlan().getImages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.item_plan_listview.setLayoutManager(linearLayoutManager);
        DesignerPlanRecyclerViewAdapter adapter = new DesignerPlanRecyclerViewAdapter(context, imgList, new
                ViewPagerClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        if (onItemCallback != null) {
                            onItemCallback.showDetail(userMessage, PLAN_TYPE);
                        }
                    }
                });
        holder.item_plan_listview.setAdapter(adapter);

        holder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCallback != null) {
                    onItemCallback.showDetail(userMessage, PLAN_TYPE);
                }
            }
        });

    }

    private void onBindProcessCommentViewHolder(final UserMessage userMessage, ProcessCommentViewHolder holder) {

        User user = userMessage.getUser();
        SuperVisor superVisor = userMessage.getSupervisor();
        String imageid = null;
        if(user != null){
            imageid = user.getImageid();
            String username = user.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(username) ? context.getString(R.string.ower) : username);
        }else if(superVisor != null){
            imageid = superVisor.getImageid();
            String superVisorName = superVisor.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(superVisorName) ? context.getString(R.string.supervisor) : superVisorName);
        }

        if (!TextUtils.isEmpty(imageid)) {
            LogTool.d(this.getClass().getName(), "imageid=" + imageid);
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }

        //评论时间
        holder.dateText.setText(StringUtils.covertLongToStringHasMini(userMessage.getCreate_at()));
        //评论内容
        holder.contentText.setText(userMessage.getContent());

        //回复
        holder.responseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCallback != null) {
                    onItemCallback.onResponse(userMessage, NODE_TYPE);
                }
            }
        });

        holder.cellName.setText(userMessage.getProcess().getBasic_address());
        ProcessSectionItem processSectionItem = ProcessBusiness.getSectionItemByName(userMessage.getProcess(),
                userMessage.getSection(), userMessage.getItem());
        holder.nodeName.setText(processSectionItem.getLabel());
        switch (processSectionItem.getStatus()) {
            case Constant.FINISHED:
                holder.itemStatus
                        .setImageResource(R.mipmap.icon_home_finish);
                holder.nodeStatus.setTextColor(context.getResources().getColor(R.color.orange_color));
                holder.nodeStatus.setText(context.getResources()
                        .getString(R.string.site_example_node_finish));
                holder.itemBackground
                        .setBackgroundResource(R.mipmap.list_item_text_bg2);
                break;
            case Constant.NO_START:
                holder.itemStatus
                        .setImageResource(R.drawable.site_listview_item_notstart_circle);
                holder.nodeStatus.setTextColor(context.getResources().getColor(R.color.middle_grey_color));
                holder.nodeStatus.setText(context.getResources()
                        .getString(R.string.site_example_node_not_start));
                holder.itemBackground
                        .setBackgroundResource(R.mipmap.list_item_text_bg1);
                break;
            case Constant.DOING:
                holder.itemStatus
                        .setImageResource(R.mipmap.icon_home_working);
                holder.nodeStatus.setTextColor(context.getResources().getColor(R.color.blue_color));
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
                    onItemCallback.showDetail(userMessage, NODE_TYPE);
                }
            }
        });
    }

    static class PlanCommentViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.content_layout)
        public LinearLayout contentLayout;
        @Bind(R.id.ltm_cominfo_head)
        public ImageView itemHeadView;
        @Bind(R.id.ltm_cominfo_designer_name)
        public TextView nameView;
        @Bind(R.id.ltm_cominfo_response)
        public TextView responseView;
        @Bind(R.id.ltm_cominfo_designer_date)
        public TextView dateText;
        @Bind(R.id.ltm_cominfo_content)
        public TextView contentText;
        @Bind(R.id.cell_name)
        public TextView cellText;
        @Bind(R.id.numText)
        public TextView numText;
        @Bind(R.id.statusText)
        public TextView statusText;
        @Bind(R.id.item_plan_listview)
        public RecyclerView item_plan_listview;

        public PlanCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    static class ProcessCommentViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.ltm_cominfo_head)
        public ImageView itemHeadView;
        @Bind(R.id.ltm_cominfo_designer_name)
        public TextView nameView;
        @Bind(R.id.ltm_cominfo_designer_date)
        public TextView dateText;
        @Bind(R.id.ltm_cominfo_response)
        public TextView responseView;
        @Bind(R.id.ltm_cominfo_content)
        public TextView contentText;
        @Bind(R.id.node_name)
        public TextView nodeName;
        @Bind(R.id.node_status)
        public TextView nodeStatus;
        @Bind(R.id.cell_name)
        public TextView cellName;
        @Bind(R.id.item_status)
        public ImageView itemStatus;
        @Bind(R.id.node_layout)
        public RelativeLayout itemBackground;
        @Bind(R.id.item_layout)
        public RelativeLayout itemLayout;

        public ProcessCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemCallback {
        void onResponse(UserMessage userMessage, int viewType);

        void showDetail(UserMessage userMessage, int viewType);
    }


}
