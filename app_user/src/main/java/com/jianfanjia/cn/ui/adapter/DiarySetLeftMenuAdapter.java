package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DiarySetStageItem;

/**
 * Description: com.jianfanjia.cn.ui.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-08 17:11
 */
public class DiarySetLeftMenuAdapter extends RecyclerView.Adapter {

    List<DiarySetStageItem> leftMenuItems;
    LayoutInflater mLayoutInflater;

    public DiarySetLeftMenuAdapter(Context context, List<DiarySetStageItem> leftMenuItems) {
        this.leftMenuItems = leftMenuItems;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_diaryset_left_menu, null);
        return new DiarySetMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DiarySetMenuViewHolder diarySetMenuViewHolder = (DiarySetMenuViewHolder) holder;

        DiarySetStageItem diarySetStageItem = leftMenuItems.get(position);

        boolean isHasDiary = diarySetStageItem.isHasDiary();
        boolean isCheck = diarySetStageItem.isCheck();
        if (isHasDiary) {
            diarySetMenuViewHolder.tvDiarySetStage.setText(diarySetStageItem.getItemName() + "阶段" + "（ + " +
                    diarySetStageItem.getCount() + "）");
            if (isCheck) {

            } else {

            }
        } else {
            diarySetMenuViewHolder.tvDiarySetStage.setText(diarySetStageItem.getItemName() + "阶段");
        }
    }

    @Override
    public int getItemCount() {
        return leftMenuItems == null ? 0 : leftMenuItems.size();
    }

    static class DiarySetMenuViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_item_indicator)
        ImageView ivItemIndicator;

        @Bind(R.id.tv_diaryset_stage)
        TextView tvDiarySetStage;

        public DiarySetMenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
