package com.jianfanjia.cn.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jianfanjia.cn.cache.DataManagerNew;
import com.jianfanjia.cn.tools.ImageShow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

/**
 * Description:adapter公共基类
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 15:38
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater layoutInflater;
    protected List<T> list;
    protected ImageShow imageShow;
//    protected ImageLoader imageLoader;
    protected DisplayImageOptions options;
    protected DataManagerNew dataManager;

    public BaseListAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        dataManager = DataManagerNew.getInstance();
        imageShow = ImageShow.getImageShow();
       /* imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(false)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();*/
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(T t) {
        list.add(t);
        notifyDataSetChanged();
    }

    public void addAll(List<T> l) {
        list.addAll(l);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }

    public abstract View initView(int position, View convertView);

}
