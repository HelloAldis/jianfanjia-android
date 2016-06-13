package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import com.jianfanjia.api.model.DailyDetailInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;

/**
 * Description: com.jianfanjia.cn.ui.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-13 10:50
 */
public class DiaryAdapter extends BaseLoadMoreRecycleAdapter<DailyDetailInfo> {

    public DiaryAdapter(Context context, RecyclerView recyclerView){
        super(context,recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item_fragment_diary,null);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    static class DailyViewHolder extends RecyclerView.ViewHolder{

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
}
