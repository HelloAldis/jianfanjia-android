package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.DesignerCaseInfo;
import com.jianfanjia.cn.bean.ImageInfo;
import com.jianfanjia.cn.cache.BusinessManager;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.interf.RecyclerViewOnItemClickListener;

import java.util.List;

/**
 * Name: DesignerCaseAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 15:14
 */
public class DesignerCaseAdapter extends BaseRecyclerViewAdapter<ImageInfo> {
    private RecyclerViewOnItemClickListener listener;
    private DesignerCaseInfo designerCaseInfo;
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private int viewType = -1;

    public DesignerCaseAdapter(Context context, List<ImageInfo> list, DesignerCaseInfo designerCaseInfo, RecyclerViewOnItemClickListener listener) {
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
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<ImageInfo> list) {
        switch (viewType) {
            case TYPE_HEAD:
                DesignerCaseInfoHeadHolder designerCaseInfoHeadHolder = (DesignerCaseInfoHeadHolder) viewHolder;
                designerCaseInfoHeadHolder.cellNameText.setText(designerCaseInfo.getCell());
                designerCaseInfoHeadHolder.stylelNameText.setText(designerCaseInfo.getHouse_area() + "㎡，" + BusinessManager.convertHouseTypeToShow(designerCaseInfo.getHouse_type()) + "，" + BusinessManager.convertDecStyleToShow(designerCaseInfo.getDec_style()));
                imageShow.displayImageHeadWidthThumnailImage(context, designerCaseInfo.getDesigner().getImageid(), designerCaseInfoHeadHolder.designerinfo_head_img);
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
                ImageInfo info = list.get(position - 1);
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

    private static class DesignerCaseInfoHeadHolder extends RecyclerViewHolderBase {
        public TextView cellNameText;
        public TextView stylelNameText;
        public ImageView designerinfo_head_img = null;
        public ImageView designerinfo_auth = null;
        public TextView itemTitleText;
        public TextView itemProduceText;

        public DesignerCaseInfoHeadHolder(View itemView) {
            super(itemView);
            cellNameText = (TextView) itemView.findViewById(R.id.cell_name);
            stylelNameText = (TextView) itemView.findViewById(R.id.stylelName);
            designerinfo_head_img = (ImageView) itemView.findViewById(R.id.designerinfo_head_img);
            designerinfo_auth = (ImageView) itemView.findViewById(R.id.designerinfo_auth);
            itemTitleText = (TextView) itemView
                    .findViewById(R.id.produceTitle);
            itemProduceText = (TextView) itemView
                    .findViewById(R.id.produceText);
        }
    }

    private static class DesignerCaseViewHolder extends RecyclerViewHolderBase {
        public ImageView itemwCaseView;
        public TextView itemTitleText;
        public TextView itemProduceText;

        public DesignerCaseViewHolder(View itemView) {
            super(itemView);
            itemwCaseView = (ImageView) itemView
                    .findViewById(R.id.list_item_case_img);
            itemTitleText = (TextView) itemView
                    .findViewById(R.id.list_item_case_title_text);
            itemProduceText = (TextView) itemView
                    .findViewById(R.id.list_item_case_produce_text);
        }
    }
}
