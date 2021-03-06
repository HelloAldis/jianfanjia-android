package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.ProductBusiness;
import com.jianfanjia.cn.ui.interf.RecyclerViewOnItemClickListener;

/**
 * Name: CollectProductAdapter
 * User: fengliang
 * Date: 2016-02-24
 * Time: 10:08
 */
public class CollectProductAdapter extends BaseRecyclerViewAdapter<Product> {
    private static final String TAG = CollectProductAdapter.class.getName();
    private RecyclerViewOnItemClickListener listener;

    public CollectProductAdapter(Context context, List<Product> list, RecyclerViewOnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<Product> list) {
        final ProductViewHolder holder = (ProductViewHolder) viewHolder;
        Product product = list.get(position);
        if (product.is_deleted()) {
            holder.llProductBaseInfo.setVisibility(View.GONE);
            holder.llDeleteTip.setVisibility(View.VISIBLE);
            holder.llDeleteIcon.setVisibility(View.VISIBLE);

            holder.itemProductView.setImageResource(R.mipmap.icon_default_pic);
            holder.itemView.setOnClickListener(null);
            holder.llDeleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnViewClick(position);
                    }
                }
            });
        } else {
            holder.llProductBaseInfo.setVisibility(View.VISIBLE);
            holder.llDeleteTip.setVisibility(View.GONE);
            holder.llDeleteIcon.setVisibility(View.GONE);

            holder.itemXiaoQuText.setText(product.getCell());
            holder.itemProduceText.setText(ProductBusiness.getProductBaseShowLine1(product));
            holder.tvDecTypeAndTotalPrice.setText(ProductBusiness.getProductBaseShowLine2(product));
            imageShow.displayScreenWidthThumnailImage(context, product.getCover_imageid(), holder
                    .itemProductView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.OnItemClick(v, holder.getLayoutPosition());
                    }
                }
            });
        }


    }

    @Override
    public RecyclerViewHolderBase createViewHolder(int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_collect_product,
                null);
        return new ProductViewHolder(view);
    }

    static class ProductViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.list_item_product_img)
        ImageView itemProductView;
        @Bind(R.id.list_item_xiaoqu_text)
        TextView itemXiaoQuText;
        @Bind(R.id.list_item_produce_text)
        TextView itemProduceText;

        @Bind(R.id.tv_worktype_and_totalprice)
        TextView tvDecTypeAndTotalPrice;

        @Bind(R.id.ll_delete_icon)
        LinearLayout llDeleteIcon;

        @Bind(R.id.ll_delete_tip)
        LinearLayout llDeleteTip;

        @Bind(R.id.ll_product_baseinfo)
        LinearLayout llProductBaseInfo;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
