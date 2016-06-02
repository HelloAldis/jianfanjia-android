package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.api.model.ProductImageInfo;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;
import com.jianfanjia.cn.designer.tools.ImageShow;
import com.jianfanjia.common.tool.TDevice;


/**
 * Name: DesignerCaseAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 15:14
 */
public class DesignerCaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Product designerCaseInfo;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_PLAN = 2;

    List<ProductImageInfo> mPlanImgLists;
    List<ProductImageInfo> mEffectImgLists;

    private ImageShow imageShow;

    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener mOnItemClickListener;

    public DesignerCaseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        imageShow = ImageShow.getImageShow();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setDesignerCaseInfo(Product designerCaseInfo) {
        this.designerCaseInfo = designerCaseInfo;
        mPlanImgLists = designerCaseInfo.getPlan_images();
        mEffectImgLists = designerCaseInfo.getImages();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else if (position == 1) {
            if (mPlanImgLists != null && mPlanImgLists.size() > 0) {
                return TYPE_PLAN;
            }
            return TYPE_ITEM;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (designerCaseInfo == null) {
            return 0;
        }
        int totalCount = 1;
        if (mPlanImgLists != null && mPlanImgLists.size() > 0) {
            totalCount += 1;
        }
        if (mEffectImgLists != null) {
            totalCount += mEffectImgLists.size();
        }
        return totalCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View headView = layoutInflater.inflate(R.layout.list_item_designer_case_info_layout, null);
                return new DesignerCaseInfoHeadHolder(headView);
            case TYPE_ITEM:
                View itemView = layoutInflater.inflate(R.layout.list_item_designer_case, null);
                return new DesignerCaseViewHolder(itemView);
            case TYPE_PLAN:
                View planView = layoutInflater.inflate(R.layout.list_item_designer_case_plan_img, null);
                return new DesignerPlanImageViewHolder(planView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                DesignerCaseInfoHeadHolder designerCaseInfoHeadHolder = (DesignerCaseInfoHeadHolder) viewHolder;
                designerCaseInfoHeadHolder.cellNameText.setText(designerCaseInfo.getCell());
                designerCaseInfoHeadHolder.stylelNameText.setText(designerCaseInfo.getHouse_area() + "㎡，" +
                        BusinessCovertUtil.convertDectypeToShow(designerCaseInfo.getDec_type()) + "，" +
                        BusinessCovertUtil.convertHouseTypeToShow(designerCaseInfo.getHouse_type()) + "，" +
                        BusinessCovertUtil.convertDecStyleToShow(designerCaseInfo.getDec_style()) + "风格");
                imageShow.displayImageHeadWidthThumnailImage(context, designerCaseInfo.getDesigner().getImageid(),
                        designerCaseInfoHeadHolder.designerinfo_head_img);
                if (designerCaseInfo.getDesigner().getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                    designerCaseInfoHeadHolder.designerinfo_auth.setVisibility(View.VISIBLE);
                } else {
                    designerCaseInfoHeadHolder.designerinfo_auth.setVisibility(View.GONE);
                }
                String decType = BusinessCovertUtil.convertDectypeToShow(designerCaseInfo.getDec_type());
                if (!TextUtils.isEmpty(decType)) {
                    designerCaseInfoHeadHolder.tvDecTypeAndTotalPrice.setText(decType + "，" + designerCaseInfo
                            .getTotal_price() + context.getString(R.string.unit_million));
                } else {
                    designerCaseInfoHeadHolder.tvDecTypeAndTotalPrice.setText(designerCaseInfo.getTotal_price() +
                            context.getString(R.string.unit_million));
                }
                designerCaseInfoHeadHolder.itemTitleText.setText("设计简介");
                designerCaseInfoHeadHolder.itemProduceText.setText(designerCaseInfo.getDescription());
                break;
            case TYPE_ITEM:
                final int lastPos;
                if (mPlanImgLists != null && mPlanImgLists.size() > 0) {
                    lastPos = position - 2;
                } else {
                    lastPos = position - 1;
                }
                ProductImageInfo info = mEffectImgLists.get(lastPos);
                final DesignerCaseViewHolder holder = (DesignerCaseViewHolder) viewHolder;
                imageShow.displayScreenWidthThumnailImage(context, info.getImageid(), holder.itemwCaseView);
                holder.itemTitleText.setText(info.getSection());
                holder.itemProduceText.setText(info.getDescription());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(lastPos + (mPlanImgLists != null ? mPlanImgLists.size()
                                    : 0));
                        }
                    }
                });
                break;
            case TYPE_PLAN:
                bindPlanImageViewHolder((DesignerPlanImageViewHolder) viewHolder);
                break;
            default:
                break;
        }
    }

    private void bindPlanImageViewHolder(DesignerPlanImageViewHolder viewHolder) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false);
        viewHolder.mRecyclerView.setLayoutManager(linearLayoutManager);
        viewHolder.mRecyclerView.setAdapter(new PlanImageAdapter(context, mPlanImgLists));
    }


    static class DesignerCaseInfoHeadHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.cell_name)
        TextView cellNameText;
        @Bind(R.id.stylelName)
        TextView stylelNameText;
        @Bind(R.id.designerinfo_head_img)
        ImageView designerinfo_head_img = null;
        @Bind(R.id.designerinfo_auth)
        ImageView designerinfo_auth = null;
        @Bind(R.id.produceTitle)
        TextView itemTitleText;
        @Bind(R.id.produceText)
        TextView itemProduceText;

        @Bind(R.id.tv_dectype_and_totalprice)
        TextView tvDecTypeAndTotalPrice;

        public DesignerCaseInfoHeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DesignerCaseViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.list_item_case_img)
        ImageView itemwCaseView;
        @Bind(R.id.list_item_case_title_text)
        TextView itemTitleText;
        @Bind(R.id.list_item_case_produce_text)
        TextView itemProduceText;

        public DesignerCaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DesignerPlanImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.recyclerview_designer_case_plan)
        RecyclerView mRecyclerView;

        public DesignerPlanImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    class PlanImageAdapter extends RecyclerView.Adapter {

        private List<ProductImageInfo> mProductImageInfos;
        private LayoutInflater mLayoutInflater;
        ImageShow mImageShow;
        Context mContext;

        public PlanImageAdapter(Context context, List<ProductImageInfo> list) {
            this.mProductImageInfos = list;
            this.mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            mImageShow = ImageShow.getImageShow();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.list_item_designer_case_plan_item, null);
            int width = (int) TDevice.getScreenWidth() - TDevice.dip2px(context, 24);
            view.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup
                    .LayoutParams.WRAP_CONTENT));
            return new PlanImageItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            PlanImageItemViewHolder planImageItemViewHolder = (PlanImageItemViewHolder) holder;
            mImageShow.displayScreenWidthThumnailImage(mContext, mProductImageInfos.get(position).getImageid(),
                    planImageItemViewHolder.ivPlanImg);
            planImageItemViewHolder.ivPlanImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mProductImageInfos == null ? 0 : mProductImageInfos.size();
        }

        class PlanImageItemViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.iv_plan_img)
            ImageView ivPlanImg;

            public PlanImageItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }
    }
}
