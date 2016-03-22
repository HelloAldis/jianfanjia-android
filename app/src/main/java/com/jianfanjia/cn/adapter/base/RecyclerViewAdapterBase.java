package com.jianfanjia.cn.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.jianfanjia.cn.view.baseview.ViewWrapper;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 16:53
 */
public abstract class RecyclerViewAdapterBase<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected List<T> items = new ArrayList<>();

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    public void addItem(List<T> ts) {
        clearItems();
        for (int i = 0; i < ts.size(); i++) {
            add(i, ts.get(i));
        }
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    private void add(int position, T t) {
        items.add(t);
        notifyItemInserted(position);
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }
    // additional methods to manipulate the items

}
