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
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<Product> list) {
        Product product = list.get(position);
        final ProductViewHolder holder = (ProductViewHolder) viewHolder;
        holder.itemXiaoQuText.setText(product.getCell());
        String decType = product.getDec_type();
        String houseType = product.getHouse_type();
        String decStyle = product.getDec_style();
        holder.itemProduceText.setText(product.getHouse_area() + "㎡，" + BusinessManager.convertDectypeToShow(decType)
                + "，" + BusinessManager.convertHouseTypeToShow(houseType) + "，" + BusinessManager
                .convertDecStyleToShow(decStyle) + "风格");
        imageShow.displayScreenWidthThumnailImage(context, product.getImages().get(0).getImageid(), holder
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

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_collect_product,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new ProductViewHolder(view);
    }


    private static class ProductViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_product_img)
        ImageView itemProductView;
        @Bind(R.id.list_item_xiaoqu_text)
        TextView itemXiaoQuText;
        @Bind(R.id.list_item_produce_text)
        TextView itemProduceText;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
