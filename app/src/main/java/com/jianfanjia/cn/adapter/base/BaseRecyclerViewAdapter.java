package com.jianfanjia.cn.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.tools.ImageShow;

import java.util.List;

/**
 * Name: BaseRecyclerViewAdapter
 * User: fengliang
 * Date: 2015-11-24
 * Time: 13:32
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolderBase> {
    protected Context context;
    protected List<T> list;
    protected LayoutInflater layoutInflater;
    protected ImageShow imageShow;

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
        imageShow = ImageShow.getImageShow();
    }


    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderBase viewHolder, int position) {
        bindView(viewHolder, position, list);
    }

    @Override
    public RecyclerViewHolderBase onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = createView(viewGroup, position);
        RecyclerViewHolderBase holder = createViewHolder(view);
        return holder;
    }

    public abstract void bindView(RecyclerViewHolderBase viewHolder, int position, List<T> list);

    public abstract View createView(ViewGroup viewGroup, int position);

    public abstract RecyclerViewHolderBase createViewHolder(View view);
}
