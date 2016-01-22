package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.bean.Process;
import com.jianfanjia.cn.designer.bean.SiteProcessItem;
import com.jianfanjia.cn.designer.fragment.ManageFragment;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.StringUtils;

import java.util.List;

/**
 * Name: MySiteAdapter
 * User: fengliang
 * Date: 2016-01-18
 * Time: 14:21
 */
public class MySiteAdapter extends BaseRecyclerViewAdapter<Process> {
    private static final String TAG = MySiteAdapter.class.getName();
    private ClickCallBack callBack;
    private List<SiteProcessItem> siteProcessList;

    public MySiteAdapter(Context context, List<Process> list, List<SiteProcessItem> siteProcessList, ClickCallBack callBack) {
        super(context, list);
        this.siteProcessList = siteProcessList;
        this.callBack = callBack;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<Process> list) {
        Process process = list.get(position);
        MySiteViewHolder holder = (MySiteViewHolder) viewHolder;
        if (!TextUtils.isEmpty(process.getUser().getImageid())) {
            imageShow.displayImageHeadWidthThumnailImage(context, process.getUser().getImageid(), holder.itemHeadView);
        } else {
            imageShow.displayLocalImage(dataManagerNew.getUserImagePath(),holder.itemHeadView);
        }
        holder.itemCellView.setText(process.getCell());
        String itemNode = MyApplication.getInstance()
                .getStringById(process.getGoing_on());
        LogTool.d(TAG, "itemNode=" + itemNode);
        if (!TextUtils.isEmpty(itemNode)) {
            holder.itemNodeView.setText(itemNode + "阶段");
        } else {
            holder.itemNodeView.setText("已竣工");
        }
        holder.itemPubTimeView.setText(StringUtils.covertLongToString(process.getStart_at()));
        holder.itemUpdateTimeView.setText(StringUtils.covertLongToString(process.getLastupdate()));
        int processIndex = MyApplication.getInstance().getPositionByItemName(process.getGoing_on());
        LogTool.d(TAG, "processIndex=" + processIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.item_process_listview.setLayoutManager(linearLayoutManager);
        ProcessRecyclerViewAdapter adapter = new ProcessRecyclerViewAdapter(context, siteProcessList, processIndex);
        holder.item_process_listview.setAdapter(adapter);
        holder.ltm_req_baseinfo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.click(position, ManageFragment.ITEM_PRIVIEW);
            }
        });
        holder.contractLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.click(position, ManageFragment.ITEM_CONTRACT);
            }
        });
        holder.planLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.click(position, ManageFragment.ITEM_PLAN);
            }
        });
        holder.gotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.click(position, ManageFragment.ITEM_GOTOO_SITE);
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_site_info,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new MySiteViewHolder(view);
    }

    private static class MySiteViewHolder extends RecyclerViewHolderBase {
        public RelativeLayout ltm_req_baseinfo_layout;
        public ImageView itemHeadView;
        public TextView itemCellView;
        public TextView itemNodeView;
        public RecyclerView item_process_listview;
        public TextView itemPubTimeView;
        public TextView itemUpdateTimeView;
        public RelativeLayout contractLayout;
        public RelativeLayout planLayout;
        public RelativeLayout gotoLayout;

        public MySiteViewHolder(View itemView) {
            super(itemView);
            ltm_req_baseinfo_layout = (RelativeLayout) itemView.findViewById(R.id.ltm_req_baseinfo_layout);
            itemHeadView = (ImageView) itemView.findViewById(R.id.ltm_req_owner_head);
            itemCellView = (TextView) itemView.findViewById(R.id.ltm_req_cell);
            itemNodeView = (TextView) itemView.findViewById(R.id.ltm_req_status);
            item_process_listview = (RecyclerView) itemView.findViewById(R.id.item_site_section_listview);
            itemPubTimeView = (TextView) itemView.findViewById(R.id.ltm_req_starttime_cont);
            itemUpdateTimeView = (TextView) itemView.findViewById(R.id.ltm_req_updatetime_cont);
            contractLayout = (RelativeLayout) itemView.findViewById(R.id.contractLayout);
            planLayout = (RelativeLayout) itemView.findViewById(R.id.planLayout);
            gotoLayout = (RelativeLayout) itemView.findViewById(R.id.gotoLayout);
        }
    }
}
