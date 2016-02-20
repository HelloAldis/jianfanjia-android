package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.DesignerListInfo;
import com.jianfanjia.cn.bean.Product;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.ListItemClickListener;

import java.util.List;

/**
 * Name: DesignerListAdapter
 * User: fengliang
 * Date: 2015-10-14
 * Time: 14:03
 */
public class DesignerListAdapter extends BaseRecyclerViewAdapter<DesignerListInfo> {
    private static final String TAG = DesignerListAdapter.class.getName();
    private ListItemClickListener listener;

    public DesignerListAdapter(Context context, List<DesignerListInfo> list, ListItemClickListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DesignerListInfo> list) {
        DesignerListInfo info = list.get(position);
        Product product = info.getProduct();
        DesignerListViewHolder designerListViewHolder = (DesignerListViewHolder) viewHolder;
        designerListViewHolder.itemXiaoQuText.setText(product.getCell());
        if (info.getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
            designerListViewHolder.itemAuthView.setVisibility(View.VISIBLE);
        } else {
            designerListViewHolder.itemAuthView.setVisibility(View.GONE);
        }
        String houseType = product.getHouse_type();
        String decStyle = product.getDec_style();
        designerListViewHolder.itemProduceText.setText(product.getHouse_area() + context.getString(R.string.str_sq_unit) + "，" + BusinessManager.convertHouseTypeToShow(houseType) + "，" + BusinessManager.convertDecStyleToShow(decStyle));
        imageShow.displayScreenWidthThumnailImage(context, product.getImages().get(0).getImageid(), designerListViewHolder.itemProductView);
        if (!TextUtils.isEmpty(info.getImageid())) {
            imageShow.displayImageHeadWidthThumnailImage(context, info.getImageid(), designerListViewHolder.itemHeadView);
        } else {
            imageShow.displayLocalImage(Constant.DEFALUT_ADD_PIC, designerListViewHolder.itemHeadView);
        }
        designerListViewHolder.itemProductView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onMaxClick(position);
                }
            }
        });
        designerListViewHolder.itemHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onMinClick(position);
                }
            }
        });
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item_designer_info,
                null);
        return itemView;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DesignerListViewHolder(view);
    }

    private static class DesignerListViewHolder extends RecyclerViewHolderBase {
        public ImageView itemProductView;
        public ImageView itemHeadView;
        public ImageView itemAuthView;
        public TextView itemXiaoQuText;
        public TextView itemProduceText;

        public DesignerListViewHolder(View itemView) {
            super(itemView);
            itemProductView = (ImageView) itemView
                    .findViewById(R.id.list_item_product_img);
            itemHeadView = (ImageView) itemView
                    .findViewById(R.id.list_item_head_img);
            itemAuthView = (ImageView) itemView
                    .findViewById(R.id.list_item_auth);
            itemXiaoQuText = (TextView) itemView
                    .findViewById(R.id.list_item_xiaoqu_text);
            itemProduceText = (TextView) itemView
                    .findViewById(R.id.list_item_produce_text);
        }
    }
}
