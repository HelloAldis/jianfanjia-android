package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.NoticeInfo;
import com.jianfanjia.cn.config.Constant;
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

    private int viewType = -1;

    public NoticeAdapter(Context context, List<NoticeInfo> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {
        NoticeInfo noticeInfo = list.get(position);
        String msgType = noticeInfo.getMessage_type();
        LogTool.d(TAG, "msgType------------------------" + msgType);
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
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<NoticeInfo> list) {
        NoticeInfo info = list.get(position);
        switch (viewType) {
            case ITEM_TYPE0:
                DelayViewHolder holder0 = (DelayViewHolder) viewHolder;
                holder0.itemCellView.setText(info.getTitle());
                holder0.itemNewTimeView.setText(info.getContent());
                break;
            case ITEM_TYPE1:
                CaiGouViewHolder holder1 = (CaiGouViewHolder) viewHolder;
                holder1.itemCellView.setText(info.getTitle());
                holder1.itemNameView.setText(info.getContent());
                break;
            case ITEM_TYPE2:
                PayViewHolder holder2 = (PayViewHolder) viewHolder;
                holder2.itemCellView.setText(info.getTitle());
                holder2.itemNameView.setText(info.getContent());
                break;
            case ITEM_TYPE3:
                CheckViewHolder holder3 = (CheckViewHolder) viewHolder;
                holder3.itemTitle.setText(info.getTitle());
                holder3.itemExample.setText(info.getContent());
                holder3.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE4:
                ReqViewHolder holder4 = (ReqViewHolder) viewHolder;
                holder4.itemTitle.setText(info.getTitle());
                holder4.itemExample.setText(info.getContent());
                holder4.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE5:
                ReqViewHolder holder5 = (ReqViewHolder) viewHolder;
                holder5.itemTitle.setText(info.getTitle());
                holder5.itemExample.setText(info.getContent());
                holder5.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE6:
                ReqViewHolder holder6 = (ReqViewHolder) viewHolder;
                holder6.itemTitle.setText(info.getTitle());
                holder6.itemExample.setText(info.getContent());
                holder6.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE7:
                ReqViewHolder holder7 = (ReqViewHolder) viewHolder;
                holder7.itemTitle.setText(info.getTitle());
                holder7.itemExample.setText(info.getContent());
                holder7.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE8:
                ReqViewHolder holder8 = (ReqViewHolder) viewHolder;
                holder8.itemTitle.setText(info.getTitle());
                holder8.itemExample.setText(info.getContent());
                holder8.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE9:
                ReqViewHolder holder9 = (ReqViewHolder) viewHolder;
                holder9.itemTitle.setText(info.getTitle());
                holder9.itemExample.setText(info.getContent());
                holder9.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE10:
                ReqViewHolder holder10 = (ReqViewHolder) viewHolder;
                holder10.itemTitle.setText(info.getTitle());
                holder10.itemExample.setText(info.getContent());
                holder10.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE11:
                ReqViewHolder holder11 = (ReqViewHolder) viewHolder;
                holder11.itemTitle.setText(info.getTitle());
                holder11.itemExample.setText(info.getContent());
                holder11.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            case ITEM_TYPE12:
                ReqViewHolder holder12 = (ReqViewHolder) viewHolder;
                holder12.itemTitle.setText(info.getTitle());
                holder12.itemExample.setText(info.getContent());
                holder12.itemDate.setText(DateFormatTool.toLocalTimeString(info.getCreate_at()));
                break;
            default:
                break;
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ITEM_TYPE0:
                return layoutInflater.inflate(R.layout.list_notice_item_delay, null);
            case ITEM_TYPE1:
                return layoutInflater.inflate(R.layout.list_notice_item_caigou, null);
            case ITEM_TYPE2:
                return layoutInflater.inflate(R.layout.list_notice_item_pay, null);
            case ITEM_TYPE3:
                return layoutInflater.inflate(R.layout.list_notice_item_check, null);
            case ITEM_TYPE4:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
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
                return new DelayViewHolder(view);
            case ITEM_TYPE1:
                return new CaiGouViewHolder(view);
            case ITEM_TYPE2:
                return new PayViewHolder(view);
            case ITEM_TYPE3:
                return new CheckViewHolder(view);
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
        public TextView itemExample;

        public SysViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.list_item_sys_tip_title);
            itemDate = (TextView) itemView.findViewById(R.id.list_item_sys_tip_date);
            itemExample = (TextView) itemView.findViewById(R.id.list_item_sys_tip_example);
        }
    }

    private static class CaiGouViewHolder extends RecyclerViewHolderBase {
        public TextView itemCellView;
        public TextView itemNameView;// 采购工序视图
        public TextView itemContentView;// 采购内容视图
        public TextView itemNodeView;// 采购节点
        public TextView itemPubTimeView;// 发布时间

        public CaiGouViewHolder(View itemView) {
            super(itemView);
            itemCellView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_cell_name);
            itemContentView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_caigou_content);
            itemNameView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_caigou_name);
            itemNodeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_caigou_node);
            itemPubTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_caigou_time);
        }
    }

    private static class PayViewHolder extends RecyclerViewHolderBase {
        TextView itemCellView;
        TextView itemNameView;// 延迟工序
        TextView itemNodeView;// 延迟节点
        TextView itemPubTimeView;// 发布时间

        public PayViewHolder(View itemView) {
            super(itemView);
            itemCellView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_cell_name);
            itemNameView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_pay_name);
            itemNodeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_pay_node);
            itemPubTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_pay_time);
        }
    }

    private static class DelayViewHolder extends RecyclerViewHolderBase {
        public TextView itemCellView;
        public TextView itemNodeView;// 延迟节点
        public TextView itemNewTimeView;// 延期时间
        public TextView itemPubTimeView;// 发布时间
        public TextView itemStatusView;

        public DelayViewHolder(View itemView) {
            super(itemView);
            itemCellView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_cell_name);
            itemNodeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_delay_node);
            itemNewTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_delay_new_time);
            itemPubTimeView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_delay_time);
            itemStatusView = (TextView) itemView
                    .findViewById(R.id.list_item_tip_delay_status);
        }
    }

    private static class CheckViewHolder extends RecyclerViewHolderBase {
        public TextView itemTitle;
        public TextView itemDate;
        public TextView itemExample;

        public CheckViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.list_item_sys_tip_title);
            itemDate = (TextView) itemView.findViewById(R.id.list_item_sys_tip_date);
            itemExample = (TextView) itemView.findViewById(R.id.list_item_sys_tip_example);
        }
    }

    private static class ReqViewHolder extends RecyclerViewHolderBase {
        public TextView itemTitle;
        public TextView itemDate;
        public TextView itemExample;

        public ReqViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.list_item_req_tip_title);
            itemDate = (TextView) itemView.findViewById(R.id.list_item_req_tip_date);
            itemExample = (TextView) itemView.findViewById(R.id.list_item_req_tip_example);
        }
    }

    private static class SiteViewHolder extends RecyclerViewHolderBase {
        public TextView itemTitle;
        public TextView itemDate;
        public TextView itemCell;
        public TextView itemSection;
        public TextView itemExample;

        public SiteViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.list_item_tip_title);
            itemDate = (TextView) itemView.findViewById(R.id.list_item_tip_date);
            itemCell = (TextView) itemView.findViewById(R.id.list_item_tip_cell);
            itemSection = (TextView) itemView.findViewById(R.id.list_item_tip_section);
            itemExample = (TextView) itemView.findViewById(R.id.list_item_tip_example);
        }
    }
}
