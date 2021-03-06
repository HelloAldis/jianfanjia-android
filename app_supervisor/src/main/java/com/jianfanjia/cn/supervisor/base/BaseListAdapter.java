package com.jianfanjia.cn.supervisor.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import com.jianfanjia.cn.supervisor.business.DataManagerNew;
import com.jianfanjia.cn.tools.ImageShow;

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
    protected DataManagerNew dataManager;

    public BaseListAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        dataManager = DataManagerNew.getInstance();
        imageShow = ImageShow.getImageShow();
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
        if (list == null) return;
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
