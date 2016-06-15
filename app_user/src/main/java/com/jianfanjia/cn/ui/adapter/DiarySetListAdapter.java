package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;


/**
 * Name: DesignerWorksAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 13:44
 */
public class DiarySetListAdapter extends BaseRecyclerViewAdapter<DiaryInfo> {

    private OnItemEditListener listener;

    private static final int HEAD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;

    private boolean isEdit = false;

    public DiarySetListAdapter(Context context, List<DiaryInfo> list, OnItemEditListener listener) {
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
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DiaryInfo> list) {
        switch (getItemViewType(position)) {
            case CONTENT_TYPE:
                DiaryInfo diaryInfo = list.get(position - 1);
                DesignerWorksViewHolder holder = (DesignerWorksViewHolder) viewHolder;
                bindContentView(position - 1, diaryInfo, holder);
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

    private void bindContentView(final int position, DiaryInfo diaryInfo, DesignerWorksViewHolder holder) {


        if (isEdit) {
            holder.deleteLayout.setVisibility(View.VISIBLE);
            holder.itemwWorksView.setColorFilter(Color.parseColor("#55000000"));
            holder.itemView.setOnClickListener(null);
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
                view = layoutInflater.inflate(R.layout.list_item_diary_info,
                        null);
                return new DesignerWorksViewHolder(view);
            case HEAD_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diary_info_head,
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
