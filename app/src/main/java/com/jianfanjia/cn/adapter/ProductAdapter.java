package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

import java.util.List;

/**
 * Name: ProductAdapter
 * User: fengliang
 * Date: 2015-12-11
 * Time: 13:20
 */
public class ProductAdapter extends BaseRecyclerViewAdapter<Product> {
    private static final String TAG = ProductAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public ProductAdapter(Context context, List<Product> list, RecyclerViewOnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Product> list) {
        Product product = list.get(position);
        final ProductViewHolder holder = (ProductViewHolder) viewHolder;
        if (!product.is_deleted()) {
            holder.imgLayout.setVisibility(View.VISIBLE);
            holder.noImgLayout.setVisibility(View.GONE);
            holder.itemXiaoQuText.setText(product.getCell());
            String houseType = product.getHouse_type();
            String decStyle = product.getDec_style();
            holder.itemProduceText.setText(product.getHouse_area() + "㎡，" + BusinessManager.convertHouseTypeToShow(houseType) + "，" + BusinessManager.convertDecStyleToShow(decStyle));
            imageShow.displayScreenWidthThumnailImage(context, product.getImages().get(0).getImageid(), holder.itemProductView);
            imageShow.displayImageHeadWidthThumnailImage(context, product.getDesigner().getImageid(), holder.itemHeadView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnItemClick(v, holder.getLayoutPosition());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != listener) {
                        listener.OnLongItemClick(v, holder.getLayoutPosition());
                    }
                    return true;
                }
            });
            holder.itemHeadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnViewClick(holder.getLayoutPosition());
                    }
                }
            });
        } else {
            holder.imgLayout.setVisibility(View.GONE);
            holder.noImgLayout.setVisibility(View.VISIBLE);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (null != listener) {
                        listener.OnLongItemClick(v, holder.getLayoutPosition());
                    }
                    return true;
                }
            });
        }

    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_product,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new ProductViewHolder(view);
    }


    private static class ProductViewHolder extends RecyclerViewHolderBase {
        public RelativeLayout imgLayout = null;
        public RelativeLayout noImgLayout = null;
        public ImageView itemProductView;
        public ImageView itemHeadView;
        public TextView itemXiaoQuText;
        public TextView itemProduceText;

        public ProductViewHolder(View itemView) {
            super(itemView);
            imgLayout = (RelativeLayout) itemView.findViewById(R.id.imgLayout);
            noImgLayout = (RelativeLayout) itemView.findViewById(R.id.noImgLayout);
            itemProductView = (ImageView) itemView
                    .findViewById(R.id.list_item_product_img);
            itemHeadView = (ImageView) itemView
                    .findViewById(R.id.list_item_head_img);
            itemXiaoQuText = (TextView) itemView
                    .findViewById(R.id.list_item_xiaoqu_text);
            itemProduceText = (TextView) itemView
                    .findViewById(R.id.list_item_produce_text);
        }
    }
}
