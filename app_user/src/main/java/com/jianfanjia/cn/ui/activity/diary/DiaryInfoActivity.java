package com.jianfanjia.cn.ui.activity.diary;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import com.jianfanjia.api.model.DailyDetailInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.adapter.DiaryAdapter;

/**
 * Description: com.jianfanjia.cn.ui.activity.diary
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-14 11:01
 */
public class DiaryInfoActivity extends BaseSwipeBackActivity {

    @Bind(R.id.diary_recycleview)
    RecyclerView mRecyclerView;

    private DiaryAdapter mDiaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mDiaryAdapter = new DiaryAdapter(this,mRecyclerView);
        mDiaryAdapter.addItem(new DailyDetailInfo());
        mDiaryAdapter.addItem(new DailyDetailInfo());
        mDiaryAdapter.addItem(new DailyDetailInfo());
        mDiaryAdapter.addItem(new DailyDetailInfo());
        mDiaryAdapter.addItem(new DailyDetailInfo());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(this));
        mDiaryAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });

        mRecyclerView.setAdapter(mDiaryAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_diaryinfo;
    }
}
