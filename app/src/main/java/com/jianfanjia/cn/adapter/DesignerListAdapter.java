package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

import java.util.List;

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
        int respond_speed = (int) designerInfo.getRespond_speed();
        int service_attitude = (int) designerInfo.getService_attitude();
        holder.itemRatingBar.setRating((respond_speed + service_attitude) / 2);
//     holder.itemAppointCountText.setText(designerInfo.getOrder_count());
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

    private static class DesignerListViewHolder extends RecyclerViewHolderBase {
        public ImageView itemHeadView;
        public TextView itemNameText;
        public ImageView itemIdentityView;
        public ImageView itemAuthView;
        public RatingBar itemRatingBar;
        public TextView itemDecTypeText;
        public TextView itemDecStyleText;
        public TextView itemProductCountText;
        public TextView itemAppointCountText;
        public TextView itemDecFeeText;

        public DesignerListViewHolder(View itemView) {
            super(itemView);
            itemHeadView = (ImageView) itemView
                    .findViewById(R.id.ltm_req_owner_head);
            itemNameText = (TextView) itemView
                    .findViewById(R.id.ltm_req_username);
            itemIdentityView = (ImageView) itemView
                    .findViewById(R.id.ltm_identity_auth);
            itemAuthView = (ImageView) itemView
                    .findViewById(R.id.ltm_info_auth);
            itemRatingBar = (RatingBar) itemView
                    .findViewById(R.id.ratingBar);
            itemDecTypeText = (TextView) itemView
                    .findViewById(R.id.ltm_decoratehousetype_cont);
            itemDecStyleText = (TextView) itemView
                    .findViewById(R.id.ltm_good_at_style_cont);
            itemProductCountText = (TextView) itemView
                    .findViewById(R.id.product_sum);
            itemAppointCountText = (TextView) itemView
                    .findViewById(R.id.appoint_sum);
            itemDecFeeText = (TextView) itemView
                    .findViewById(R.id.designer_fee);
        }
    }
}
