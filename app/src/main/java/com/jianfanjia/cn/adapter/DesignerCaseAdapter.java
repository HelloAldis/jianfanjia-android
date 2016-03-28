package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

/**
 * Name: DesignerCaseAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 15:14
 */
public class DesignerCaseAdapter extends BaseRecyclerViewAdapter<ProductImageInfo> {
    private RecyclerViewOnItemClickListener listener;
    private Product designerCaseInfo;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private int viewType = -1;

    public DesignerCaseAdapter(Context context, List<ProductImageInfo> list, Product designerCaseInfo,
                               RecyclerViewOnItemClickListener listener) {
        super(context, list);
        this.designerCaseInfo = designerCaseInfo;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            viewType = TYPE_HEAD;
        } else {
            viewType = TYPE_ITEM;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<ProductImageInfo> list) {
        switch (viewType) {
            case TYPE_HEAD:
                DesignerCaseInfoHeadHolder designerCaseInfoHeadHolder = (DesignerCaseInfoHeadHolder) viewHolder;
                designerCaseInfoHeadHolder.cellNameText.setText(designerCaseInfo.getCell());
                designerCaseInfoHeadHolder.stylelNameText.setText(designerCaseInfo.getHouse_area() + "㎡，" +
                        BusinessManager.convertDectypeToShow(designerCaseInfo.getDec_type()) + "，" + BusinessManager
                        .convertHouseTypeToShow(designerCaseInfo.getHouse_type()) + "，" + BusinessManager
                        .convertDecStyleToShow(designerCaseInfo.getDec_style()) + "风格");
                imageShow.displayImageHeadWidthThumnailImage(context, designerCaseInfo.getDesigner().getImageid(),
                        designerCaseInfoHeadHolder.designerinfo_head_img);
                if (designerCaseInfo.getDesigner().getAuth_type().equals(Constant.DESIGNER_FINISH_AUTH_TYPE)) {
                    designerCaseInfoHeadHolder.designerinfo_auth.setVisibility(View.VISIBLE);
                } else {
                    designerCaseInfoHeadHolder.designerinfo_auth.setVisibility(View.GONE);
                }
                designerCaseInfoHeadHolder.itemTitleText.setText("设计简介");
                designerCaseInfoHeadHolder.itemProduceText.setText(designerCaseInfo.getDescription());
                designerCaseInfoHeadHolder.designerinfo_head_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener) {
                            listener.OnViewClick(position);
                        }
                    }
                });
                break;
            case TYPE_ITEM:
                ProductImageInfo info = list.get(position - 1);
                final DesignerCaseViewHolder holder = (DesignerCaseViewHolder) viewHolder;
                imageShow.displayScreenWidthThumnailImage(context, info.getImageid(), holder.itemwCaseView);
                holder.itemTitleText.setText(info.getSection());
                holder.itemProduceText.setText(info.getDescription());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != listener) {
                            listener.OnItemClick(v, position - 1);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public View createView(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_HEAD:
                View headView = layoutInflater.inflate(R.layout.designer_case_info_layout, null);
                return headView;
            case TYPE_ITEM:
                View itemView = layoutInflater.inflate(R.layout.list_item_designer_case, null);
                return itemView;
        }
        return null;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        switch (viewType) {
            case TYPE_HEAD:
                return new DesignerCaseInfoHeadHolder(view);
            case TYPE_ITEM:
                return new DesignerCaseViewHolder(view);
        }
        return null;
    }

    static class DesignerCaseInfoHeadHolder extends RecyclerViewHolderBase {
        @Bind(R.id.cell_name)
        TextView cellNameText;
        @Bind(R.id.stylelName)
        TextView stylelNameText;
        @Bind(R.id.designerinfo_head_img)
        ImageView designerinfo_head_img = null;
        @Bind(R.id.designerinfo_auth)
        ImageView designerinfo_auth = null;
        @Bind(R.id.produceTitle)
        TextView itemTitleText;
        @Bind(R.id.produceText)
        TextView itemProduceText;

        public DesignerCaseInfoHeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class DesignerCaseViewHolder extends RecyclerViewHolderBase {
        @Bind(R.id.list_item_case_img)
        ImageView itemwCaseView;
        @Bind(R.id.list_item_case_title_text)
        TextView itemTitleText;
        @Bind(R.id.list_item_case_produce_text)
        TextView itemProduceText;

        public DesignerCaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
