package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.Product;

import java.util.List;

/**
 * Name: ProductAdapter
 * User: fengliang
 * Date: 2015-12-11
 * Time: 13:20
 */
public class ProductAdapter extends BaseRecyclerViewAdapter<Product> {

    public ProductAdapter(Context context, List<Product> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Product> list) {

    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return null;
    }
}
