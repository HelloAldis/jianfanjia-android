package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.Img;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.TDevice;

/**
 * Name: DecorationAdapter
 * User: fengliang
 * Date: 2015-12-07
 * Time: 10:33
 */
public class SearchDecorationImgAdapter extends BaseRecycleAdapter<BeautyImgInfo> {
    private static final String TAG = SearchDecorationImgAdapter.class.getName();
    private OnItemClickListener listener;

    public SearchDecorationImgAdapter(Context context, RecyclerView recyclerView, OnItemClickListener listener) {
        super(context, recyclerView);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_decoration,
                null);
        return new DecorationViewHolder(view);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        BeautyImgInfo info = mDatas.get(position);
        final DecorationViewHolder holder = (DecorationViewHolder) viewHolder;
        List<Img> imgList = info.getImages();
        if (null != imgList && imgList.size() > 0) {
            Img img = info.getImages().get(0);
            int width = (int) TDevice.getScreenWidth() / 2;
            int height = width * img.getHeight() / img.getWidth();//高通过宽等比例缩放
            CardView.LayoutParams layoutParams = (CardView.LayoutParams) holder.itemDecorateView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            holder.itemDecorateView.setLayoutParams(layoutParams);
            imageShow.displayHalfScreenWidthThumnailImage(context, img.getImageid(), holder.itemDecorateView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnItemClick(position);
                    }
                }
            });
        } else {
            int width = (int) TDevice.getScreenWidth() / 2;
            int height = width;
            CardView.LayoutParams layoutParams = (CardView.LayoutParams) holder.itemDecorateView.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            holder.itemDecorateView.setLayoutParams(layoutParams);
            holder.itemDecorateView.setBackgroundResource(R.mipmap.pix_default);
        }
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
