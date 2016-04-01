package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.interf.ViewPagerClickListener;
import com.jianfanjia.common.tool.TDevice;

/**
 * Name: DesignerPlanRecyclerViewAdapter
 * User: fengliang
 * Date: 2016-01-15
 * Time: 11:54
 */
public class DesignerPlanRecyclerViewAdapter extends BaseRecyclerViewAdapter<String> {
    private static final String TAG = DesignerPlanRecyclerViewAdapter.class.getName();
    private ViewPagerClickListener itemClickListener;

    public DesignerPlanRecyclerViewAdapter(Context context, List<String> list, ViewPagerClickListener itemClickListener) {
        super(context, list);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<String> list) {
        String imgId = list.get(position);
        DesignerPlanViewHolder holder = (DesignerPlanViewHolder) viewHolder;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.itemImgView.getLayoutParams();
        layoutParams.width = (int) TDevice.getScreenWidth() / 3;
        layoutParams.height = (int) TDevice.getScreenWidth() / 3;
        holder.itemImgView.setLayoutParams(layoutParams);
        imageShow.displayThumbnailImage(imgId, holder.itemImgView, (int) TDevice.getScreenWidth() / 3);
        holder.itemImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != itemClickListener) {
                    itemClickListener.onClickItem(position);
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_plan_view_item,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DesignerPlanViewHolder(view);
    }

    static class DesignerPlanViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_plan_img)
        public ImageView itemImgView;

        public DesignerPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
