package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.config.Url_New;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Name: PreviewAdapter
 * User: fengliang
 * Date: 2015-10-29
 * Time: 09:16
 */
public class PreviewAdapter extends PagerAdapter {
    private static final String TAG = "PreviewAdapter";
    private Context context;
    private List<String> mList;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    public PreviewAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pic_default)
                .showImageForEmptyUri(R.mipmap.pic_default)
                .showImageOnFail(R.mipmap.pic_default).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }


    public List<String> getList() {
        return mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.list_item_preview_view_item, container, false);
        ImageView imageView = (ImageView) view
                .findViewById(R.id.list_item_plan_img);
        String imgid = mList.get(position);
        imageLoader.displayImage(Url_New.GET_IMAGE + imgid, imageView, options);
        container.addView(view, 0);
        return view;
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }
}