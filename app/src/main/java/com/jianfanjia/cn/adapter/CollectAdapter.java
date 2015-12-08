package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.Product;

import java.util.List;

/**
 * Name: CollectAdapter
 * User: fengliang
 * Date: 2015-12-08
 * Time: 16:47
 */
public class CollectAdapter extends BaseRecyclerViewAdapter<Product> {

    public CollectAdapter(Context context, List<Product> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Product> list) {
        Product info = list.get(position);
        CollectViewHolder holder = (CollectViewHolder) viewHolder;
        holder.itemXiaoQuText.setText(info.getDescription());
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_designer_works,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new CollectViewHolder(view);
    }

    private static class CollectViewHolder extends RecyclerViewHolderBase {
        public ImageView itemwWorksView;
        public TextView itemXiaoQuText;
        public TextView itemProduceText;

        public CollectViewHolder(View itemView) {
            super(itemView);
            itemwWorksView = (ImageView) itemView
                    .findViewById(R.id.list_item_works_img);
            itemXiaoQuText = (TextView) itemView
                    .findViewById(R.id.list_item_works_xiaoqu_text);
            itemProduceText = (TextView) itemView
                    .findViewById(R.id.list_item_works_produce_text);
        }
    }
}
