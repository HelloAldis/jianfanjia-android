package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.model.DailyDetailInfo;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.activity.diary.DiaryInfoActivity;
import com.jianfanjia.cn.ui.adapter.DiaryAdapter;

/**
 * Description: com.jianfanjia.cn.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-12 17:41
 */
public class DiaryFragment extends BaseFragment {

    @Bind(R.id.daily_pullfefresh)
    PullToRefreshRecycleView mPullToRefreshRecycleView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private DiaryAdapter mDiaryAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_process));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);

        mDiaryAdapter = new DiaryAdapter(getContext(),mPullToRefreshRecycleView.getRefreshableView());
        mDiaryAdapter.addItem(new DailyDetailInfo());
        mDiaryAdapter.addItem(new DailyDetailInfo());
        mDiaryAdapter.addItem(new DailyDetailInfo());
        mDiaryAdapter.addItem(new DailyDetailInfo());
        mDiaryAdapter.addItem(new DailyDetailInfo());

        mPullToRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPullToRefreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getContext()));
        mPullToRefreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshRecycleView.onRefreshComplete();
                    }
                }, 1000);
            }
        });
        mDiaryAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });
        mDiaryAdapter.setErrorView(errorLayout);
        mDiaryAdapter.setEmptyView(emptyLayout);

        mPullToRefreshRecycleView.setAdapter(mDiaryAdapter);
    }

    @OnClick({R.id.imgbtn_add_daily})
    protected void onClick(View view){
        switch (view.getId()){
            case R.id.imgbtn_add_daily:
                startActivity(DiaryInfoActivity.class);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_daily;
    }
}
