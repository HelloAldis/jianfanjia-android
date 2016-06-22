package com.jianfanjia.cn.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryInfoList;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.model.DiarySetInfoList;
import com.jianfanjia.api.request.common.GetMyDiarySetRequest;
import com.jianfanjia.api.request.guest.SearchDiaryRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.activity.diary.AddDiaryActivity;
import com.jianfanjia.cn.ui.activity.diary.AddDiarySetActivity;
import com.jianfanjia.cn.ui.adapter.DiaryDynamicAdapter;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description: com.jianfanjia.cn.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-06-12 17:41
 */
public class DiaryDynamicFragment extends BaseFragment {

    private static final String TAG = DiaryDynamicFragment.class.getClass().getName();
    @Bind(R.id.daily_pullfefresh)
    PullToRefreshRecycleView mPullToRefreshRecycleView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private DiaryDynamicAdapter mDiaryDynamicAdapter;
    private int total = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_diary));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);

        mDiaryDynamicAdapter = new DiaryDynamicAdapter(getContext(), mPullToRefreshRecycleView.getRefreshableView(),
                this);

        mPullToRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPullToRefreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getContext()));
        mPullToRefreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getAllUpdateDiary();
            }
        });
        mDiaryDynamicAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });
        mDiaryDynamicAdapter.setErrorView(errorLayout);
        mDiaryDynamicAdapter.setEmptyView(emptyLayout);

        mPullToRefreshRecycleView.setAdapter(mDiaryDynamicAdapter);
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
                                mDiaryDynamicAdapter.getData().size());
                        mDiaryDynamicAdapter.addData(diaryInfoList.getDiaries());
                        if (total > mDiaryDynamicAdapter.getData().size()) {
                            mDiaryDynamicAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_LOAD_MORE);
                        } else {
                            mDiaryDynamicAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NO_MORE);
                        }
                        mDiaryDynamicAdapter.hideErrorAndEmptyView();
                    } else {
                        mDiaryDynamicAdapter.setEmptyViewShow();
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
                mDiaryDynamicAdapter.setErrorViewShow();
                mDiaryDynamicAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
            }
        });
    }

    @OnClick({R.id.imgbtn_add_diary})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_add_diary:
                addDiaryAction();
                break;
        }
    }

    private void addDiaryAction() {
        getAllDiarySet();
    }

    private void gotoAddDiary(List<DiarySetInfo> diarySetInfos) {
        AddDiaryActivity.intentToAddDiary(getContext(), diarySetInfos, null);
    }

    private void gotoAddDiarySet() {
        Bundle bundle = new Bundle();
        IntentUtil.startActivity(getActivity(), AddDiarySetActivity.class, bundle);
    }

    private void getAllDiarySet() {
        GetMyDiarySetRequest getMyDiarySetRequest = new GetMyDiarySetRequest();

        Api.getMyDiarySetList(getMyDiarySetRequest, new ApiCallback<ApiResponse<DiarySetInfoList>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<DiarySetInfoList> apiResponse) {
                DiarySetInfoList diarySetInfoList = apiResponse.getData();
                if (diarySetInfoList != null) {
                    List<DiarySetInfo> mDiaryInfoList = diarySetInfoList.getDiarySets();
                    if (mDiaryInfoList.size() > 0) {
                        gotoAddDiary(mDiaryInfoList);
                    } else {
                        gotoAddDiarySet();
                    }
                } else {
                    gotoAddDiarySet();
                }
            }

            @Override
            public void onFailed(ApiResponse<DiarySetInfoList> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_SHOW_DIARYINFO:

                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dynamic_diary;
    }
}
