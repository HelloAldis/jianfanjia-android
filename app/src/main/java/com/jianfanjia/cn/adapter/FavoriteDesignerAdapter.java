package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

/**
 * Name: FavoriteDesignerAdapter
 * User: fengliang
 * Date: 2015-12-11
 * Time: 17:43
 */
public class FavoriteDesignerAdapter extends BaseRecyclerViewAdapter<DesignerInfo> {
    private static final String TAG = FavoriteDesignerAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public FavoriteDesignerAdapter(Context context, List<DesignerInfo> list, RecyclerViewOnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<DesignerInfo> list) {
        DesignerInfo designerInfo = list.get(position);
        final FavoriteDesignerViewHolder holder = (FavoriteDesignerViewHolder) viewHolder;
        holder.ltm_myfavdesi_name.setText(TextUtils.isEmpty(designerInfo.getUsername()) ? context.getResources()
                .getString(R.string.designer) : designerInfo.getUsername());
        String imageid = designerInfo.getImageid();
        if (!TextUtils.isEmpty(imageid)) {
            imageShow.displayImageHeadWidthThumnailImage(context, imageid, holder.ltm_myfavdesi_head);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_OWNER_PIC, holder.ltm_myfavdesi_head);
        }
        holder.ltm_myfavdesi_score.setRating((int) (designerInfo.getRespond_speed() + designerInfo
                .getService_attitude()) / 2);
        if (designerInfo.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            holder.authView.setVisibility(View.VISIBLE);
        } else {
            holder.authView.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.OnItemClick(v, holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_my_favorite_designer,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new FavoriteDesignerViewHolder(view);
    }


    static class FavoriteDesignerViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.ltm_myfavdesi_name)
        TextView ltm_myfavdesi_name;
        @Bind(R.id.ltm_myfavdesi_head)
        ImageView ltm_myfavdesi_head;
        @Bind(R.id.ltm_myfavdesi_score)
        RatingBar ltm_myfavdesi_score;
        @Bind(R.id.designerinfo_auth)
        ImageView authView;

        public FavoriteDesignerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
