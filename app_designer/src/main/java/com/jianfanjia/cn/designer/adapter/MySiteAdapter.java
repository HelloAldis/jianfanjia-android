package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.api.model.Process;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.application.MyApplication;
import com.jianfanjia.cn.designer.base.RecyclerViewAdapterBase;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.bean.ProcessSectionItem;
import com.jianfanjia.cn.designer.fragment.ManageFragment;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.interf.OnItemClickListener;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.cn.designer.tools.StringUtils;
import com.jianfanjia.common.tool.LogTool;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Name: MySiteAdapter
 * User: fengliang
 * Date: 2016-01-18
 * Time: 14:21
 */
public class MySiteAdapter extends RecyclerViewAdapterBase<Process> {
    private static final String TAG = MySiteAdapter.class.getName();
    private ClickCallBack callBack;
    private List<ProcessSectionItem> siteProcessList;
    private int processIndex = -1;
    private Context context;

    public MySiteAdapter(Context context, List<ProcessSectionItem> siteProcessList, ClickCallBack
            callBack) {
        this.context = context;
        this.siteProcessList = siteProcessList;
        this.callBack = callBack;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        Process process = items.get(position);
        MySiteViewHolder holder = (MySiteViewHolder) viewHolder;
        if (!TextUtils.isEmpty(process.getUser().getImageid())) {
            ImageShow.getImageShow().displayImageHeadWidthThumnailImage(context, process.getUser().getImageid(),
                    holder.itemHeadView);
        } else {
            holder.itemHeadView.setImageResource(R.mipmap.icon_default_head);
        }
        holder.itemCellView.setText(process.getBasic_address());
        LogTool.d(TAG, "process.getGoing_on()=" + process.getGoing_on());
        String itemNode = null;
        if (process.getGoing_on().equals("done")) {
            holder.itemNodeView.setTextColor(context.getResources().getColor(R.color.green_color));
            holder.itemNodeView.setText(context.getResources().getText(R.string.all_finish));
            processIndex = 7;
        } else {
            processIndex = MyApplication.getInstance().getPositionByItemName(process.getGoing_on());
            LogTool.d(TAG, "processIndex =" + processIndex);
            itemNode = process.getSections().get(processIndex).getLabel();
            holder.itemNodeView.setText(itemNode + "阶段");

        }
        holder.itemPubTimeView.setText(StringUtils.covertLongToString(process.getStart_at()));
        holder.itemUpdateTimeView.setText(StringUtils.covertLongToString(process.getLastupdate()));
        LogTool.d(TAG, "processIndex=" + processIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.item_process_listview.setLayoutManager(linearLayoutManager);
        ProcessRecyclerViewAdapter adapter = new ProcessRecyclerViewAdapter(context, process.getSections(),
                new OnItemClickListener() {
                    @Override
                    public void OnItemClick(int pos) {
                        if (null != callBack) {
                            callBack.click(position, ManageFragment.ITEM_GOTOO_SITE);
                        }
                    }
                });
        holder.item_process_listview.setAdapter(adapter);
        holder.ltm_req_baseinfo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != callBack) {
                    callBack.click(position, ManageFragment.ITEM_PRIVIEW);
                }
            }
        });
        holder.contractLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != callBack) {
                    callBack.click(position, ManageFragment.ITEM_CONTRACT);
                }
            }
        });
        holder.planLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != callBack) {
                    callBack.click(position, ManageFragment.ITEM_PLAN);
                }
            }
        });
        holder.gotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != callBack) {
                    callBack.click(position, ManageFragment.ITEM_GOTOO_SITE);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_site_info,
                null);
        return new MySiteViewHolder(view);
    }

    static class MySiteViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.ltm_req_baseinfo_layout)
        RelativeLayout ltm_req_baseinfo_layout;

        @Bind(R.id.ltm_req_owner_head)
        ImageView itemHeadView;

        @Bind(R.id.ltm_req_cell)
        TextView itemCellView;

        @Bind(R.id.ltm_req_status)
        TextView itemNodeView;

        @Bind(R.id.item_site_section_listview)
        RecyclerView item_process_listview;

        @Bind(R.id.ltm_req_starttime_cont)
        TextView itemPubTimeView;

        @Bind(R.id.ltm_req_updatetime_cont)
        TextView itemUpdateTimeView;

        @Bind(R.id.contractLayout)
        RelativeLayout contractLayout;

        @Bind(R.id.planLayout)
        RelativeLayout planLayout;

        @Bind(R.id.gotoLayout)
        RelativeLayout gotoLayout;

        public MySiteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
