package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.ui.activity.diary.AddDiarySetActivity;
import com.jianfanjia.cn.ui.activity.diary.DiarySetInfoActivity;


/**
 * Name: DiarySetInfoAdapter 日记集列表
 * User: zhanghao
 * Date: 2015-10-15
 * Time: 13:44
 */
public class DiarySetListAdapter extends BaseRecyclerViewAdapter<DiarySetInfo> {

    private static final int HEAD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;

    public DiarySetListAdapter(Context context, List<DiarySetInfo> list) {
        super(context, list);
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DiarySetInfo> list) {
        switch (getItemViewType(position)) {
            case CONTENT_TYPE:
                DiarySetInfo diarySetInfo = list.get(position - 1);
                DesignerWorksViewHolder holder = (DesignerWorksViewHolder) viewHolder;
                bindContentView(position - 1, diarySetInfo, holder);
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
                gotoAddDiarySet(null);
            }
        });
    }

    private void gotoAddDiarySet(DiarySetInfo diarySetInfo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentConstant.DIARYSET_INFO, diarySetInfo);
        IntentUtil.startActivity(context, AddDiarySetActivity.class, bundle);
    }

    private void gotoDiarySetInfo(DiarySetInfo diarySetInfo) {
        DiarySetInfoActivity.intentToDiarySet(context, diarySetInfo);
    }

    private void bindContentView(final int position, final DiarySetInfo diarySetInfo, DesignerWorksViewHolder holder) {

        holder.tvDiarySetDec.setText(DiaryBusiness.getDiarySetDes(diarySetInfo));
        holder.tvDiarySetTitle.setText(diarySetInfo.getTitle());
        holder.tvDiarySetStage.setText(DiaryBusiness.getShowDiarySectionLabel(diarySetInfo.getLatest_section_label()));

        holder.tvViewCount.setText(diarySetInfo.getView_count() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDiarySetInfo(diarySetInfo);
            }
        });

        if (!TextUtils.isEmpty(diarySetInfo.getCover_imageid())) {
            imageShow.displayScreenWidthThumnailImage(context, diarySetInfo.getCover_imageid(), holder
                    .ivDiarySetCoverPic);
        } else {
            holder.ivDiarySetCoverPic.setImageResource(R.mipmap.bg_fragment_my);
        }

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
                view = layoutInflater.inflate(R.layout.list_item_diaryset_list_info,
                        null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new DesignerWorksViewHolder(view);
            case HEAD_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diaryset_list_head,
                        null);
                return new DesignerWorksViewHolderHead(view);
        }
        return null;
    }

    static class DesignerWorksViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.iv_diaryset_cover_pic)
        ImageView ivDiarySetCoverPic;
        @Bind(R.id.tv_diaryset_title)
        TextView tvDiarySetTitle;
        @Bind(R.id.tv_diaryset_dec)
        TextView tvDiarySetDec;
        //        @Bind(R.id.tv_like_count)
//        TextView tvLikeCount;
        @Bind(R.id.tv_view_count)
        TextView tvViewCount;

        @Bind(R.id.tv_diaryset_stage)
        TextView tvDiarySetStage;

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
