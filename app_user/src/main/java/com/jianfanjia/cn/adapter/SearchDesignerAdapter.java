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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.BusinessCovertUtil;
import com.jianfanjia.cn.tools.ImageShow;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-20 14:59
 */
public class SearchDesignerAdapter extends BaseLoadMoreRecycleAdapter<Designer> {

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

        Designer designerInfo = mDatas.get(position);

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
        holder.itemHighPointView.setVisibility(View.GONE);
        holder.startLine.setBackgroundResource(R.color.horzontal_line_color);
        holder.endLine.setBackgroundResource(R.color.horzontal_line_color);
        List<String> tags = designerInfo.getTags();
        if(tags != null && tags.size() > 0){
            holder.itemTagText.setVisibility(View.VISIBLE);
            holder.itemTagText.setText(tags.get(0));
            switch (tags.get(0)){
                case RequirementBusiness.TAG_NEW_GENERATE:
                    holder.itemTagText.setBackgroundResource(R.drawable.text_rectangle_blue_bg);
                    break;
                case RequirementBusiness.TAG_MIDDER_GENERATE:
                    holder.itemTagText.setBackgroundResource(R.drawable.text_rectangle_pink_bg);
                    break;
                case RequirementBusiness.TAG_HIGH_POINT:
                    holder.itemTagText.setBackgroundResource(R.drawable.text_rectangle_orange_bg);
                    holder.itemHighPointView.setVisibility(View.VISIBLE);
                    holder.startLine.setBackgroundResource(R.color.orange_color);
                    holder.endLine.setBackgroundResource(R.color.orange_color);
                    break;
            }
        }else {
            holder.itemTagText.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnViewClick(position);
            }
        });
        holder.productSumView.setText(designerInfo.getAuthed_product_count());
        holder.designerFeeView.setText(BusinessCovertUtil.convertDesignFeeToShow(designerInfo.getDesign_fee_range()));
        holder.appointSumView.setText(designerInfo.getOrder_count() + "");

        holder.decorateHouseStyleView.setText(BusinessCovertUtil.getHouseTypeStr(designerInfo.getDec_house_types()));
        holder.goodAtStyleView.setText(BusinessCovertUtil.getDecStyleStr(designerInfo.getDec_styles()));
    }

    class SearchDesignerViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ltm_req_owner_head)
        ImageView headImageView;
        @Bind(R.id.ltm_info_auth)
        ImageView infoAuthImageView;
        @Bind(R.id.ltm_req_tag)
        TextView itemTagText;
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

        @Bind(R.id.designerinfo_high_point)
        ImageView itemHighPointView;

        @Bind(R.id.start_line)
        View startLine;

        @Bind(R.id.end_line)
        View endLine;

        public SearchDesignerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
