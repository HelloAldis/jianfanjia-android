package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryInfoList;
import com.jianfanjia.api.request.guest.SearchDiaryRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.activity.diary.DiarySetInfoActivity;
import com.jianfanjia.cn.ui.adapter.DiaryAdapter;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-12 17:41
 */
public class DiaryFragment extends BaseFragment {

    private static final String TAG = DiaryFragment.class.getClass().getName();
    @Bind(R.id.daily_pullfefresh)
    PullToRefreshRecycleView mPullToRefreshRecycleView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private DiaryAdapter mDiaryAdapter;
    private int total = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_diary));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);

        mDiaryAdapter = new DiaryAdapter(getContext(), mPullToRefreshRecycleView.getRefreshableView());

        mPullToRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPullToRefreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getContext()));
        mPullToRefreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getAllUpdateDiary();
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

    @Override
    public void onResume() {
        super.onResume();
        getAllUpdateDiary();
    }

    private void getAllUpdateDiary() {
        SearchDiaryRequest searchDiaryRequest = new SearchDiaryRequest();
        Map<String, Object> create = new HashMap<>();
        create.put("$gt", 0);
        Map<String, Object> param = new HashMap<>();
        param.put("create_at", create);
        searchDiaryRequest.setQuery(param);

        Api.searchDiary(searchDiaryRequest, new ApiCallback<ApiResponse<DiaryInfoList>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {
                mPullToRefreshRecycleView.onRefreshComplete();
            }

            @Override
            public void onSuccess(ApiResponse<DiaryInfoList> apiResponse) {
                DiaryInfoList diaryInfoList = apiResponse.getData();

                LogTool.d(TAG, "diaryInfoList:" + diaryInfoList);
                if (null != diaryInfoList) {
                    total = diaryInfoList.getTotal();
                    if (total > 0) {
                        LogTool.d(TAG, "total size =" + total);
                        LogTool.d(TAG, "searchDesignerAdapter.getData().size() =" +
                                mDiaryAdapter.getData().size());
                        mDiaryAdapter.addData(diaryInfoList.getDiaries());
                        if (total > mDiaryAdapter.getData().size()) {
                            mDiaryAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_LOAD_MORE);
                        } else {
                            mDiaryAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NO_MORE);
                        }
                        mDiaryAdapter.hideErrorAndEmptyView();
                    } else {
                        mDiaryAdapter.setEmptyViewShow();
                    }
                }
            }

            @Override
            public void onFailed(ApiResponse<DiaryInfoList> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                mDiaryAdapter.setErrorViewShow();
                mDiaryAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
            }
        });
    }

    @OnClick({R.id.imgbtn_add_daily})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_add_daily:
                startActivity(DiarySetInfoActivity.class);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_daily;
    }
}
