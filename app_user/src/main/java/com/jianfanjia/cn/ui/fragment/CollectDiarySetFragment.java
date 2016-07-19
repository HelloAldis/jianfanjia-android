package com.jianfanjia.cn.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.DiarySetInfo;
import com.jianfanjia.api.model.DiarySetInfoList;
import com.jianfanjia.api.request.common.GetDiarySetFavoriteListRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseFragment;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.Event.CollectDiarySetEvent;
import com.jianfanjia.cn.ui.adapter.DiarySetListAdapter;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.ui.fragment
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-07-09 17:26
 */
public class CollectDiarySetFragment extends BaseFragment implements PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = CollectDesignerFragment.class.getName();

    @Bind(R.id.my_favorite_designer_listview)
    PullToRefreshRecycleView mPullToRefreshRecycleView;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private DiarySetListAdapter mDiarySetListAdapter = null;
    private DiarySetInfoList favoriteDiarySetList = null;
    private List<DiarySetInfo> mDiarySetInfoList = new ArrayList<>();
    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    public static CollectDiarySetFragment newInstance() {
        CollectDiarySetFragment collectDiarySetFragment = new CollectDiarySetFragment();
        return collectDiarySetFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        isPrepared = true;
        load();
        return view;
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.emtpy_view_no_diaryset_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_diaryset);
        mPullToRefreshRecycleView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPullToRefreshRecycleView.setHasFixedSize(true);
        mPullToRefreshRecycleView.setItemAnimator(new DefaultItemAnimator());
        mPullToRefreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity()
                .getApplicationContext()));
        mPullToRefreshRecycleView.setOnRefreshListener(this);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        getFavoriteDiarySetList(0, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);

    }

    private void onVisible() {
        load();
    }

    private void onInvisible() {

    }

    private void load() {
        if (!isPrepared || !isVisible) {
            return;
        }
        getFavoriteDiarySetList(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getFavoriteDiarySetList(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getFavoriteDiarySetList(mDiarySetInfoList.size(), Constant.HOME_PAGE_LIMIT, getUpMyFavoriteDesignerListener);
    }

    private void getFavoriteDiarySetList(int from, int limit, ApiCallback<ApiResponse<DiarySetInfoList>> listener) {
        GetDiarySetFavoriteListRequest request = new GetDiarySetFavoriteListRequest();
        request.setFrom(from);
        request.setLimit(limit);
        Api.getFavoriteDiarySetList(request, listener, this);
    }

    private ApiCallback<ApiResponse<DiarySetInfoList>> getDownMyFavoriteDesignerListener = new
            ApiCallback<ApiResponse<DiarySetInfoList>>() {
                @Override
                public void onPreLoad() {
                    if (!mHasLoadedOnce) {
                        Hud.show(getUiContext());
                    }
                }

                @Override
                public void onHttpDone() {
                    Hud.dismiss();
                    if (mPullToRefreshRecycleView != null) {
                        mPullToRefreshRecycleView.onRefreshComplete();
                    }
                }

                @Override
                public void onSuccess(ApiResponse<DiarySetInfoList> apiResponse) {
                    mHasLoadedOnce = true;

                    favoriteDiarySetList = apiResponse.getData();
                    LogTool.d("favoriteDiarySetList=" + favoriteDiarySetList);
                    if (favoriteDiarySetList != null) {
                        mDiarySetInfoList.clear();
                        if (favoriteDiarySetList.getDiarySets() != null) {
                            mDiarySetInfoList.addAll(favoriteDiarySetList.getDiarySets());
                        }
                        if (null != mDiarySetInfoList && mDiarySetInfoList.size() > 0) {
                            mDiarySetListAdapter = new DiarySetListAdapter(getActivity(), mDiarySetInfoList);
                            mDiarySetListAdapter.setHasAddDiarySet(false);
                            mPullToRefreshRecycleView.setAdapter(mDiarySetListAdapter);
                            mPullToRefreshRecycleView.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            errorLayout.setVisibility(View.GONE);
                        } else {
                            mPullToRefreshRecycleView.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            errorLayout.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailed(ApiResponse<DiarySetInfoList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.getMsg(code));
                    mPullToRefreshRecycleView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                }
            };

    private ApiCallback<ApiResponse<DiarySetInfoList>> getUpMyFavoriteDesignerListener = new
            ApiCallback<ApiResponse<DiarySetInfoList>>() {

                @Override
                public void onPreLoad() {

                }

                @Override
                public void onHttpDone() {
                    if (mPullToRefreshRecycleView != null) {
                        mPullToRefreshRecycleView.onRefreshComplete();
                    }
                }

                @Override
                public void onSuccess(ApiResponse<DiarySetInfoList> apiResponse) {
                    favoriteDiarySetList = apiResponse.getData();
                    LogTool.d("favoriteDiarySetList=" + favoriteDiarySetList);
                    if (favoriteDiarySetList != null) {
                        List<DiarySetInfo> diarySetInfoList = favoriteDiarySetList.getDiarySets();
                        if (null != diarySetInfoList && diarySetInfoList.size() > 0) {
                            mDiarySetListAdapter.add(mDiarySetInfoList.size(), diarySetInfoList);
                        } else {
                            makeTextShort(getResources().getString(R.string.no_more_data));
                        }
                    }
                }

                @Override
                public void onFailed(ApiResponse<DiarySetInfoList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.getMsg(code));
                }

            };

    public void onEventMainThread(CollectDiarySetEvent collectDiarySetEvent) {
        notifyChangeItemState(collectDiarySetEvent.getDiarySetid(), collectDiarySetEvent.isCollect());
    }

    private void notifyChangeItemState(String diarySetid, boolean isCollect) {
        if (isCollect) {
            if (mDiarySetInfoList.size() > Constant.HOME_PAGE_LIMIT) {
                getFavoriteDiarySetList(0, (mDiarySetInfoList.size() / Constant.HOME_PAGE_LIMIT + 1) * Constant
                        .HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
            } else {
                getFavoriteDiarySetList(0, Constant.HOME_PAGE_LIMIT, getDownMyFavoriteDesignerListener);
            }
        } else {
            int removePos = -1;
            DiarySetInfo diarySetInfo = null;
            for (int i = 0; i < mDiarySetInfoList.size(); i++) {
                diarySetInfo = mDiarySetInfoList.get(i);
                if (diarySetInfo.get_id().equals(diarySetid)) {
                    removePos = i;
                }
            }
            LogTool.d(removePos + "");
            if (removePos != -1) {
                mDiarySetListAdapter.remove(removePos);
                if (mDiarySetInfoList.size() == 0) {
                    mPullToRefreshRecycleView.setVisibility(View.GONE);
                    emptyLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect_designer;
    }
}
