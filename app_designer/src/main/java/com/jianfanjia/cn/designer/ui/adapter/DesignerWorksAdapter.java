package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.Product;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.designer.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.designer.business.ProductBusiness;
import com.jianfanjia.cn.designer.tools.BusinessCovertUtil;


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
        imageShow.displayScreenWidthThumnailImage(context, product.getImages().get(0).getImageid(), holder
                .itemwWorksView);
        holder.itemXiaoQuText.setText(product.getCell());
        String decType = product.getDec_type();
        String house_type = product.getHouse_type();
        String dec_style = product.getDec_style();
        holder.itemProduceText.setText(product.getHouse_area() + "㎡，" + BusinessCovertUtil.convertDectypeToShow(decType)
                + "，" + BusinessCovertUtil.convertHouseTypeToShow(house_type) + "，" + BusinessCovertUtil
                .convertDecStyleToShow(dec_style) + "风格");

        String status = product.getAuth_type();
        holder.authStatusText.setVisibility(View.VISIBLE);
        switch (status){
            case ProductBusiness.PRODUCT_AUTH_FAILURE:
                holder.authStatusText.setText(context.getString(R.string.authorize_fail));
                holder.authStatusText.setBackgroundResource(R.mipmap.icon_auth_fail);
                break;
            case ProductBusiness.PRODUCT_NOT_AUTH:
                holder.authStatusText.setText(context.getString(R.string.auth_going));
                holder.authStatusText.setBackgroundResource(R.mipmap.icon_auth_going);
                break;
            case ProductBusiness.PRODUCT_AUTH_SUCCESS:
                holder.authStatusText.setText(context.getString(R.string.auth_success));
                holder.authStatusText.setBackgroundResource(R.mipmap.icon_auth_success);
                break;
            default:
                holder.authStatusText.setVisibility(View.GONE);
                break;
        }

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
        View view = layoutInflater.inflate(R.layout.list_item_designer_product,
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
        @Bind(R.id.auth_status)
        TextView authStatusText;

        public DesignerWorksViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
