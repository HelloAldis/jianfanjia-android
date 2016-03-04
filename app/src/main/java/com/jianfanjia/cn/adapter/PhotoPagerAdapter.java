package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.interf.ViewPagerClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by donglua on 15/6/21.
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private List<String> paths = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ViewPagerClickListener viewPagerClickListener;

    public PhotoPagerAdapter(Context context, List<String> paths, ViewPagerClickListener viewPagerClickListener) {
        this.mContext = context;
        this.paths = paths;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.viewPagerClickListener = viewPagerClickListener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.picker_item_pager, container, false);

        PhotoView imageView = (PhotoView) itemView.findViewById(R.id.iv_pager);

        final String path = paths.get(position);
        final Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }

        ImageLoader.getInstance().displayImage(uri.toString(), imageView);

        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

                if (viewPagerClickListener != null) {
                    viewPagerClickListener.onClickItem(position);
                }
            }
        });

        container.addView(itemView);

        return itemView;
    }


    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
