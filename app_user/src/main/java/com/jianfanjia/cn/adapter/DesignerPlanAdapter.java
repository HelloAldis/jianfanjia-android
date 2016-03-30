package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.jianfanjia.api.model.Plan;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.interf.ItemClickListener;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.DateFormatTool;

/**
 * Name: DesignerPlanAdapter
 * User: fengliang
 * Date: 2015-10-22
 * Time: 17:54
 */
public class DesignerPlanAdapter extends BaseRecyclerViewAdapter<Plan> {
    private ItemClickListener itemClickListener;

    public DesignerPlanAdapter(Context context, List<Plan> list, ItemClickListener itemClickListener) {
        super(context, list);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Plan> list) {
        Plan info = list.get(position);
        final DesignerPlanViewHolder holder = (DesignerPlanViewHolder) viewHolder;
        holder.numText.setText(TextUtils.isEmpty(info.getName()) ? "" : info.getName());
        holder.dateText.setText(DateFormatTool.longToString(info.getLast_status_update_time()));
        holder.commentText.setText("留言(" + info.getComment_count() + ")");
        String status = info.getStatus();
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
        List<String> imgList = info.getImages();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.item_plan_listview.setLayoutManager(linearLayoutManager);
        DesignerPlanRecyclerViewAdapter adapter = new DesignerPlanRecyclerViewAdapter(context, imgList, new
                ViewPagerClickListener() {
                    @Override
                    public void onClickItem(int pos) {
                        if (null != itemClickListener) {
                            itemClickListener.onCallBack(holder.getLayoutPosition(), pos);
                        }
                    }
                });
        holder.item_plan_listview.setAdapter(adapter);
        holder.commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onItemCallBack(holder.getLayoutPosition(), Constant.PLAN_COMMENT_ITEM);
                }
            }
        });
        holder.previewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onItemCallBack(holder.getLayoutPosition(), Constant.PLAN_PREVIEW_ITEM);
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_plan_info,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DesignerPlanViewHolder(view);
    }

    static class DesignerPlanViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.numText)
        TextView numText;
        @Bind(R.id.statusText)
        TextView statusText;
        @Bind(R.id.item_plan_listview)
        RecyclerView item_plan_listview;
        @Bind(R.id.dateText)
        TextView dateText;
        @Bind(R.id.commentText)
        TextView commentText;
        @Bind(R.id.previewText)
        TextView previewText;

        public DesignerPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
