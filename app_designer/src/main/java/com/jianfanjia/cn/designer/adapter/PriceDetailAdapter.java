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
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.business.RequirementBusiness;


/**
 * Name: PriceDetailAdapter
 * User: fengliang
 * Date: 2015-10-23
 * Time: 16:14
 */
public class PriceDetailAdapter extends BaseRecyclerViewAdapter<PlanPriceDetail> {
    private static final String TAG = PriceDetailAdapter.class.getName();
    private Plan detailInfo;
    private static final int TYPE_HEAD_DEFAULT = 0;
    private static final int TYPE_HEAD_365 = 1;
    private static final int TYPE_ITEM = 2;
    private int viewType = -1;
    private boolean isDetail = false;

    private String packageType;
    private String basicPrice;//365包基础报价
    private String individurationPrice;//365包个性化报价

    public PriceDetailAdapter(Context context, List<PlanPriceDetail> list, Plan detailInfo, String packageType) {
        super(context, list);
        this.detailInfo = detailInfo;
        this.packageType = packageType;
        init365PackgetInfo();
        setItemHeadTitle();
    }

    private void init365PackgetInfo() {
        PlanPriceDetail planPriceDetail = RequirementBusiness.getPackget365PriceDetail(list);
        if (planPriceDetail != null) {
            basicPrice = planPriceDetail.getPrice();
            individurationPrice = RequirementBusiness.getIndividurationPrice(detailInfo.getTotal_price() + "",
                    basicPrice);
        }
    }

    private void setItemHeadTitle() {
        PlanPriceDetail detail = new PlanPriceDetail();
        detail.setItem(context.getResources().getString(R.string.project_text));
        detail.setPrice(context.getResources().getString(R.string.project_price_text));
        detail.setDescription(context.getResources().getString(R.string.des_text));
        if (null != list && list.size() > 0) {
            list.add(0, detail);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (packageType.equals(RequirementBusiness.PACKGET_365)) {
                viewType = TYPE_HEAD_365;
            } else {
                viewType = TYPE_HEAD_DEFAULT;
            }
        } else {
            viewType = TYPE_ITEM;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() + 1 : 1;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<PlanPriceDetail> list) {
        switch (viewType) {
            case TYPE_HEAD_365:
                PriceDetailHead365Holder priceDetailHead365Holder = (PriceDetailHead365Holder) viewHolder;
                priceDetailHead365Holder.project_total_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                priceDetailHead365Holder.project_total_price.setText(context.getString(R.string.money_alias) +
                        detailInfo
                                .getProject_price_before_discount());
                priceDetailHead365Holder.project_price_after_discount.setText(context.getString(R.string.money_alias)
                        + detailInfo
                        .getProject_price_after_discount());
                priceDetailHead365Holder.total_design_fee.setText(context.getString(R.string.money_alias) +
                        detailInfo.getTotal_design_fee());
                priceDetailHead365Holder.project_price_before_discount.setText(context.getString(R.string
                        .money_alias) + detailInfo.getTotal_price());
                priceDetailHead365Holder.project_individutation_price.setText(context.getString(R.string.money_alias)
                        + (TextUtils.isEmpty(individurationPrice) ? 0 : individurationPrice));
                priceDetailHead365Holder.project_basic_price.setText(context.getString(R.string.money_alias) +
                        (TextUtils.isEmpty(basicPrice) ? 0 : individurationPrice));
                break;
            case TYPE_HEAD_DEFAULT:
                PriceDetailHeadHolder priceDetailHeadHolder = (PriceDetailHeadHolder) viewHolder;
                priceDetailHeadHolder.project_total_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                priceDetailHeadHolder.project_total_price.setText(context.getString(R.string.money_alias) +
                        detailInfo.getProject_price_before_discount());
                priceDetailHeadHolder.project_price_after_discount.setText(context.getString(R.string.money_alias) +
                        detailInfo
                                .getProject_price_after_discount());
                priceDetailHeadHolder.total_design_fee.setText(context.getString(R.string.money_alias) + detailInfo
                        .getTotal_design_fee());
                priceDetailHeadHolder.project_price_before_discount.setText(context.getString(R.string.money_alias) +
                        detailInfo.getTotal_price());
                break;
            case TYPE_ITEM:
                final PlanPriceDetail detail = list.get(position - 1);
                final PriceDetailViewHolder priceDetailViewHolder = (PriceDetailViewHolder) viewHolder;
                priceDetailViewHolder.itemTitle.setText(detail.getItem());
                if(detail.getItem().equals(RequirementBusiness.PACKGET_365_ITEM)){
                    priceDetailViewHolder.itemLayout.setBackgroundResource(R.color.transparent_background);
                }else {
                    priceDetailViewHolder.itemLayout.setBackgroundResource(R.color.font_white);
                }
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
            case TYPE_HEAD_DEFAULT:
                View headDefaultView = layoutInflater.inflate(R.layout.detail_price_head_layout, null);
                return headDefaultView;
            case TYPE_HEAD_365:
                View head365View = layoutInflater.inflate(R.layout.detail_price_head_365_layout, null);
                return head365View;
            case TYPE_ITEM:
                View itemView = layoutInflater.inflate(R.layout.list_item_price_item, null);
                return itemView;
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case TYPE_HEAD_DEFAULT:
                return new PriceDetailHeadHolder(view);
            case TYPE_HEAD_365:
                return new PriceDetailHead365Holder(view);
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

    static class PriceDetailHead365Holder extends RecyclerViewHolderBase {
        @Bind(R.id.project_total_price)
        TextView project_total_price;
        @Bind(R.id.project_price_after_discount)
        TextView project_price_after_discount;
        @Bind(R.id.total_design_fee)
        TextView total_design_fee;
        @Bind(R.id.project_price_before_discount)
        TextView project_price_before_discount;
        @Bind(R.id.project_basic_price)
        TextView project_basic_price;
        @Bind(R.id.project_individutation_price)
        TextView project_individutation_price;

        public PriceDetailHead365Holder(View itemView) {
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
        @Bind(R.id.itemLayout)
        LinearLayout itemLayout;
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
