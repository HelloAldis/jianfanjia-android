package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.interf.ViewPagerClickListener;
import com.jianfanjia.cn.designer.tools.TDevice;

import java.util.List;

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

    private static class DesignerPlanViewHolder extends RecyclerViewHolderBase {
        public ImageView itemImgView;

        public DesignerPlanViewHolder(View itemView) {
            super(itemView);
            itemImgView = (ImageView) itemView
                    .findViewById(R.id.list_item_plan_img);
        }
    }

}