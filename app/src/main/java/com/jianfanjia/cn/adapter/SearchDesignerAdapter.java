package com.jianfanjia.cn.adapter;

import android.support.v4.util.CircularArray;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jianfanjia.cn.adapter.base.BaseLoadingAdapter;
import com.jianfanjia.cn.bean.ProductNew;

/**
 * Description: com.jianfanjia.cn.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-02-20 14:59
 */
public class SearchDesignerAdapter extends BaseLoadingAdapter<ProductNew> {

    public SearchDesignerAdapter(RecyclerView recyclerView, CircularArray<ProductNew> ts) {
        super(recyclerView, ts);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
