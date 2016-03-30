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
import com.jianfanjia.api.model.Process;
import com.jianfanjia.cn.designer.bean.SiteProcessItem;
import com.jianfanjia.cn.designer.fragment.ManageFragment;
import com.jianfanjia.cn.designer.interf.ClickCallBack;
import com.jianfanjia.cn.designer.interf.OnItemClickListener;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private int processIndex = -1;

    public MySiteAdapter(Context context, List<Process> list, List<SiteProcessItem> siteProcessList, ClickCallBack
            callBack) {
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
            holder.itemHeadView.setImageResource(R.mipmap.icon_default_head);
        }
        holder.itemCellView.setText(process.getCell());
        LogTool.d(TAG, "process.getGoing_on()=" + process.getGoing_on());
        String itemNode = null;
        if (process.getGoing_on().equals("done")) {
            holder.itemNodeView.setText(context.getResources().getText(R.string.all_finish));
            processIndex = 7;
        } else {
            itemNode = MyApplication.getInstance()
                    .getStringById(process.getGoing_on());
            holder.itemNodeView.setText(itemNode + "阶段");
            processIndex = MyApplication.getInstance().getPositionByItemName(process.getGoing_on());
        }
        holder.itemPubTimeView.setText(StringUtils.covertLongToString(process.getStart_at()));
        holder.itemUpdateTimeView.setText(StringUtils.covertLongToString(process.getLastupdate()));
        LogTool.d(TAG, "processIndex=" + processIndex);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.item_process_listview.setLayoutManager(linearLayoutManager);
        ProcessRecyclerViewAdapter adapter = new ProcessRecyclerViewAdapter(context, siteProcessList, processIndex,
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
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_site_info,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
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
