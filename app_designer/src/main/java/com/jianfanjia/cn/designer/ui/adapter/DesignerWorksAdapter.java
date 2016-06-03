package com.jianfanjia.cn.designer.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

    private OnItemEditListener listener;

    private static final int HEAD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;

    private boolean isEdit = false;

    public DesignerWorksAdapter(Context context, List<Product> list, OnItemEditListener listener) {
        super(context, list);
        this.listener = listener;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<Product> list) {
        switch (getItemViewType(position)) {
            case CONTENT_TYPE:
                Product product = list.get(position - 1);
                DesignerWorksViewHolder holder = (DesignerWorksViewHolder) viewHolder;
                bindContentView(position - 1, product, holder);
                break;
            case HEAD_TYPE:
                DesignerWorksViewHolderHead holderHead = (DesignerWorksViewHolderHead) viewHolder;
                bindHeadView(holderHead);
                break;
        }

    }

    private void bindHeadView(DesignerWorksViewHolderHead viewHolder) {
        viewHolder.uploadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemAdd();
            }
        });
    }

    private void bindContentView(final int position, Product product, DesignerWorksViewHolder holder) {
        imageShow.displayScreenWidthThumnailImage(context, product.getCover_imageid(), holder
                .itemwWorksView);
        holder.itemXiaoQuText.setText(product.getCell());
        String decType = product.getDec_type();
        String house_type = product.getHouse_type();
        String dec_style = product.getDec_style();
        holder.itemProduceText.setText(product.getHouse_area() + "㎡，" + BusinessCovertUtil.convertDectypeToShow(decType)
                + "，" + BusinessCovertUtil.convertHouseTypeToShow(house_type) + "，" + BusinessCovertUtil
                .convertDecStyleToShow(dec_style) + "风格");

        if (!TextUtils.isEmpty(decType)) {
            holder.itemDecTypeAndTotalPriceText.setText(BusinessCovertUtil.convertDectypeToShow(decType) + "，" + product
                    .getTotal_price() + context.getString(R.string.unit_million));
        } else {
            holder.itemDecTypeAndTotalPriceText.setText(product.getTotal_price() +
                    context.getString(R.string.unit_million));
        }

        String status = product.getAuth_type();
        holder.authStatusText.setVisibility(View.VISIBLE);
        switch (status) {
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
            case ProductBusiness.PRODUCT_AUTH_VIOLATION:
                holder.authStatusText.setText(context.getString(R.string.authorize_violation));
                holder.authStatusText.setBackgroundResource(R.mipmap.icon_auth_fail);
                break;
        }

        if (isEdit) {
            if (!TextUtils.isEmpty(product.getAuth_type()) && !product.getAuth_type().equals(ProductBusiness
                    .PRODUCT_NOT_AUTH)) {
                holder.deleteLayout.setVisibility(View.VISIBLE);
                holder.itemwWorksView.setColorFilter(Color.parseColor("#55000000"));
                holder.itemView.setOnClickListener(null);
            }
        } else {
            holder.deleteLayout.setVisibility(View.GONE);
            holder.itemwWorksView.clearColorFilter();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        holder.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemDelete(position);
            }
        });


    }

    @Override
    public void remove(int position) {
        if (list == null) return;
        list.remove(position);
        notifyItemRemoved(position + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_TYPE;
        } else {
            return CONTENT_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return null == list ? 1 : list.size() + 1;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(int viewType) {
        View view;
        switch (viewType) {
            case CONTENT_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_designer_product,
                        null);
                return new DesignerWorksViewHolder(view);
            case HEAD_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_designer_product_head,
                        null);
                return new DesignerWorksViewHolderHead(view);
        }
        return null;
    }


    static class DesignerWorksViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.list_item_works_img)
        ImageView itemwWorksView;
        @Bind(R.id.list_item_works_xiaoqu_text)
        TextView itemXiaoQuText;
        @Bind(R.id.list_item_works_produce_text)
        TextView itemProduceText;
        @Bind(R.id.list_item_works_dectype_totalprice_text)
        TextView itemDecTypeAndTotalPriceText;
        @Bind(R.id.auth_status)
        TextView authStatusText;

        @Bind(R.id.deleteLayout)
        RelativeLayout deleteLayout;

        public DesignerWorksViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DesignerWorksViewHolderHead extends RecyclerViewHolderBase {
        @Bind(R.id.upload_product_layout)
        FrameLayout uploadLayout;

        public DesignerWorksViewHolderHead(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
