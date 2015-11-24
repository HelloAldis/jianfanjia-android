package com.jianfanjia.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.adapter.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.adapter.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.bean.ImageInfo;

import java.util.List;

/**
 * Name: DesignerCaseAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 15:14
 */
public class DesignerCaseAdapter extends BaseRecyclerViewAdapter<ImageInfo> {

    public DesignerCaseAdapter(Context context, List<ImageInfo> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, int position, List<ImageInfo> list) {
        ImageInfo info = list.get(position);
        DesignerCaseViewHolder holder = (DesignerCaseViewHolder) viewHolder;
        imageShow.displayScreenWidthThumnailImage(context, info.getImageid(), holder.itemwCaseView);
        holder.itemTitleText.setText(info.getSection());
        holder.itemProduceText.setText(info.getDescription());
    }

    @Override
    public View createView(ViewGroup viewGroup, int position) {
        View view = layoutInflater.inflate(R.layout.list_item_designer_case,
                null);
        return view;
    }

    @Override
    public RecyclerViewHolderBase createViewHolder(View view) {
        return new DesignerCaseViewHolder(view);
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
