package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.NoticeInfo;

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
    private int viewType = -1;

    public NoticeAdapter(Context context, List<NoticeInfo> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {
        NoticeInfo noticeInfo = list.get(position);
        return super.getItemViewType(position);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<NoticeInfo> list) {
        NoticeInfo info = list.get(position);
        switch (viewType) {
            case ITEM_TYPE0:
                break;
            case ITEM_TYPE1:
                break;
            case ITEM_TYPE2:
                break;
            case ITEM_TYPE3:
                break;
            default:
                break;
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ITEM_TYPE0:
                return layoutInflater.inflate(R.layout.list_notice_item_sys, null);
            case ITEM_TYPE1:
                return layoutInflater.inflate(R.layout.list_notice_item_req, null);
            case ITEM_TYPE2:
                return layoutInflater.inflate(R.layout.list_notice_item_site, null);
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case ITEM_TYPE0:
                return new SysViewHolder(view);
            case ITEM_TYPE1:
                return new ReqViewHolder(view);
            case ITEM_TYPE2:
                return new SiteViewHolder(view);
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
