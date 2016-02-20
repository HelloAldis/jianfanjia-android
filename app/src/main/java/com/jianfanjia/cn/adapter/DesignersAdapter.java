package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.interf.ListItemClickListener;

import java.util.List;

/**
 * Name: DesignersAdapter
 * User: fengliang
 * Date: 2016-02-20
 * Time: 11:33
 */
public class DesignersAdapter extends BaseRecyclerViewAdapter<DesignerInfo> {
    private static final String TAG = DesignersAdapter.class.getName();
    private ListItemClickListener listener;

    public DesignersAdapter(Context context, List<DesignerInfo> list, ListItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DesignerInfo> list) {

    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.designer_list_item,
                null);
        return itemView;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DesignerViewHolder(view);
    }

    private static class DesignerViewHolder extends RecyclerViewHolderBase {
        public ImageView itemProductView;
        public ImageView itemHeadView;
        public ImageView itemAuthView;
        public TextView itemNameText;
        public TextView itemProduceText;

        public DesignerViewHolder(View itemView) {
            super(itemView);
            itemProductView = (ImageView) itemView
                    .findViewById(R.id.list_item_product_img);
            itemHeadView = (ImageView) itemView
                    .findViewById(R.id.list_item_head_img);
            itemAuthView = (ImageView) itemView
                    .findViewById(R.id.list_item_auth);
            itemNameText = (TextView) itemView
                    .findViewById(R.id.list_item_xiaoqu_text);
            itemProduceText = (TextView) itemView
                    .findViewById(R.id.list_item_produce_text);
        }
    }
}
