package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.ProductBusiness;
import com.jianfanjia.cn.ui.interf.RecyclerViewOnItemClickListener;

/**
 * Name: ProductAdapter
 * User: fengliang
 * Date: 2015-12-11
 * Time: 13:20
 */
public class SearchProductAdapter extends BaseLoadMoreRecycleAdapter<Product> {
    private static final String TAG = SearchProductAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public SearchProductAdapter(Context context, RecyclerView recyclerView, RecyclerViewOnItemClickListener listener) {
        super(context, recyclerView);
        this.listener = listener;
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Product product = mDatas.get(position);
        final ProductViewHolder holder = (ProductViewHolder) viewHolder;
        holder.itemXiaoQuText.setText(product.getCell());
        holder.itemProduceText.setText(ProductBusiness.getProductBaseShowLine1(product));
        holder.tvDecTypeAndTotalPrice.setText(ProductBusiness.getProductBaseShowLine2(product));
        imageShow.displayScreenWidthThumnailImage(context, product.getCover_imageid(), holder
                .itemProductView);
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
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_product,
                null);
        return new ProductViewHolder(view);
    }

    static class ProductViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_product_img)
        ImageView itemProductView;
        @Bind(R.id.list_item_head_img)
        ImageView itemHeadView;
        @Bind(R.id.list_item_xiaoqu_text)
        TextView itemXiaoQuText;
        @Bind(R.id.list_item_produce_text)
        TextView itemProduceText;

        @Bind(R.id.tv_worktype_and_totalprice)
        TextView tvDecTypeAndTotalPrice;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
