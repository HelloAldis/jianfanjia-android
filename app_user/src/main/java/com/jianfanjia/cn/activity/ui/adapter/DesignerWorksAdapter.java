package com.jianfanjia.cn.activity.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.jianfanjia.cn.activity.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.activity.base.RecyclerViewHolderBase;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.tools.BusinessCovertUtil;
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
        String decType = product.getDec_type();
        String house_type = product.getHouse_type();
        String dec_style = product.getDec_style();
        holder.itemProduceText.setText(product.getHouse_area() + "㎡，" + BusinessCovertUtil.convertDectypeToShow(decType)
                + "，" + BusinessCovertUtil.convertHouseTypeToShow(house_type) + "，" + BusinessCovertUtil
                .convertDecStyleToShow(dec_style) + "风格");
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

        public DesignerWorksViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
