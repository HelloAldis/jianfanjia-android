package com.jianfanjia.cn.activity.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.activity.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.activity.business.ProductBusiness;
import com.jianfanjia.cn.activity.ui.interf.OnItemClickListener;

/**
 * Name: DesignerWorksAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 13:44
 */
public class DesignerWorksAdapter extends BaseRecyclerViewAdapter<Product> {
    private OnItemClickListener listener;

    public DesignerWorksAdapter(Context context, List<Product> list, OnItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<Product> list) {
        Product product = list.get(position);
        DesignerWorksViewHolder holder = (DesignerWorksViewHolder) viewHolder;
        imageShow.displayScreenWidthThumnailImage(context, product.getCover_imageid(), holder
                .itemwWorksView);
        holder.itemXiaoQuText.setText(product.getCell());
        holder.itemProduceText.setText(ProductBusiness.getProductBaseShowLine1(product));
        holder.tvDecTypeAndTotalPrice.setText(ProductBusiness.getProductBaseShowLine2(product));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.OnItemClick(position);
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_designer_works,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DesignerWorksViewHolder(view);
    }


    static class DesignerWorksViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_works_img)
        ImageView itemwWorksView;
        @Bind(R.id.list_item_works_xiaoqu_text)
        TextView itemXiaoQuText;
        @Bind(R.id.list_item_works_produce_text)
        TextView itemProduceText;

        @Bind(R.id.tv_worktype_and_totalprice)
        TextView tvDecTypeAndTotalPrice;

        public DesignerWorksViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
