package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

import java.util.List;

/**
 * Name: DecorationImgAdapter
 * User: fengliang
 * Date: 2015-12-16
 * Time: 17:56
 */
public class DecorationImgAdapter extends BaseRecyclerViewAdapter<BeautyImgInfo> {
    private static final String TAG = DecorationAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public DecorationImgAdapter(Context context, List<BeautyImgInfo> list, RecyclerViewOnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<BeautyImgInfo> list) {
        BeautyImgInfo beautyImgInfo = list.get(position);
        final DecorationViewHolder holder = (DecorationViewHolder) viewHolder;
        if (!beautyImgInfo.is_deleted()) {
            holder.itemDecorateView.setVisibility(View.VISIBLE);
            holder.itemNoDecorateView.setVisibility(View.GONE);
            imageShow.displayHalfScreenWidthThumnailImage(context, beautyImgInfo.getImages().get(0).getImageid(), holder.itemDecorateView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnItemClick(v, holder.getLayoutPosition());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != listener) {
                        listener.OnLongItemClick(v, holder.getLayoutPosition());
                    }
                    return true;
                }
            });
        } else {
            holder.itemDecorateView.setVisibility(View.GONE);
            holder.itemNoDecorateView.setVisibility(View.VISIBLE);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != listener) {
                        listener.OnLongItemClick(v, holder.getLayoutPosition());
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_decoration,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DecorationViewHolder(view);
    }

    private static class DecorationViewHolder extends RecyclerViewHolderBase {
        public ImageView itemDecorateView;
        public ImageView itemNoDecorateView;

        public DecorationViewHolder(View itemView) {
            super(itemView);
            itemDecorateView = (ImageView) itemView
                    .findViewById(R.id.list_item_decorate_img);
            itemNoDecorateView = (ImageView) itemView
                    .findViewById(R.id.list_item_no_decorate_img);
        }
    }
}
