package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Url;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class ShowPicPagerAdapter extends PagerAdapter {
    private List<String> images;
    private LayoutInflater inflater;
    private ViewPagerClickListener viewPagerClickListener;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ShowPicPagerAdapter(Context context, List<String> imageList,
                               ViewPagerClickListener viewPagerClickListener) {
        this.images = imageList;
        this.inflater = LayoutInflater.from(context);
        this.viewPagerClickListener = viewPagerClickListener;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
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
        ImageView imageView = (ImageView) view.findViewById(R.id.image_item);
        if (!images.get(position).contains(Constant.DEFALUT_PIC_HEAD)) {
            imageLoader.displayImage(Url.GET_IMAGE + images.get(position),
                    imageView, options);
        } else {
            imageLoader.displayImage(images.get(position),
                    imageView, options);
        }
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (viewPagerClickListener != null) {
                    viewPagerClickListener.onClickItem(position);
                }
            }
        });
        container.addView(view, 0);
        return view;
    }

}
