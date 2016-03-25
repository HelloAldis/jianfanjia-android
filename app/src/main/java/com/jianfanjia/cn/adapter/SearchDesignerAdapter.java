package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.ImageShow;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-20 14:59
 */
public class SearchDesignerAdapter extends BaseRecycleAdapter<DesignerInfo> {

    private RecyclerViewOnItemClickListener listener;

    public SearchDesignerAdapter(Context context, RecyclerView recyclerView, RecyclerViewOnItemClickListener
            recyclerViewOnItemClickListener) {
        super(context, recyclerView);
        this.listener = recyclerViewOnItemClickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_designer_common, null);
        return new SearchDesignerViewHolder(view);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        SearchDesignerViewHolder holder = (SearchDesignerViewHolder) viewHolder;

        DesignerInfo designerInfo = mDatas.get(position);

        holder.nameView.setText(TextUtils.isEmpty(designerInfo.getUsername()) ? context.getResources().getString(R
                .string.designer) : designerInfo.getUsername());
        String imageid = designerInfo.getImageid();
        if (!TextUtils.isEmpty(imageid)) {
            ImageShow.getImageShow().displayImageHeadWidthThumnailImage(context, imageid, holder.headImageView);
        } else {
            ImageShow.getImageShow().displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.headImageView);
        }
        holder.ratingBar.setRating((int) (designerInfo.getRespond_speed() + designerInfo.getService_attitude()) / 2);
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            holder.infoAuthImageView.setVisibility(View.VISIBLE);
        } else {
            holder.infoAuthImageView.setVisibility(View.GONE);
        }
        if (designerInfo.getUid_auth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            holder.identityAuthImageView.setVisibility(View.VISIBLE);
        } else {
            holder.identityAuthImageView.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnViewClick(position);
            }
        });
        holder.productSumView.setText(designerInfo.getAuthed_product_count());
        holder.designerFeeView.setText(BusinessManager.convertDesignFeeToShow(designerInfo.getDesign_fee_range()));
        holder.appointSumView.setText(designerInfo.getOrder_count() + "");

        holder.decorateHouseStyleView.setText(BusinessManager.getHouseTypeStr(designerInfo.getDec_house_types()));
        holder.goodAtStyleView.setText(BusinessManager.getDecStyleStr(designerInfo.getDec_styles()));
    }

    class SearchDesignerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ltm_req_owner_head)
        ImageView headImageView;
        @Bind(R.id.ltm_info_auth)
        ImageView infoAuthImageView;
        @Bind(R.id.ltm_identity_auth)
        ImageView identityAuthImageView;
        @Bind(R.id.ltm_req_username)
        TextView nameView;
        @Bind(R.id.ltm_good_at_style_cont)
        TextView goodAtStyleView;
        @Bind(R.id.ltm_decoratehousetype_cont)
        TextView decorateHouseStyleView;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.product_sum)
        TextView productSumView;
        @Bind(R.id.appoint_sum)
        TextView appointSumView;
        @Bind(R.id.designer_fee)
        TextView designerFeeView;

        public SearchDesignerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
