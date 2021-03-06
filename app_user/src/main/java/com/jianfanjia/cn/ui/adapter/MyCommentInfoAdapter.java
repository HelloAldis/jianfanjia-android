package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.jianfanjia.api.model.Comment;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.DiaryImageDetailInfo;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.model.ProcessSectionItem;
import com.jianfanjia.api.model.SuperVisor;
import com.jianfanjia.api.model.User;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.DataManagerNew;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.business.ProcessBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.ui.interf.ViewPagerClickListener;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-07 17:46
 */
public class MyCommentInfoAdapter extends BaseLoadMoreRecycleAdapter<UserMessage> {

    public static final int PLAN_TYPE = 0;//方案的评论
    public static final int NODE_TYPE = 1;//节点的评论
    public static final int DIARY_TYPE = 2;//日记的评论

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
            if (mDatas.get(position).getMessage_type().equals(Constant.TYPE_DIARY_COMMENT_MSG)) {
                return DIARY_TYPE;
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
            case DIARY_TYPE:
                view = LayoutInflater.from(context).inflate(R.layout.list_item_commentinfo_type3, null);
                return new DiaryCommentViewHolder(view);
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
            case DIARY_TYPE:
                DiaryCommentViewHolder diaryCommentViewHolder = (DiaryCommentViewHolder) viewHolder;
                onBindDiaryCommentViewHolder(noticeInfo, diaryCommentViewHolder);
                break;
        }
    }

    private void onBindDiaryCommentViewHolder(final UserMessage noticeInfo, DiaryCommentViewHolder holder) {

        User user = noticeInfo.getUser();
        String imageid = null;
        if (user != null) {
            imageid = user.getImageid();
            String designerName = user.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(designerName) ? context.getString(R.string.ower) :
                    designerName);
        }

        Comment comment = noticeInfo.getTo_comment();
        if (comment != null) {
            holder.tvDiaryContentByComment.setVisibility(View.VISIBLE);
            MyCommentInfoAdapter.setCommentSecondTextViewShow(holder.tvDiaryContentByComment, comment.getContent());
        } else {
            holder.tvDiaryContentByComment.setVisibility(View.GONE);
        }

        //留言人的头像
        if (!TextUtils.isEmpty(imageid)) {
            LogTool.d("imageid=" + imageid);
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }

        //评论时间
        holder.dateText.setText(DateFormatTool.longToStringHasMini(noticeInfo.getCreate_at()));
        //评论内容
        DiaryBusiness.setCommentContentTextViewShow(holder.contentText, noticeInfo.getContent());

        DiaryInfo diaryInfo = noticeInfo.getDiary();
        final DiarySetInfo diarySetInfo = diaryInfo.getDiarySet();

        if (diarySetInfo != null) {
            holder.tvDiarySetTitle.setText(diarySetInfo.getTitle());
            holder.tvDiaryBaseInfo.setText(DiaryBusiness.getDiarySetDes(diarySetInfo));
        }
        holder.tvDiaryContent.setText(diaryInfo.getContent());
        setFirstDiaryPic(holder.ivDiaryFirstPic, diaryInfo.getImages());

        //回复
        holder.responseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCallback != null) {
                    onItemCallback.onResponse(noticeInfo, DIARY_TYPE);
                }
            }
        });

        holder.contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemCallback != null) {
                    onItemCallback.showDetail(noticeInfo, DIARY_TYPE);
                }
            }
        });

    }

    private void setFirstDiaryPic(ImageView ivDiaryFirstPic, List<DiaryImageDetailInfo> images) {
        if (images == null || images.size() == 0) {
            ivDiaryFirstPic.setVisibility(View.GONE);
        } else {
            ivDiaryFirstPic.setVisibility(View.VISIBLE);
            imageShow.displayHalfScreenWidthThumnailImage(context, images.get(0).getImageid(), ivDiaryFirstPic);
        }
    }


    private void onBindPlanCommentViewHolder(final UserMessage noticeInfo, PlanCommentViewHolder holder) {

        //设计师的名字
        Designer designer = noticeInfo.getDesigner();
        SuperVisor superVisor = noticeInfo.getSupervisor();
        String imageid = null;
        if (designer != null) {
            imageid = designer.getImageid();
            String designerName = designer.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(designerName) ? context.getString(R.string.designer) :
                    designerName);
        } else if (superVisor != null) {
            imageid = superVisor.getImageid();
            String superVisorName = superVisor.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(superVisorName) ? context.getString(R.string.supervisor) :
                    superVisorName);
        }

        //留言人的头像
        if (!TextUtils.isEmpty(imageid)) {
            LogTool.d("imageid=" + imageid);
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }
        holder.cellText.setText(noticeInfo.getRequirement().getBasic_address());

        holder.numText.setText(noticeInfo.getPlan().getName());
        //评论时间
        holder.dateText.setText(DateFormatTool.longToStringHasMini(noticeInfo.getCreate_at()));
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
        DesignerPlanRecyclerViewAdapter adapter = new DesignerPlanRecyclerViewAdapter(context, imgList, new
                ViewPagerClickListener() {
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

        Designer designer = noticeInfo.getDesigner();
        SuperVisor superVisor = noticeInfo.getSupervisor();
        String imageid = null;
        if (designer != null) {
            imageid = designer.getImageid();
            String designerName = designer.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(designerName) ? context.getString(R.string.designer) :
                    designerName);
        } else if (superVisor != null) {
            imageid = superVisor.getImageid();
            String superVisorName = superVisor.getUsername();
            holder.nameView.setText(TextUtils.isEmpty(superVisorName) ? context.getString(R.string.supervisor) :
                    superVisorName);
        }
        if (!TextUtils.isEmpty(imageid)) {
            LogTool.d("imageid=" + imageid);
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.itemHeadView);
        }

        //设计师的名字
        //评论时间
        holder.dateText.setText(DateFormatTool.longToStringHasMini(noticeInfo.getCreate_at()));
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

        holder.cellName.setText(noticeInfo.getProcess().getBasic_address());
        ProcessSectionItem processSectionItem = ProcessBusiness.getSectionItemByName(noticeInfo.getProcess(),
                noticeInfo.getSection(), noticeInfo.getItem());
        holder.nodeName.setText(processSectionItem.getLabel());
        switch (noticeInfo.getProcess().getSectionInfoByName(noticeInfo.getSection()).
                getSectionItemInfoByName(noticeInfo.getItem())
                .getStatus()) {
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
                    onItemCallback.showDetail(noticeInfo, NODE_TYPE);
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
        public RelativeLayout responseView;
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
        public RelativeLayout responseView;
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

    static class DiaryCommentViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.ltm_cominfo_head)
        public ImageView itemHeadView;
        @Bind(R.id.ltm_cominfo_designer_name)
        public TextView nameView;
        @Bind(R.id.ltm_cominfo_designer_date)
        public TextView dateText;
        @Bind(R.id.ltm_cominfo_response)
        public RelativeLayout responseView;
        @Bind(R.id.ltm_cominfo_content)
        public TextView contentText;
        @Bind(R.id.ltm_diaryset_title)
        TextView tvDiarySetTitle;
        @Bind(R.id.iv_diary_first_pic)
        ImageView ivDiaryFirstPic;
        @Bind(R.id.ltm_diary_baseinfo)
        TextView tvDiaryBaseInfo;
        @Bind(R.id.ltm_diary_content)
        TextView tvDiaryContent;

        @Bind(R.id.ltm_cominfo_content_bycomment)
        public TextView tvDiaryContentByComment;

        @Bind(R.id.item_layout)
        RelativeLayout contentLayout;

        public DiaryCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemCallback {
        void onResponse(UserMessage noticeInfo, int viewType);

        void showDetail(UserMessage noticeInfo, int viewType);
    }

    public static void setCommentSecondTextViewShow(TextView textView, String content) {
        if(TextUtils.isEmpty(content)) return;

        int start = content.indexOf("回复");
        if (start != -1) {
            String finalContent = DataManagerNew.getInstance().getUserName() + " " + content;
            int leftstart = 0;
            int leftend = finalContent.indexOf("回复");

            int rightStart = finalContent.indexOf("回复") + 2;
            int rightend = finalContent.indexOf("：");

            SpannableString spannableString = new SpannableString(finalContent);
            spannableString.setSpan(new ForegroundColorSpan(MyApplication.getInstance().getResources().getColor(R
                    .color.orange_color)), leftstart, leftend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(MyApplication.getInstance().getResources().getColor(R
                    .color.orange_color)), rightStart, rightend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        } else {
            String finalContent = DataManagerNew.getInstance().getUserName() + "：" + content;
            int leftstart = 0;
            int leftend = finalContent.indexOf("：");

            SpannableString spannableString = new SpannableString(finalContent);
            spannableString.setSpan(new ForegroundColorSpan(MyApplication.getInstance().getResources().getColor(R
                    .color.orange_color)), leftstart, leftend, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannableString);
        }
    }


}
