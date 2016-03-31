package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.jianfanjia.api.model.BeautifulImage;
import com.jianfanjia.api.model.BeautifulImageDetail;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;
import com.jianfanjia.common.tool.TDevice;

/**
 * Name: DecorationImgAdapter
 * User: fengliang
 * Date: 2015-12-16
 * Time: 17:56
 */
public class DecorationImgAdapter extends BaseRecyclerViewAdapter<BeautifulImage> {
    private static final String TAG = DecorationAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public DecorationImgAdapter(Context context, List<BeautifulImage> list, RecyclerViewOnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<BeautifulImage> list) {
        BeautifulImage beautyImgInfo = list.get(position);
        final DecorationViewHolder holder = (DecorationViewHolder) viewHolder;
        if (!beautyImgInfo.is_deleted()) {
            holder.itemDecorateView.setVisibility(View.VISIBLE);
            holder.itemNoDecorateView.setVisibility(View.GONE);
            BeautifulImageDetail img = beautyImgInfo.getImages().get(0);
            int width = (int) TDevice.getScreenWidth() / 2;
            int height = width * img.getHeight() / img.getWidth();//高通过宽等比例缩放
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.itemDecorateView.getLayoutParams();
            params.width = width;
            params.height = height;
            holder.itemDecorateView.setLayoutParams(params);
            holder.itemNoDecorateView.setLayoutParams(params);
            imageShow.displayScreenWidthThumnailImage(context, img.getImageid(), holder.itemDecorateView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnItemClick(v, holder.getLayoutPosition());
                    }
                }
            });
        } else {
            holder.itemDecorateView.setVisibility(View.GONE);
            holder.itemNoDecorateView.setVisibility(View.VISIBLE);
            holder.itemNoDecorateView.setImageResource(R.mipmap.pix_default);
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

    static class DecorationViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_decorate_img)
        ImageView itemDecorateView;
        @Bind(R.id.list_item_no_decorate_img)
        ImageView itemNoDecorateView;

        public DecorationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
