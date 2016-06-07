package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.api.model.BeautifulImage;
import com.jianfanjia.api.model.BeautifulImageDetail;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.ui.interf.OnItemClickListener;
import com.jianfanjia.common.tool.TDevice;

/**
 * Name: DecorationAdapter
 * User: fengliang
 * Date: 2015-12-07
 * Time: 10:33
 */
public class DecorationAdapter extends BaseRecyclerViewAdapter<BeautifulImage> {
    private static final String TAG = DecorationAdapter.class.getName();
    private OnItemClickListener listener;

    public DecorationAdapter(Context context, List<BeautifulImage> list, OnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    public List<BeautifulImage> getBeautyImgList() {
        return list;
    }

    @Override
    public void bindView(final RecyclerViewHolderBase viewHolder, final int position, final List<BeautifulImage> list) {
        BeautifulImage info = list.get(position);
        final DecorationViewHolder holder = (DecorationViewHolder) viewHolder;
        List<BeautifulImageDetail> imgList = info.getImages();
        if (null != imgList && imgList.size() > 0) {
            BeautifulImageDetail img = info.getImages().get(0);
            int width = (int) TDevice.getScreenWidth() / 2;
            int height = width * img.getHeight() / img.getWidth();//高通过宽等比例缩放
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.itemDecorateView
                    .getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            holder.itemDecorateView.setLayoutParams(layoutParams);
            imageShow.displayHalfScreenWidthThumnailImage(context, img.getImageid(), holder.itemDecorateView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnItemClick(holder.getLayoutPosition());
                    }
                }
            });
        } else {
            int width = (int) TDevice.getScreenWidth() / 2;
            int height = width;
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.itemDecorateView
                    .getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            holder.itemDecorateView.setLayoutParams(layoutParams);
            holder.itemDecorateView.setBackgroundResource(R.mipmap.pix_default);
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

        public DecorationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
