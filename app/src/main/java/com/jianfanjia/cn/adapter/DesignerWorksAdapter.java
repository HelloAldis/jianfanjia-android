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
import com.jianfanjia.cn.interf.OnItemClickListener;

import java.util.List;

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
        imageShow.displayScreenWidthThumnailImage(context, product.getImages().get(0).getImageid(), holder.itemwWorksView);
        holder.itemXiaoQuText.setText(product.getCell());
        String house_type = product.getHouse_type();
        String dec_style = product.getDec_style();
        holder.itemProduceText.setText(product.getHouse_area() + "㎡，" + BusinessManager.convertHouseTypeToShow(house_type) + "，" + BusinessManager.convertDecStyleToShow(dec_style) + "风格");
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


    private static class DesignerWorksViewHolder extends RecyclerViewHolderBase {
        public ImageView itemwWorksView;
        public TextView itemXiaoQuText;
        public TextView itemProduceText;

        public DesignerWorksViewHolder(View itemView) {
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
