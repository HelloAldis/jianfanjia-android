package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.PlanPriceDetail;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;


/**
 * Name: PriceDetailAdapter
 * User: fengliang
 * Date: 2015-10-23
 * Time: 16:14
 */
public class PriceDetailAdapter extends BaseRecyclerViewAdapter<PlanPriceDetail> {
    private static final String TAG = PriceDetailAdapter.class.getName();
    private Plan detailInfo;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private int viewType = -1;
    private boolean isDetail = false;

    public PriceDetailAdapter(Context context, List<PlanPriceDetail> list, Plan detailInfo) {
        super(context, list);
        this.detailInfo = detailInfo;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            viewType = TYPE_HEAD;
        } else {
            viewType = TYPE_ITEM;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<PlanPriceDetail> list) {
        switch (viewType) {
            case TYPE_HEAD:
                PriceDetailHeadHolder priceDetailHeadHolder = (PriceDetailHeadHolder) viewHolder;
                priceDetailHeadHolder.project_total_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                priceDetailHeadHolder.project_total_price.setText("￥" + detailInfo.getProject_price_before_discount());
                priceDetailHeadHolder.project_price_after_discount.setText("￥" + detailInfo
                        .getProject_price_after_discount());
                priceDetailHeadHolder.total_design_fee.setText("￥" + detailInfo.getTotal_design_fee());
                priceDetailHeadHolder.project_price_before_discount.setText("￥" + detailInfo.getTotal_price());
                break;
            case TYPE_ITEM:
                final PlanPriceDetail detail = list.get(position - 1);
                final PriceDetailViewHolder priceDetailViewHolder = (PriceDetailViewHolder) viewHolder;
                priceDetailViewHolder.itemTitle.setText(detail.getItem());
                priceDetailViewHolder.itemContent.setText(detail.getPrice());
                priceDetailViewHolder.itemDetailText.setText(detail.getDescription());
                if (position != 1) {
                    if (!TextUtils.isEmpty(detail.getDescription())) {
                        priceDetailViewHolder.detailLayout.setVisibility(View.VISIBLE);
                        priceDetailViewHolder.itemDes.setVisibility(View.GONE);
                    } else {
                        priceDetailViewHolder.detailLayout.setVisibility(View.GONE);
                        priceDetailViewHolder.itemDes.setVisibility(View.VISIBLE);
                        priceDetailViewHolder.itemDes.setText("无");
                    }
                    priceDetailViewHolder.detailLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isDetail) {
                                isDetail = false;
                                priceDetailViewHolder.detailLayout.setSelected(false);
                                priceDetailViewHolder.itemDetailLayout.setVisibility(View.GONE);
                            } else {
                                isDetail = true;
                                priceDetailViewHolder.detailLayout.setSelected(true);
                                priceDetailViewHolder.itemDetailLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    priceDetailViewHolder.itemDes.setText(detail.getDescription());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View headView = layoutInflater.inflate(R.layout.detail_price_head_layout, null);
                return headView;
            case TYPE_ITEM:
                View itemView = layoutInflater.inflate(R.layout.list_item_price_item, null);
                return itemView;
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case TYPE_HEAD:
                return new PriceDetailHeadHolder(view);
            case TYPE_ITEM:
                return new PriceDetailViewHolder(view);
        }
        return null;
    }

    static class PriceDetailHeadHolder extends RecyclerViewHolderBase {
        @Bind(R.id.project_total_price)
        TextView project_total_price;
        @Bind(R.id.project_price_after_discount)
        TextView project_price_after_discount;
        @Bind(R.id.total_design_fee)
        TextView total_design_fee;
        @Bind(R.id.project_price_before_discount)
        TextView project_price_before_discount;

        public PriceDetailHeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class PriceDetailViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.titleText)
        TextView itemTitle;
        @Bind(R.id.contentText)
        TextView itemContent;
        @Bind(R.id.desText)
        TextView itemDes;
        @Bind(R.id.detailText)
        TextView itemDetail;
        @Bind(R.id.detailLayout)
        RelativeLayout detailLayout;
        @Bind(R.id.itemDetailLayout)
        LinearLayout itemDetailLayout;
        @Bind(R.id.itemDetailText)
        TextView itemDetailText;

        public PriceDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
