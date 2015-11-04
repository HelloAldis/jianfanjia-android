package com.jianfanjia.cn.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.cache.DataManagerNew;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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
    protected ImageLoader imageLoader;
    protected DisplayImageOptions options;
    protected DataManagerNew dataManager;

    public BaseListAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
        dataManager = DataManagerNew.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pix_default)
                .showImageForEmptyUri(R.mipmap.pix_default)
                .showImageOnFail(R.mipmap.pix_default).cacheInMemory(false)
                .cacheOnDisk(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
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

    protected String getHouseType(String houseType) {
        String str = null;
        if (houseType.equals("0")) {
            str = "一居";
        } else if (houseType.equals("1")) {
            str = "二居";
        } else if (houseType.equals("2")) {
            str = "三居";
        } else if (houseType.equals("3")) {
            str = "四居";
        } else if (houseType.equals("4")) {
            str = "复式";
        } else if (houseType.equals("5")) {
            str = "别墅";
        } else if (houseType.equals("6")) {
            str = "LOFT";
        } else if (houseType.equals("7")) {
            str = "其他";
        }
        return str;
    }

    protected String getDecStyle(String decStyle) {
        String str = null;
        if (decStyle.equals("0")) {
            str = "欧式";
        } else if (decStyle.equals("1")) {
            str = "中式";
        } else if (decStyle.equals("2")) {
            str = "现代";
        } else if (decStyle.equals("3")) {
            str = "地中海";
        } else if (decStyle.equals("4")) {
            str = "美式";
        } else if (decStyle.equals("5")) {
            str = "东南亚";
        } else if (decStyle.equals("6")) {
            str = "田园";
        }
        return str;
    }
}
