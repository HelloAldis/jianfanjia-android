package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.DecorationItemInfo;

import java.util.List;

/**
 * Name: DecorationAdapter
 * User: fengliang
 * Date: 2015-12-07
 * Time: 10:33
 */
public class DecorationAdapter extends BaseRecyclerViewAdapter<DecorationItemInfo> {

    public DecorationAdapter(Context context, List<DecorationItemInfo> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<DecorationItemInfo> list) {
        DecorationItemInfo info = list.get(position);
        DecorationViewHolder holder = (DecorationViewHolder) viewHolder;
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_decoration,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DecorationViewHolder(view);
    }

    private static class DecorationViewHolder extends RecyclerViewHolderBase {
        public ImageView itemDecorateView;

        public DecorationViewHolder(View itemView) {
            super(itemView);
            itemDecorateView = (ImageView) itemView
                    .findViewById(R.id.list_item_decorate_img);
        }
    }
}
