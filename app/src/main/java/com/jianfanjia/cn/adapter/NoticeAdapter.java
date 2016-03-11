package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.application.MyApplication;
import com.jianfanjia.cn.bean.NoticeInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.RecyclerItemCallBack;
import com.jianfanjia.cn.tools.DateFormatTool;
import com.jianfanjia.cn.tools.LogTool;

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
        } else if (msgType.equals(Constant.TYPE_PAY_MSG)) {
            viewType = ITEM_TYPE2;
        } else if (msgType.equals(Constant.TYPE_CONFIRM_CHECK_MSG)) {
            viewType = ITEM_TYPE3;
        } else if (msgType.equals(Constant.TYPE_SYSTEM_MSG)) {
            viewType = ITEM_TYPE4;
        } else if (msgType.equals(Constant.TYPE_PLAN_COMMENT_MSG)) {
            viewType = ITEM_TYPE5;
        } else if (msgType.equals(Constant.TYPE_SECTION_COMMENT_MSG)) {
            viewType = ITEM_TYPE6;
        } else if (msgType.equals(Constant.TYPE_DESIGNER_RESPONSE_MSG)) {
            viewType = ITEM_TYPE7;
        } else if (msgType.equals(Constant.TYPE_DESIGNER_REJECT_MSG)) {
            viewType = ITEM_TYPE8;
        } else if (msgType.equals(Constant.TYPE_DESIGNER_UPLOAD_PLAN_MSG)) {
            viewType = ITEM_TYPE9;
        } else if (msgType.equals(Constant.TYPE_DESIGNER_CONFIG_CONTRACT_MSG)) {
            viewType = ITEM_TYPE10;
        } else if (msgType.equals(Constant.TYPE_DESIGNER_REJECT_DELAY_MSG)) {
            viewType = ITEM_TYPE11;
        } else if (msgType.equals(Constant.TYPE_DESIGNER_AGREE_DELAY_MSG)) {
            viewType = ITEM_TYPE12;
        }
        return viewType;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<NoticeInfo> list) {
        final NoticeInfo info = list.get(position);
        switch (viewType) {
            case ITEM_TYPE0:
                SiteViewHolder holder0 = (SiteViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder0.itemTitle.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder0.itemCell.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder0.itemDate.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder0.itemSection.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder0.itemContent.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder0.itemTitle.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder0.itemCell.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder0.itemDate.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder0.itemSection.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder0.itemContent.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder0.itemTitle.setText(info.getTitle());
                holder0.itemDate.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder0.itemCell.setText(info.getProcess().getCell());
                holder0.itemContent.setText(info.getContent());
                holder0.itemSection.setText(MyApplication.getInstance()
                        .getStringById(info.getSection()) + "阶段");
                holder0.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE1:
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
                SiteViewHolder holder2 = (SiteViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder2.itemTitle.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder2.itemCell.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder2.itemDate.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder2.itemSection.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder2.itemContent.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder2.itemTitle.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder2.itemCell.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder2.itemDate.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder2.itemSection.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder2.itemContent.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder2.itemTitle.setText(info.getTitle());
                holder2.itemDate.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder2.itemCell.setText(info.getProcess().getCell());
                holder2.itemContent.setText(info.getContent());
                holder2.itemSection.setText(MyApplication.getInstance()
                        .getStringById(info.getSection()) + "阶段");
                holder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE3:
                SiteViewHolder holder3 = (SiteViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder3.itemTitle.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder3.itemCell.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder3.itemDate.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder3.itemSection.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder3.itemContent.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder3.itemTitle.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder3.itemCell.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder3.itemDate.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder3.itemSection.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder3.itemContent.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder3.itemTitle.setText(info.getTitle());
                holder3.itemDate.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder3.itemCell.setText(info.getProcess().getCell());
                holder3.itemContent.setText(info.getContent());
                holder3.itemSection.setText(MyApplication.getInstance().getStringById(info.getSection()) + "阶段");
                holder3.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE4:
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
            case ITEM_TYPE5:
                break;
            case ITEM_TYPE6:
                break;
            case ITEM_TYPE7:
                ReqViewHolder holder7 = (ReqViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder7.itemTitleView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder7.itemContentView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder7.itemCellView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder7.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder7.itemTitleView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder7.itemContentView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder7.itemCellView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder7.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder7.itemTitleView.setText(info.getTitle());
                holder7.itemContentView.setText(info.getContent());
                holder7.itemCellView.setText(info.getRequirement().getCell());
                holder7.itemPubTimeView.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder7.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE8:
                ReqViewHolder holder8 = (ReqViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder8.itemTitleView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder8.itemContentView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder8.itemCellView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder8.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder8.itemTitleView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder8.itemContentView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder8.itemCellView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder8.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder8.itemTitleView.setText(info.getTitle());
                holder8.itemContentView.setText(info.getContent());
                holder8.itemCellView.setText(info.getRequirement().getCell());
                holder8.itemPubTimeView.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder8.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE9:
                ReqViewHolder holder9 = (ReqViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder9.itemTitleView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder9.itemContentView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder9.itemCellView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder9.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder9.itemTitleView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder9.itemContentView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder9.itemCellView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder9.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder9.itemTitleView.setText(info.getTitle());
                holder9.itemContentView.setText(info.getContent());
                holder9.itemCellView.setText(info.getRequirement().getCell());
                holder9.itemPubTimeView.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder9.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE10:
                ReqViewHolder holder10 = (ReqViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder10.itemTitleView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder10.itemContentView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder10.itemCellView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder10.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder10.itemTitleView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder10.itemContentView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder10.itemCellView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder10.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder10.itemTitleView.setText(info.getTitle());
                holder10.itemContentView.setText(info.getContent());
                holder10.itemCellView.setText(info.getRequirement().getCell());
                holder10.itemPubTimeView.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder10.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE11:
                ReqViewHolder holder11 = (ReqViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder11.itemTitleView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder11.itemContentView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder11.itemCellView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder11.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder11.itemTitleView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder11.itemContentView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder11.itemCellView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder11.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder11.itemTitleView.setText(info.getTitle());
                holder11.itemContentView.setText(info.getContent());
                holder11.itemCellView.setText(info.getRequirement().getCell());
                holder11.itemPubTimeView.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder11.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != callback) {
                            callback.onClick(position, info);
                        }
                    }
                });
                break;
            case ITEM_TYPE12:
                ReqViewHolder holder12 = (ReqViewHolder) viewHolder;
                if (info.getStatus().equals(Constant.READ)) {
                    holder12.itemTitleView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder12.itemContentView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder12.itemCellView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                    holder12.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.light_black_color));
                } else {
                    holder12.itemTitleView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder12.itemContentView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder12.itemCellView.setTextColor(context.getResources().getColor(R.color.grey_color));
                    holder12.itemPubTimeView.setTextColor(context.getResources().getColor(R.color.grey_color));
                }
                holder12.itemTitleView.setText(info.getTitle());
                holder12.itemContentView.setText(info.getContent());
                holder12.itemCellView.setText(info.getRequirement().getCell());
                holder12.itemPubTimeView.setText(DateFormatTool.getRelativeTime(info.getCreate_at()));
                holder12.itemView.setOnClickListener(new View.OnClickListener() {
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
                return layoutInflater.inflate(R.layout.list_notice_item_site, null);
            case ITEM_TYPE1:
                return layoutInflater.inflate(R.layout.list_notice_item_site, null);
            case ITEM_TYPE2:
                return layoutInflater.inflate(R.layout.list_notice_item_site, null);
            case ITEM_TYPE3:
                return layoutInflater.inflate(R.layout.list_notice_item_site, null);
            case ITEM_TYPE4:
                return layoutInflater.inflate(R.layout.list_notice_item_sys, null);
            case ITEM_TYPE5:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
            case ITEM_TYPE6:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
            case ITEM_TYPE7:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
            case ITEM_TYPE8:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
            case ITEM_TYPE9:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
            case ITEM_TYPE10:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
            case ITEM_TYPE11:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
            case ITEM_TYPE12:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case ITEM_TYPE0:
                return new SiteViewHolder(view);
            case ITEM_TYPE1:
                return new SiteViewHolder(view);
            case ITEM_TYPE2:
                return new SiteViewHolder(view);
            case ITEM_TYPE3:
                return new SiteViewHolder(view);
            case ITEM_TYPE4:
                return new ReqViewHolder(view);
            case ITEM_TYPE5:
                return new ReqViewHolder(view);
            case ITEM_TYPE6:
                return new ReqViewHolder(view);
            case ITEM_TYPE7:
                return new ReqViewHolder(view);
            case ITEM_TYPE8:
                return new ReqViewHolder(view);
            case ITEM_TYPE9:
                return new ReqViewHolder(view);
            case ITEM_TYPE10:
                return new ReqViewHolder(view);
            case ITEM_TYPE11:
                return new ReqViewHolder(view);
            case ITEM_TYPE12:
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
