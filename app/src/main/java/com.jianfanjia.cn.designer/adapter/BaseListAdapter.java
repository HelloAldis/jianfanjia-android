package com.jianfanjia.cn.designer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jianfanjia.cn.designer.cache.DataManagerNew;
import com.jianfanjia.cn.designer.tools.ImageShow;

import java.util.List;

/**
 * @param <T>
 * @author fengliang
 * @ClassName: BaseListAdapter
 * @Description: adapter公共基类
 * @date 2015-8-18 下午1:31:55
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected Context context;
    protected LayoutInflater layoutInflater;
    protected List<T> list;
    protected DataManagerNew dataManager;
    protected ImageShow imageShow;

    public BaseListAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        imageShow = ImageShow.getImageShow();
        dataManager = DataManagerNew.getInstance();
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

    public void addItem(T t, int index) {
        if (list == null) return;
        list.add(index, t);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView);
    }

    public abstract View initView(int position, View convertView);
}
