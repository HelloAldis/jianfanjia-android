package com.jianfanjia.cn.activity.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2015-10-19 16:53
 */
public abstract class RecyclerViewAdapterBase<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> items = new ArrayList<>();

    @Override
    public int getItemCount() {
        return items.size();
    }


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
