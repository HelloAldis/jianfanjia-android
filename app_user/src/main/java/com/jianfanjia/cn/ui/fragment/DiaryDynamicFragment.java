package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiaryInfo;
import com.jianfanjia.api.model.DiaryInfoList;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.model.DiarySetInfoList;
import com.jianfanjia.api.model.DiaryUpdateInfo;
import com.jianfanjia.api.request.common.GetMyDiarySetRequest;
import com.jianfanjia.api.request.guest.GetDiaryUpdateRequest;
import com.jianfanjia.api.request.guest.SearchDiaryRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.business.DiaryBusiness;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.IntentUtil;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.Event.RefreshDiaryInfoEvent;
import com.jianfanjia.cn.ui.activity.diary.AddDiaryActivity;
import com.jianfanjia.cn.ui.activity.diary.AddDiarySetActivity;
import com.jianfanjia.cn.ui.adapter.DiaryDynamicAdapter;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

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
    private android.os.Handler mHandler = new android.os.Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_diary));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_designer);

        mDiaryDynamicAdapter = new DiaryDynamicAdapter(getContext(), mPullToRefreshRecycleView.getRefreshableView());

        mPullToRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mPullToRefreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getContext()));
        mPullToRefreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pullRefreshMoreData();
            }
        });
        mDiaryDynamicAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreOldData();
            }
        });
        mDiaryDynamicAdapter.setErrorView(errorLayout);
        mDiaryDynamicAdapter.setEmptyView(emptyLayout);

        mPullToRefreshRecycleView.setAdapter(mDiaryDynamicAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        pullRefreshMoreData();
    }

    @OnClick({R.id.imgbtn_add_diary, R.id.error_include})
    protected void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_add_diary:
                addDiaryAction();
                break;
            case R.id.error_include:
                pullRefreshMoreData();
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
            }

            @Override
            public void onHttpDone() {
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

    private void pullRefreshMoreData() {
        if (mDiaryDynamicAdapter.getData().size() == 0) {
            loadMoreData(System.currentTimeMillis(), firstLoadMoreData);
        } else {
            refreshNewData(mDiaryDynamicAdapter.getData().get(0).getCreate_at());
        }
    }

    private Runnable refreshOldDataStatusRunable = new Runnable() {
        @Override
        public void run() {
            refreshOldDataStatus();
        }
    };

    private void refreshOldDataStatus() {
        List<String> requireRefreshStatusDiaryInfoId = new ArrayList<>();
        for (DiaryInfo diaryInfo : mDiaryDynamicAdapter.getData()) {
            if (System.currentTimeMillis() - diaryInfo.getRefreshTime() >= DiaryBusiness.REQUIRE_REFRESH_TIME) {
                requireRefreshStatusDiaryInfoId.add(diaryInfo.get_id());
            }
        }
        if (requireRefreshStatusDiaryInfoId.size() > 0) {
            refreshNowDataStatus(requireRefreshStatusDiaryInfoId);
        }
    }

    private void loadMoreOldData() {
        int lastSize = mDiaryDynamicAdapter.getData().size() - 1;
        DiaryInfo oldestDiaryIngo = mDiaryDynamicAdapter.getData().get(lastSize);
        loadMoreData(oldestDiaryIngo.getCreate_at(), normalLoadMoreData);
    }

    private void refreshNewData(long lastCreateAt) {
        SearchDiaryRequest searchDiaryRequest = new SearchDiaryRequest();
        Map<String, Object> create = new HashMap<>();
        create.put("$gt", lastCreateAt);
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
                mHandler.postDelayed(refreshOldDataStatusRunable, 200);
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
                        setRefreshTime(diaryInfoList.getDiaries());
                        mDiaryDynamicAdapter.addData(0, diaryInfoList.getDiaries());
                        mPullToRefreshRecycleView.getRefreshableView().scrollToPosition(0);
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
            }
        });
    }

    private void refreshNowDataStatus(final List<String> diaryIdList) {
        GetDiaryUpdateRequest getDiaryUpdateRequest = new GetDiaryUpdateRequest();
        getDiaryUpdateRequest.setDiaryids(diaryIdList);

        Api.getDiaryChanges(getDiaryUpdateRequest, new ApiCallback<ApiResponse<List<DiaryUpdateInfo>>>() {
            @Override
            public void onPreLoad() {

            }

            @Override
            public void onHttpDone() {

            }

            @Override
            public void onSuccess(ApiResponse<List<DiaryUpdateInfo>> apiResponse) {
                if (apiResponse.getData() != null) {
                    refreshOldDataSuccess(apiResponse.getData());
                }
            }

            @Override
            public void onFailed(ApiResponse<List<DiaryUpdateInfo>> apiResponse) {

            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void refreshOldDataSuccess(List<DiaryUpdateInfo> data) {
        List<Integer> requireRefreshItemPos = new ArrayList<>();
        int i = 0;
        for (DiaryInfo diaryInfo : mDiaryDynamicAdapter.getData()) {
            for (DiaryUpdateInfo diaryUpdateInfo : data) {
                if (diaryUpdateInfo != null && diaryInfo.get_id().equals(diaryUpdateInfo.get_id())) {
                    diaryInfo.setFavorite_count(diaryUpdateInfo.getFavorite_count());
                    diaryInfo.setView_count(diaryUpdateInfo.getView_count());
                    diaryInfo.setComment_count(diaryUpdateInfo.getComment_count());
                    requireRefreshItemPos.add(i);
                    break;
                }
            }
            i++;
        }
        for (int pos : requireRefreshItemPos) {
            LogTool.d(this.getClass().getName(), "refreshOldData pos =" + pos);
            mDiaryDynamicAdapter.notifyItemChanged(pos);
        }
    }

    private void setRefreshTime(List<DiaryInfo> diaryInfoList) {
        for (DiaryInfo diaryInfo : diaryInfoList) {
            diaryInfo.setRefreshTime(System.currentTimeMillis());
        }
    }

    private void loadMoreData(long oldestCreateAt, ApiCallback<ApiResponse<DiaryInfoList>> apiCallback) {
        SearchDiaryRequest searchDiaryRequest = new SearchDiaryRequest();
        Map<String, Object> create = new HashMap<>();
        create.put("$lt", oldestCreateAt);
        Map<String, Object> param = new HashMap<>();
        param.put("create_at", create);
        searchDiaryRequest.setQuery(param);
        searchDiaryRequest.setFrom(0);
        searchDiaryRequest.setLimit(Constant.HOME_PAGE_LIMIT);

        Api.searchDiary(searchDiaryRequest, apiCallback);
    }

    private ApiCallback<ApiResponse<DiaryInfoList>> normalLoadMoreData = new ApiCallback<ApiResponse<DiaryInfoList>>() {
        @Override
        public void onPreLoad() {
        }

        @Override
        public void onHttpDone() {
        }

        @Override
        public void onSuccess(ApiResponse<DiaryInfoList> apiResponse) {
            DiaryInfoList diaryInfoList = apiResponse.getData();

            loadMoreSuccess(diaryInfoList);
        }

        @Override
        public void onFailed(ApiResponse<DiaryInfoList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            loadMoreError();
        }
    };

    private ApiCallback<ApiResponse<DiaryInfoList>> firstLoadMoreData = new ApiCallback<ApiResponse<DiaryInfoList>>() {
        @Override
        public void onPreLoad() {
            showWaitDialog();
        }

        @Override
        public void onHttpDone() {
            hideWaitDialog();
        }

        @Override
        public void onSuccess(ApiResponse<DiaryInfoList> apiResponse) {
            DiaryInfoList diaryInfoList = apiResponse.getData();

            loadMoreSuccess(diaryInfoList);
        }

        @Override
        public void onFailed(ApiResponse<DiaryInfoList> apiResponse) {
            makeTextShort(apiResponse.getErr_msg());
        }

        @Override
        public void onNetworkError(int code) {
            loadMoreError();
        }
    };

    private void loadMoreError() {
        makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
        mDiaryDynamicAdapter.setErrorViewShow();
        mDiaryDynamicAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
    }

    private void loadMoreSuccess(DiaryInfoList diaryInfoList) {
        LogTool.d(TAG, "diaryInfoList:" + diaryInfoList);
        if (null != diaryInfoList) {
            total = diaryInfoList.getTotal();
            if (total > 0) {
                LogTool.d(TAG, "total size =" + total);
                LogTool.d(TAG, "searchDesignerAdapter.getData().size() =" +
                        mDiaryDynamicAdapter.getData().size());
                mDiaryDynamicAdapter.addData(diaryInfoList.getDiaries());
                setRefreshTime(diaryInfoList.getDiaries());
                if (diaryInfoList.getDiaries().size() == Constant.HOME_PAGE_LIMIT) {
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

    public void onEventMainThread(RefreshDiaryInfoEvent refreshDiaryInfoEvent) {
        DiaryInfo resultDiaryInfo = refreshDiaryInfoEvent.getDiaryInfo();
        if (resultDiaryInfo != null) {
            LogTool.d(this.getClass().getName(), "result diary id =" + resultDiaryInfo.get_id());
        }
        refreshOneDiaryStatus(resultDiaryInfo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void refreshOneDiaryStatus(DiaryInfo resultDiaryInfo) {
        int pos = 0;
        boolean isDelete = false;
        for (DiaryInfo diaryInfo : mDiaryDynamicAdapter.getData()) {
            if (diaryInfo.get_id().equals(resultDiaryInfo.get_id())) {
                LogTool.d(this.getClass().getName(), "diary is delete" + diaryInfo.is_deleted());
                if (resultDiaryInfo.is_deleted()) {
                    isDelete = true;
                    break;
                } else {
                    diaryInfo.setComment_count(resultDiaryInfo.getComment_count());
                    diaryInfo.setIs_my_favorite(resultDiaryInfo.is_my_favorite());
                    diaryInfo.setFavorite_count(resultDiaryInfo.getFavorite_count());
                    mDiaryDynamicAdapter.notifyItemChanged(pos);
                    break;
                }
            }
            pos++;
        }
        if (isDelete) {
            mDiaryDynamicAdapter.getData().remove(pos);
            mDiaryDynamicAdapter.notifyItemRemoved(pos);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dynamic_diary;
    }
}
