package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

/**
 * Name: ProductAdapter
 * User: fengliang
 * Date: 2015-12-11
 * Time: 13:20
 */
public class SearchProductAdapter extends BaseRecycleAdapter<Product> {
    private static final String TAG = SearchProductAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public SearchProductAdapter(Context context, RecyclerView recyclerView,RecyclerViewOnItemClickListener listener) {
        super(context, recyclerView);
        this.listener = listener;
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Product product = mDatas.get(position);
        final ProductViewHolder holder = (ProductViewHolder) viewHolder;
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
        holder.itemHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.OnViewClick(holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.list_item_product,
                null);
        return new ProductViewHolder(view);
    }

    private static class ProductViewHolder extends RecyclerViewHolderBase {
        public ImageView itemProductView;
        public ImageView itemHeadView;
        public TextView itemXiaoQuText;
        public TextView itemProduceText;

        public ProductViewHolder(View itemView) {
            super(itemView);
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
