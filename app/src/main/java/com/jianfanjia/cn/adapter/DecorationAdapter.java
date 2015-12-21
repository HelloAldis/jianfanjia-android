package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ScreenUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    public void bindView(final RecyclerViewHolderBase viewHolder, final int position, List<BeautyImgInfo> list) {
        BeautyImgInfo info = list.get(position);
        final DecorationViewHolder holder = (DecorationViewHolder) viewHolder;
        List<Img> imgList = info.getImages();
//        holder.itemDecorateView.setImageResource(R.drawable.whitebottom);
        holder.itemDecorateView.setTag(imgList.get(0).getImageid());
        imageShow.displayScreenWidthThumnailImage(context, imgList.get(0).getImageid(), holder.itemDecorateView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imgUrl, View view, Bitmap bitmap) {
                if (holder.itemDecorateView.getTag() != null
                        && holder.itemDecorateView.getTag().equals(imgUrl)) {
                    //此处代码获取屏幕的宽高，实现不同屏幕的适配
                    int width = ScreenUtil.getScreenWidth(context) / 2;
                    int height = width * bitmap.getHeight() / bitmap.getWidth();//高通过宽等比例缩放
                    LogTool.d(TAG, "width=" + width + " height=" + height);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            width, height);
                    holder.itemDecorateView.setLayoutParams(params);
                    holder.itemDecorateView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });


        holder.itemDecorateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.OnItemClick(position);
                }
            }
        });
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
