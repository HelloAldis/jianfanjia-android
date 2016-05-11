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
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.RequirementBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.cn.tools.BusinessCovertUtil;

/**
 * Name: DesignerListAdapter
 * User: fengliang
 * Date: 2015-10-14
 * Time: 14:03
 */
public class DesignerListAdapter extends BaseRecyclerViewAdapter<Designer> {
    private static final String TAG = DesignerListAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public DesignerListAdapter(Context context, List<Designer> list, RecyclerViewOnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<Designer> list) {
        Designer designerInfo = list.get(position);
        DesignerListViewHolder holder = (DesignerListViewHolder) viewHolder;
        holder.itemNameText.setText(designerInfo.getUsername());
        imageShow.displayImageHeadWidthThumnailImage(context, designerInfo.getImageid(), holder.itemHeadView);
        holder.itemProductCountText.setText(designerInfo.getAuthed_product_count());
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            holder.itemAuthView.setVisibility(View.VISIBLE);
        } else {
            holder.itemAuthView.setVisibility(View.GONE);
        }
        holder.itemHighPointView.setVisibility(View.GONE);
        holder.startLine.setBackgroundResource(R.color.horzontal_line_color);
        holder.endLine.setBackgroundResource(R.color.horzontal_line_color);

        List<String> tags = designerInfo.getTags();
        if (tags != null && tags.size() > 0) {
            holder.itemTagText.setVisibility(View.VISIBLE);
            holder.itemTagText.setText(tags.get(0));
            switch (tags.get(0)) {
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
        } else {
            holder.itemTagText.setVisibility(View.GONE);
        }
        int respond_speed = (int) designerInfo.getRespond_speed();
        int service_attitude = (int) designerInfo.getService_attitude();
        holder.itemRatingBar.setRating((respond_speed + service_attitude) / 2);
        holder.itemAppointCountText.setText(designerInfo.getOrder_count() + "");
        holder.itemDecTypeText.setText(BusinessCovertUtil.getHouseTypeStr(designerInfo.getDec_house_types()));
        holder.itemDecStyleText.setText(BusinessCovertUtil.getDecStyleStr(designerInfo.getDec_styles()));
        holder.itemDecFeeText.setText(BusinessCovertUtil.convertDesignFeeToShow(designerInfo.getDesign_fee_range()));
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
        @Bind(R.id.ltm_req_tag)
        TextView itemTagText;
        @Bind(R.id.ltm_info_auth)
        ImageView itemAuthView;

        @Bind(R.id.designerinfo_high_point)
        ImageView itemHighPointView;

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

        @Bind(R.id.start_line)
        View startLine;

        @Bind(R.id.end_line)
        View endLine;

        public DesignerListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
