package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.jianfanjia.api.model.BeautifulImage;
import com.jianfanjia.api.model.BeautifulImageDetail;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.ui.interf.ViewPagerClickListener;
import com.jianfanjia.cn.tools.ImageShow;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Name: PreImgPagerAdapter
 * User: fengliang
 * Date: 2016-02-18
 * Time: 16:52
 */
public class PreImgPagerAdapter extends PagerAdapter {
    private Context context;
    private List<BeautifulImage> beautyImagesList;
    private LayoutInflater inflater;
    private ViewPagerClickListener viewPagerClickListener;
    private ImageShow imageShow;

    public PreImgPagerAdapter(Context context, List<BeautifulImage> beautyImagesList,
                              ViewPagerClickListener viewPagerClickListener) {
        this.context = context;
        this.beautyImagesList = beautyImagesList;
        this.inflater = LayoutInflater.from(context);
        this.viewPagerClickListener = viewPagerClickListener;
        imageShow = ImageShow.getImageShow();
    }

    public List<BeautifulImage> getBeautyImagesList() {
        return beautyImagesList;
    }

    public void addItem(List<BeautifulImage> list) {
        beautyImagesList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return beautyImagesList.size();
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
        BeautifulImage info = beautyImagesList.get(position);
        List<BeautifulImageDetail> images = info.getImages();
        View view = inflater.inflate(R.layout.viewpager_item_show_img, null);
        PhotoView imageView = (PhotoView) view.findViewById(R.id.image_item);
        if (!images.get(0).getImageid().contains(Constant.DEFALUT_PIC_HEAD)) {
            imageShow.displayScreenWidthThumnailImage(context, images.get(0).getImageid(), imageView);
        } else {
            imageShow.displayLocalImage(images.get(0).getImageid(), imageView);
        }

        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (viewPagerClickListener != null) {
                    viewPagerClickListener.onClickItem(position);
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