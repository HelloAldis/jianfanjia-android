package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.BeautyImgInfo;
import com.jianfanjia.cn.bean.Img;
import com.jianfanjia.cn.interf.OnItemClickListener;
import com.jianfanjia.cn.tools.ScreenUtil;

import java.util.List;

/**
 * Name: DecorationAdapter
 * User: fengliang
 * Date: 2015-12-07
 * Time: 10:33
 */
public class DecorationAdapter extends BaseRecyclerViewAdapter<BeautyImgInfo> {
    private static final String TAG = DecorationAdapter.class.getName();
    private OnItemClickListener listener;

    public DecorationAdapter(Context context, List<BeautyImgInfo> list, OnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(final RecyclerViewHolderBase viewHolder,final int position, final List<BeautyImgInfo> list) {
        BeautyImgInfo info = list.get(position);
        final DecorationViewHolder holder = (DecorationViewHolder) viewHolder;
        List<Img> imgList = info.getImages();
        if (null != imgList && imgList.size() > 0) {
            Img img = info.getImages().get(0);
            int width = ScreenUtil.getScreenWidth(context) / 2;
            int height = width * img.getHeight() / img.getWidth();//高通过宽等比例缩放
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    width, height);
            holder.itemDecorateView.setLayoutParams(params);
            imageShow.displayScreenWidthThumnailImage(context, img.getImageid(), holder.itemDecorateView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnItemClick(position);
                    }
                }
            });
        } else {
            int width = ScreenUtil.getScreenWidth(context) / 2;
            int height = ScreenUtil.getScreenWidth(context) / 2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    width, height);
            holder.itemDecorateView.setLayoutParams(params);
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

    private static class DecorationViewHolder extends RecyclerViewHolderBase {
        public ImageView itemDecorateView;

        public DecorationViewHolder(View itemView) {
            super(itemView);
            itemDecorateView = (ImageView) itemView
                    .findViewById(R.id.list_item_decorate_img);
        }
    }
}
