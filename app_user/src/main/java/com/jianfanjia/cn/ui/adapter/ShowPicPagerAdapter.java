package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.tools.ImageShow;
import com.jianfanjia.cn.ui.interf.ViewPagerClickListener;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowPicPagerAdapter extends PagerAdapter {
    private List<String> images;
    private LayoutInflater inflater;
    private ImageShow imageShow;
    Context context;
    View.OnLongClickListener onLongClickListener;
    ViewPagerClickListener mViewPagerClickListener;
    public ShowPicPagerAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.images = imageList;
        this.inflater = LayoutInflater.from(context);
        imageShow = ImageShow.getImageShow();
    }

    public void setViewPagerClickListener(ViewPagerClickListener viewPagerClickListener) {
        mViewPagerClickListener = viewPagerClickListener;
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
        final PhotoView imageView = (PhotoView) view.findViewById(R.id.image_item);
        if (!images.get(position).contains(Constant.DEFALUT_PIC_HEAD)) {
            imageShow.displayScreenWidthThumnailImage(context, images.get(position), imageView);
        } else {
            imageShow.displayLocalImage(images.get(position), imageView);
        }
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if(mViewPagerClickListener != null){
                    mViewPagerClickListener.onClickItem(position);
                }
            }

            @Override
            public void onOutsidePhotoTap() {

            }
        });
        container.addView(view, 0);
        return view;
    }




}
