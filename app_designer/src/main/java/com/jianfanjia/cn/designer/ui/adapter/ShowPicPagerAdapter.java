package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.ui.interf.ViewPagerClickListener;
import com.jianfanjia.cn.designer.tools.ImageShow;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowPicPagerAdapter extends PagerAdapter {
    private List<String> images;
    private LayoutInflater inflater;
    private ViewPagerClickListener viewPagerClickListener;
    private ImageShow imageShow;
    Context context;
    View.OnLongClickListener onLongClickListener;

    public ShowPicPagerAdapter(Context context, List<String> imageList,
                               ViewPagerClickListener viewPagerClickListener) {
        this.context = context;
        this.images = imageList;
        this.inflater = LayoutInflater.from(context);
        this.viewPagerClickListener = viewPagerClickListener;
        imageShow = ImageShow.getImageShow();
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void deleteItem(int position) {
        if (position > -1 && position < images.size()) {
            images.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.i(this.getClass().getName(), images.get(position));
        View view = inflater.inflate(R.layout.viewpager_item_show_pic, null);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.image_item);
        if (!images.get(position).contains(Constant.DEFALUT_PIC_HEAD)) {
            imageShow.displayScreenWidthThumnailImage(context,images.get(position),imageView);
        } else {
            imageShow.displayLocalImage(images.get(position),imageView);
        }
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (viewPagerClickListener != null) {
                    viewPagerClickListener.onClickItem(position);
                }
            }
        });
        if (onLongClickListener != null) {
            imageView.setOnLongClickListener(onLongClickListener);
        }
        container.addView(view, 0);
        return view;
    }

}
