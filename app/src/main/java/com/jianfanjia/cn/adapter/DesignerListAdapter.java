package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

/**
 * Name: DesignerListAdapter
 * User: fengliang
 * Date: 2015-10-14
 * Time: 14:03
 */
public class DesignerListAdapter extends BaseRecyclerViewAdapter<DesignerInfo> {
    private static final String TAG = DesignerListAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public DesignerListAdapter(Context context, List<DesignerInfo> list, RecyclerViewOnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DesignerInfo> list) {
        DesignerInfo designerInfo = list.get(position);
        DesignerListViewHolder holder = (DesignerListViewHolder) viewHolder;
        holder.itemNameText.setText(designerInfo.getUsername());
        imageShow.displayImageHeadWidthThumnailImage(context, designerInfo.getImageid(), holder.itemHeadView);
        holder.itemProductCountText.setText(designerInfo.getAuthed_product_count());
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            holder.itemAuthView.setVisibility(View.VISIBLE);
        } else {
            holder.itemAuthView.setVisibility(View.GONE);
        }
        if (designerInfo.getUid_auth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            holder.itemIdentityView.setVisibility(View.VISIBLE);
        } else {
            holder.itemIdentityView.setVisibility(View.GONE);
        }
        int respond_speed = (int) designerInfo.getRespond_speed();
        int service_attitude = (int) designerInfo.getService_attitude();
        holder.itemRatingBar.setRating((respond_speed + service_attitude) / 2);
        holder.itemAppointCountText.setText(designerInfo.getOrder_count() + "");
        holder.itemDecTypeText.setText(BusinessManager.getHouseTypeStr(designerInfo.getDec_house_types()));
        holder.itemDecStyleText.setText(BusinessManager.getDecStyleStr(designerInfo.getDec_styles()));
        holder.itemDecFeeText.setText(BusinessManager.convertDesignFeeToShow(designerInfo.getDesign_fee_range()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.OnViewClick(position);
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item_designer_common,
                null);
        return itemView;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DesignerListViewHolder(view);
    }

    static class DesignerListViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.ltm_req_owner_head)
        ImageView itemHeadView;
        @Bind(R.id.ltm_req_username)
        TextView itemNameText;
        @Bind(R.id.ltm_identity_auth)
        ImageView itemIdentityView;
        @Bind(R.id.ltm_info_auth)
        ImageView itemAuthView;
        @Bind(R.id.ratingBar)
        RatingBar itemRatingBar;
        @Bind(R.id.ltm_decoratehousetype_cont)
        TextView itemDecTypeText;
        @Bind(R.id.ltm_good_at_style_cont)
        TextView itemDecStyleText;
        @Bind(R.id.product_sum)
        TextView itemProductCountText;
        @Bind(R.id.appoint_sum)
        TextView itemAppointCountText;
        @Bind(R.id.designer_fee)
        TextView itemDecFeeText;

        public DesignerListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
