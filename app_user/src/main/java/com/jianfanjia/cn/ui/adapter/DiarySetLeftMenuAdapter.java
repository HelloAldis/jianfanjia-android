package com.jianfanjia.cn.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.bean.DiarySetStageItem;
import com.jianfanjia.common.tool.LogTool;
import com.jianfanjia.common.tool.TDevice;

/**
 * Description: com.jianfanjia.cn.ui.adapter
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-08 17:11
 */
public class DiarySetLeftMenuAdapter extends RecyclerView.Adapter {

    List<DiarySetStageItem> leftMenuItems;
    LayoutInflater mLayoutInflater;
    private int lineHeight;
    private int totalItemHeight;
    private OnNavigateListener mOnNavigateListener;

    private int highLightColor;
    private int normalColor;


    public DiarySetLeftMenuAdapter(Context context, List<DiarySetStageItem> leftMenuItems) {
        this.leftMenuItems = leftMenuItems;
        mLayoutInflater = LayoutInflater.from(context);
        totalItemHeight = (int) TDevice.getScreenHeight() - TDevice.getNavigationBarHeight(context) - TDevice
                .getStatusBarHeight(context) - TDevice.dip2px(context, 48);
        lineHeight = (totalItemHeight - TDevice.dip2px(context, 18) * 10) / 9;

        highLightColor = context.getResources().getColor(R.color.orange_color);
        normalColor = context.getResources().getColor(R.color.light_black_color);

        LogTool.d(this.getClass().getName(), "totalItemHeight =" + totalItemHeight + ",lineHeight =" + lineHeight);
    }

    public void setOnNavigateListener(OnNavigateListener onNavigateListener) {
        mOnNavigateListener = onNavigateListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.list_item_diaryset_left_menu, null);
        DiarySetMenuViewHolder diarySetMenuViewHolder = new DiarySetMenuViewHolder(view);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) diarySetMenuViewHolder.ivItemLine
                .getLayoutParams();
        layoutParams.height = lineHeight;
        diarySetMenuViewHolder.ivItemLine.setLayoutParams(layoutParams);
        return new DiarySetMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DiarySetMenuViewHolder diarySetMenuViewHolder = (DiarySetMenuViewHolder) holder;

        final DiarySetStageItem diarySetStageItem = leftMenuItems.get(position);

        boolean isCheck = diarySetStageItem.isCheck();
        if (isCheck) {
            diarySetMenuViewHolder.tvDiarySetStage.setTextColor(highLightColor);
        } else {
            diarySetMenuViewHolder.tvDiarySetStage.setTextColor(normalColor);
        }
        if (diarySetStageItem.getCount() > 0) {
            diarySetMenuViewHolder.tvDiarySetStage.setText(diarySetStageItem.getItemName() + "阶段" + "（" +
                    diarySetStageItem.getCount() + "）");
            diarySetMenuViewHolder.tvDiarySetStage.setAlpha(1f);
            diarySetMenuViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNavigateListener != null) {
                        mOnNavigateListener.navigateTo(diarySetStageItem.getItemName());
                    }
                }
            });
        } else {
            diarySetMenuViewHolder.itemView.setOnClickListener(null);
            diarySetMenuViewHolder.tvDiarySetStage.setText(diarySetStageItem.getItemName() + "阶段");
            diarySetMenuViewHolder.tvDiarySetStage.setAlpha(0.4f);
        }

        if (position == leftMenuItems.size() - 1) {
            diarySetMenuViewHolder.ivItemLine.setVisibility(View.GONE);
        } else {
            diarySetMenuViewHolder.ivItemLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return leftMenuItems == null ? 0 : leftMenuItems.size();
    }

    static class DiarySetMenuViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_item_line)
        ImageView ivItemLine;

        @Bind(R.id.tv_diaryset_stage)
        TextView tvDiarySetStage;

        public DiarySetMenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnNavigateListener {
        void navigateTo(String itemName);
    }
}
