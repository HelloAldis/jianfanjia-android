package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.ProcessSection;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.business.ProcessBusiness;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.ui.interf.RecyclerItemCallBack;
import com.jianfanjia.common.tool.DateFormatTool;
import com.jianfanjia.common.tool.LogTool;

/**
 * Name: NoticeAdapter
 * User: fengliang
 * Date: 2016-03-07
 * Time: 09:25
 */
public class NoticeAdapter extends BaseLoadMoreRecycleAdapter<UserMessage> {
    private static final String TAG = NoticeAdapter.class.getName();
    private static final int ITEM_TYPE0 = 0;
    private static final int ITEM_TYPE1 = 1;
    private static final int ITEM_TYPE2 = 2;
    private static final int ITEM_TYPE3 = 3;
    private static final int ITEM_TYPE4 = 4;
    private static final int ITEM_TYPE5 = 5;
    private static final int ITEM_TYPE6 = 6;
    private static final int ITEM_TYPE7 = 7;
    private static final int ITEM_TYPE8 = 8;
    private static final int ITEM_TYPE9 = 9;
    private static final int ITEM_TYPE10 = 10;
    private static final int ITEM_TYPE11 = 11;
    private static final int ITEM_TYPE12 = 12;
    private static final int ITEM_TYPE13 = 13;
    private static final int ITEM_TYPE14 = 14;
    private static final int ITEM_TYPE15 = 15;
    private static final int ITEM_TYPE16 = 16;
    private static final int ITEM_TYPE17 = 17;
    private static final int ITEM_TYPE18 = 18;
    private static final int ITEM_TYPE19 = 19;
    private static final int ITEM_TYPE20 = 20;
    private static final int ITEM_TYPE21 = 21;
    private RecyclerItemCallBack callback;

    public NoticeAdapter(Context context, RecyclerView recyclerView, RecyclerItemCallBack callback) {
        super(context, recyclerView);
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getItemViewType(position) == TYPE_NORMAL_ITEM) {
            String msgType = mDatas.get(position).getMessage_type();
            LogTool.d(TAG, "msgType:" + msgType);
            if (msgType.equals(Constant.TYPE_DELAY_MSG)) {
                return  ITEM_TYPE0;
            } else if (msgType.equals(Constant.TYPE_CAIGOU_MSG)) {
                return  ITEM_TYPE1;
            } else if (msgType.equals(Constant.TYPE_SYSTEM_MSG)) {
                return  ITEM_TYPE2;
            } else if (msgType.equals(Constant.TYPE_PLAN_COMMENT_MSG)) {
                return  ITEM_TYPE3;
            } else if (msgType.equals(Constant.TYPE_SECTION_COMMENT_MSG)) {
                return  ITEM_TYPE4;
            } else if (msgType.equals(Constant.TYPE_AUTH_TYPE_AGRAEE)) {
                return  ITEM_TYPE5;
            } else if (msgType.equals(Constant.TYPE_AUTH_TYPE_DISGREE)) {
                return  ITEM_TYPE6;
            } else if (msgType.equals(Constant.TYPE_UID_TYPE_AGRAEE)) {
                return  ITEM_TYPE7;
            } else if (msgType.equals(Constant.TYPE_UID_TYPE_DISGRAEE)) {
                return  ITEM_TYPE8;
            } else if (msgType.equals(Constant.TYPE_PROCESS_AGRAEE)) {
                return  ITEM_TYPE9;
            } else if (msgType.equals(Constant.TYPE_PROCESS_DISGRAEE)) {
                return  ITEM_TYPE10;
            } else if (msgType.equals(Constant.TYPE_PRODUCT_AGRAEE)) {
                return  ITEM_TYPE11;
            } else if (msgType.equals(Constant.TYPE_PRODUCT_DISGRAEE)) {
                return  ITEM_TYPE12;
            } else if (msgType.equals(Constant.TYPE_PRODUCT_OFFLINE)) {
                return  ITEM_TYPE13;
            } else if (msgType.equals(Constant.TYPE_USER_APPOINT_MSG)) {
                return  ITEM_TYPE14;
            } else if (msgType.equals(Constant.TYPE_USER_CONFIRM_HOSER_MSG)) {
                return  ITEM_TYPE15;
            } else if (msgType.equals(Constant.TYPE_PLAN_CHOOSED_MSG)) {
                return  ITEM_TYPE16;
            } else if (msgType.equals(Constant.TYPE_PLAN_NOT_CHOOSED_MSG)) {
                return  ITEM_TYPE17;
            } else if (msgType.equals(Constant.TYPE_CONFIRM_CONTRACT_MSG)) {
                return  ITEM_TYPE18;
            } else if (msgType.equals(Constant.TYPE_REJECT_DELAY_MSG)) {
                return  ITEM_TYPE19;
            } else if (msgType.equals(Constant.TYPE_AGREE_DELAY_MSG)) {
                return  ITEM_TYPE20;
            } else if (msgType.equals(Constant.TYPE_CONFIRM_CHECK_MSG)) {
                return  ITEM_TYPE21;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ITEM_TYPE0:
            case ITEM_TYPE1:
            case ITEM_TYPE19:
            case ITEM_TYPE20:
            case ITEM_TYPE21:
                view = layoutInflater.inflate(R.layout.list_notice_item_site, null);
                return new SiteViewHolder(view);
            case ITEM_TYPE2:
            case ITEM_TYPE5:
            case ITEM_TYPE6:
            case ITEM_TYPE7:
            case ITEM_TYPE8:
            case ITEM_TYPE9:
            case ITEM_TYPE10:
            case ITEM_TYPE11:
            case ITEM_TYPE12:
            case ITEM_TYPE13:
                view = layoutInflater.inflate(R.layout.list_notice_item_sys, null);
                return new SysViewHolder(view);
            case ITEM_TYPE3:
            case ITEM_TYPE4:
            case ITEM_TYPE14:
            case ITEM_TYPE15:
            case ITEM_TYPE16:
            case ITEM_TYPE17:
            case ITEM_TYPE18:
                view = layoutInflater.inflate(R.layout.list_notice_item_req, null);
                return new ReqViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder,final int position) {
        final UserMessage info = mDatas.get(position);
        switch (getItemViewType(position)) {
            case ITEM_TYPE0:
            case ITEM_TYPE1:
            case ITEM_TYPE19:
            case ITEM_TYPE20:
            case ITEM_TYPE21:
                SiteViewHolder siteViewHolder = (SiteViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    siteViewHolder.itemTitle.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    siteViewHolder.itemCell.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    siteViewHolder.itemDate.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    siteViewHolder.itemSection.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    siteViewHolder.itemContent.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                } else {
                    siteViewHolder.itemTitle.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    siteViewHolder.itemCell.setTextColor(context.getResources().getColor(R.color.grey_color));
                    siteViewHolder.itemDate.setTextColor(context.getResources().getColor(R.color.grey_color));
                    siteViewHolder.itemSection.setTextColor(context.getResources().getColor(R.color.going_blue_color));
                    siteViewHolder.itemContent.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                siteViewHolder.itemTitle.setText(info.getTitle());
                siteViewHolder.itemDate.setText(DateFormatTool.getHumReadDateString(info.getCreate_at()));
                siteViewHolder.itemContent.setText(info.getContent());
                siteViewHolder.itemCell.setText(info.getProcess().getBasic_address());
                ProcessSection processSection = ProcessBusiness.getSectionInfoByName(info
                        .getProcess(), info.getSection());
                siteViewHolder.itemSection.setText(processSection.getLabel() + "阶段");
                siteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE2:
            case ITEM_TYPE5:
            case ITEM_TYPE6:
            case ITEM_TYPE7:
            case ITEM_TYPE8:
            case ITEM_TYPE9:
            case ITEM_TYPE10:
            case ITEM_TYPE11:
            case ITEM_TYPE12:
            case ITEM_TYPE13:
                SysViewHolder sysHolder = (SysViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    sysHolder.itemTitle.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    sysHolder.itemDate.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    sysHolder.itemContent.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                } else {
                    sysHolder.itemTitle.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    sysHolder.itemDate.setTextColor(context.getResources().getColor(R.color.grey_color));
                    sysHolder.itemContent.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                sysHolder.itemTitle.setText(info.getTitle());
                sysHolder.itemContent.setText(info.getContent());
                sysHolder.itemDate.setText(DateFormatTool.getHumReadDateString(info.getCreate_at()));
                sysHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE3:
            case ITEM_TYPE4:
            case ITEM_TYPE14:
            case ITEM_TYPE15:
            case ITEM_TYPE16:
            case ITEM_TYPE17:
            case ITEM_TYPE18:
                ReqViewHolder reqHolder = (ReqViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    reqHolder.itemTitleView.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    reqHolder.itemContentView.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    reqHolder.itemCellView.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                    reqHolder.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.color_grey_bg));
                } else {
                    reqHolder.itemTitleView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    reqHolder.itemContentView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    reqHolder.itemCellView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    reqHolder.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                reqHolder.itemTitleView.setText(info.getTitle());
                reqHolder.itemContentView.setText(info.getContent());
                reqHolder.itemCellView.setText(info.getRequirement().getBasic_address());
                reqHolder.itemPubTimeView.setText(DateFormatTool.getHumReadDateString(info.getCreate_at()));
                reqHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    static class SysViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_sys_tip_title)
        TextView itemTitle;
        @Bind(R.id.list_item_sys_tip_date)
        TextView itemDate;
        @Bind(R.id.list_item_sys_tip_content)
        TextView itemContent;

        public SysViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ReqViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_tip_req_title)
        TextView itemTitleView;
        @Bind(R.id.list_item_tip_req_cell)
        TextView itemCellView;
        @Bind(R.id.list_item_tip_req_content)
        TextView itemContentView;
        @Bind(R.id.list_item_tip_req_time)
        TextView itemPubTimeView;

        public ReqViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class SiteViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_tip_site_title)
        TextView itemTitle;
        @Bind(R.id.list_item_tip_site_time)
        TextView itemDate;
        @Bind(R.id.list_item_tip_site_cell)
        TextView itemCell;
        @Bind(R.id.list_item_tip_site_node)
        TextView itemSection;
        @Bind(R.id.list_item_tip_site_content)
        TextView itemContent;

        public SiteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

