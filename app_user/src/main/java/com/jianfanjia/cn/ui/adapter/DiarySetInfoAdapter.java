package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseRecyclerViewAdapter;
import com.jianfanjia.cn.base.RecyclerViewHolderBase;


/**
 * Name: DesignerWorksAdapter
 * User: fengliang
 * Date: 2015-10-15
 * Time: 13:44
 */
public class DiarySetInfoAdapter extends BaseRecyclerViewAdapter<DiarySetInfo> {

    private OnItemEditListener listener;

    private static final int HEAD_TYPE = 0;
    private static final int CONTENT_TYPE = 1;

    public DiarySetInfoAdapter(Context context, List<DiarySetInfo> list, OnItemEditListener listener) {
        super(context, list);
        this.listener = listener;
    }

    @Override
    public void bindView(RecyclerViewHolderBase viewHolder, final int position, List<DiarySetInfo> list) {
        switch (getItemViewType(position)) {
            case CONTENT_TYPE:
                DiarySetInfo dailyInfo = list.get(position - 1);
                DailyViewHolder holder = (DailyViewHolder) viewHolder;
                bindContentView(position - 1, dailyInfo, holder);
                break;
            case HEAD_TYPE:
                DiarySetInfoViewHolderHead holderHead = (DiarySetInfoViewHolderHead) viewHolder;
                bindHeadView(holderHead);
                break;
        }

    }

    private void bindHeadView(DiarySetInfoViewHolderHead viewHolder) {
        viewHolder.rlWirteDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemAdd();
            }
        });
    }

    private void bindContentView(final int position, DiarySetInfo dailyInfo, DailyViewHolder holder) {


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
                view = layoutInflater.inflate(R.layout.list_item_fragment_diary,
                        null);
                return new DailyViewHolder(view);
            case HEAD_TYPE:
                view = layoutInflater.inflate(R.layout.list_item_diarydetail_write,
                        null);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT));
                return new DiarySetInfoViewHolderHead(view);
        }
        return null;
    }

    static class DailyViewHolder extends RecyclerViewHolderBase {

        @Bind(R.id.diary_head)
        ImageView ivDailyHead;

        @Bind(R.id.ltm_diary_stage)
        TextView tvDailyStage;

        @Bind(R.id.ltm_diary_cellname)
        TextView tvCellName;

        @Bind(R.id.ltm_diary_delte)
        TextView tvDailtDelete;

        @Bind(R.id.ltm_diary_content)
        TextView tvDailyContent;

        @Bind(R.id.ltm_diary_goingtime)
        TextView tvDailyGoingTime;

        @Bind(R.id.ltm_diary_comment_layout)
        RelativeLayout rlDailyCommentLayout;

        @Bind(R.id.ltm_diary_like_layout)
        RelativeLayout rlDailyLikeLayout;

        @Bind(R.id.tv_like_count)
        TextView tvLikeCount;

        @Bind(R.id.tv_comment_count)
        TextView tvCommentCount;

        public DailyViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class DiarySetInfoViewHolderHead extends RecyclerViewHolderBase {

        @Bind(R.id.rl_writediary)
        RelativeLayout rlWirteDiary;

        public DiarySetInfoViewHolderHead(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
