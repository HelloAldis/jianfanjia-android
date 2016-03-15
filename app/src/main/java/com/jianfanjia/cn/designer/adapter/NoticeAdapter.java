package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.bean.NoticeInfo;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.interf.RecyclerItemCallBack;
import com.jianfanjia.cn.designer.tools.DateFormatTool;
import com.jianfanjia.cn.designer.tools.LogTool;

import java.util.List;

/**
 * Name: NoticeAdapter
 * User: fengliang
 * Date: 2016-03-07
 * Time: 09:25
 */
public class NoticeAdapter extends BaseRecyclerViewAdapter<NoticeInfo> {
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

    private RecyclerItemCallBack callback;
    private int viewType = -1;

    public NoticeAdapter(Context context, List<NoticeInfo> list, RecyclerItemCallBack callback) {
        super(context, list);
        this.callback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        NoticeInfo noticeInfo = list.get(position);
        String msgType = noticeInfo.getMessage_type();
        LogTool.d(TAG, "msgType:" + msgType);
        if (msgType.equals(Constant.TYPE_DELAY_MSG)) {
            viewType = ITEM_TYPE0;
        } else if (msgType.equals(Constant.TYPE_CAIGOU_MSG)) {
            viewType = ITEM_TYPE1;
        } else if (msgType.equals(Constant.TYPE_SYSTEM_MSG)) {
            viewType = ITEM_TYPE2;
        } else if (msgType.equals(Constant.TYPE_PLAN_COMMENT_MSG)) {
            viewType = ITEM_TYPE3;
        } else if (msgType.equals(Constant.TYPE_SECTION_COMMENT_MSG)) {
            viewType = ITEM_TYPE4;
        } else if (msgType.equals(Constant.TYPE_AUTH_TYPE_AGRAEE)) {
            viewType = ITEM_TYPE5;
        } else if (msgType.equals(Constant.TYPE_AUTH_TYPE_DISGREE)) {
            viewType = ITEM_TYPE6;
        } else if (msgType.equals(Constant.TYPE_UID_TYPE_AGRAEE)) {
            viewType = ITEM_TYPE7;
        } else if (msgType.equals(Constant.TYPE_UID_TYPE_DISGRAEE)) {
            viewType = ITEM_TYPE8;
        } else if (msgType.equals(Constant.TYPE_PROCESS_AGRAEE)) {
            viewType = ITEM_TYPE9;
        } else if (msgType.equals(Constant.TYPE_PROCESS_DISGRAEE)) {
            viewType = ITEM_TYPE10;
        } else if (msgType.equals(Constant.TYPE_PRODUCT_AGRAEE)) {
            viewType = ITEM_TYPE11;
        } else if (msgType.equals(Constant.TYPE_PRODUCT_DISGRAEE)) {
            viewType = ITEM_TYPE12;
        } else if (msgType.equals(Constant.TYPE_PRODUCT_OFFLINE)) {
            viewType = ITEM_TYPE13;
        } else if (msgType.equals(Constant.TYPE_USER_APPOINT_MSG)) {
            viewType = ITEM_TYPE14;
        } else if (msgType.equals(Constant.TYPE_USER_CONFIRM_HOSER_MSG)) {
            viewType = ITEM_TYPE15;
        } else if (msgType.equals(Constant.TYPE_PLAN_CHOOSED_MSG)) {
            viewType = ITEM_TYPE16;
        } else if (msgType.equals(Constant.TYPE_PLAN_NOT_CHOOSED_MSG)) {
            viewType = ITEM_TYPE17;
        } else if (msgType.equals(Constant.TYPE_CONFIRM_CONTRACT_MSG)) {
            viewType = ITEM_TYPE18;
        } else if (msgType.equals(Constant.TYPE_REJECT_DELAY_MSG)) {
            viewType = ITEM_TYPE19;
        } else if (msgType.equals(Constant.TYPE_AGREE_DELAY_MSG)) {
            viewType = ITEM_TYPE20;
        }
        return viewType;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<NoticeInfo> list) {
        final NoticeInfo info = list.get(position);
        switch (viewType) {
            case ITEM_TYPE0:
            case ITEM_TYPE1:
            case ITEM_TYPE19:
            case ITEM_TYPE20:
                SiteViewHolder holder1 = (SiteViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder1.itemTitle.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder1.itemCell.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder1.itemDate.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder1.itemSection.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder1.itemContent.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder1.itemTitle.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder1.itemCell.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder1.itemDate.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder1.itemSection.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder1.itemContent.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder1.itemTitle.setText(info.getTitle());
                holder1.itemDate.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder1.itemContent.setText(info.getContent());
                holder1.itemCell.setText(info.getProcess().getCell());
                holder1.itemSection.setText(MyApplication.getInstance()
                        .getStringById(info.getSection()) + "阶段");
                holder1.itemView.setOnClickListener(new View.OnClickListener() {
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
                SysViewHolder holder4 = (SysViewHolder) viewHolder;
                holder4.itemTitle.setText(info.getTitle());
                holder4.itemContent.setText(info.getContent());
                holder4.itemDate.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder4.itemView.setOnClickListener(new View.OnClickListener() {
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
                break;
            case ITEM_TYPE14:
            case ITEM_TYPE15:
            case ITEM_TYPE16:
            case ITEM_TYPE17:
            case ITEM_TYPE18:
                ReqViewHolder reqHolder = (ReqViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    reqHolder.itemTitleView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    reqHolder.itemContentView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    reqHolder.itemCellView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    reqHolder.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    reqHolder.itemTitleView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    reqHolder.itemContentView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    reqHolder.itemCellView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    reqHolder.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                reqHolder.itemTitleView.setText(info.getTitle());
                reqHolder.itemContentView.setText(info.getContent());
//                holder15.itemCellView.setText(info.getRequirement().getCell());
                reqHolder.itemPubTimeView.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
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

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ITEM_TYPE0:
            case ITEM_TYPE1:
            case ITEM_TYPE19:
            case ITEM_TYPE20:
                return layoutInflater.inflate(R.layout.list_notice_item_site, null);
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
                return layoutInflater.inflate(R.layout.list_notice_item_sys, null);
            case ITEM_TYPE3:
            case ITEM_TYPE4:
            case ITEM_TYPE14:
            case ITEM_TYPE15:
            case ITEM_TYPE16:
            case ITEM_TYPE17:
            case ITEM_TYPE18:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case ITEM_TYPE0:
            case ITEM_TYPE1:
            case ITEM_TYPE19:
            case ITEM_TYPE20:
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
                return new SysViewHolder(view);
            case ITEM_TYPE3:
            case ITEM_TYPE4:
            case ITEM_TYPE14:
            case ITEM_TYPE15:
            case ITEM_TYPE16:
            case ITEM_TYPE17:
            case ITEM_TYPE18:
                return new ReqViewHolder(view);
        }
        return null;
    }

    private static class SysViewHolder extends RecyclerViewHolderBase {
        public TextView itemTitle;
        public TextView itemDate;
        public TextView itemContent;

        public SysViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.list_item_sys_tip_title);
            itemDate = (TextView) itemView.findViewById(R.id.list_item_sys_tip_date);
            itemContent = (TextView) itemView.findViewById(R.id.list_item_sys_tip_content);
        }
    }

    private static class ReqViewHolder extends RecyclerViewHolderBase {
        public TextView itemTitleView;
        public TextView itemCellView;
        public TextView itemContentView;
        public TextView itemPubTimeView;

        public ReqViewHolder(View itemView) {
            super(itemView);
            itemTitleView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_req_title);
            itemContentView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_req_content);
            itemCellView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_req_cell);
            itemPubTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_req_time);
        }
    }

    private static class SiteViewHolder extends RecyclerViewHolderBase {
        public TextView itemTitle;
        public TextView itemDate;
        public TextView itemCell;
        public TextView itemSection;
        public TextView itemContent;

        public SiteViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.list_item_tip_site_title);
            itemDate = (TextView) itemView.findViewById(R.id.list_item_tip_site_time);
            itemCell = (TextView) itemView.findViewById(R.id.list_item_tip_site_cell);
            itemSection = (TextView) itemView.findViewById(R.id.list_item_tip_site_node);
            itemContent = (TextView) itemView.findViewById(R.id.list_item_tip_site_content);
        }
    }
}
