package com.jianfanjia.cn.designer.fragment;

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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.request.common.SearchUserMsgRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.my.NoticeDetailActivity;
import com.jianfanjia.cn.designer.adapter.NoticeAdapter;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseFragment;
import com.jianfanjia.cn.designer.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.interf.RecyclerItemCallBack;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;

/**
 * @author fengliang
 * @ClassName: NoticeFragment
 * @Description: 通知 全部
 * @date 2015-8-26 下午1:07:52
 */
public class NoticeFragment extends BaseFragment {
    private static final String TAG = NoticeFragment.class.getName();
    private View view = null;

    @Bind(R.id.all_notice_listview)
    PullToRefreshRecycleView all_notice_listview;

    @Bind(R.id.empty_include)
    RelativeLayout emptyLayout;

    @Bind(R.id.error_include)
    RelativeLayout errorLayout;

    private NoticeAdapter noticeAdapter = null;
    private boolean isVisible = false;
    private boolean isPrepared = false;
    private boolean mHasLoadedOnce = false;
    private String[] typeArray = null;

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

    public static NoticeFragment newInstance(String[] typeArray) {
        Bundle args = new Bundle();
        NoticeFragment noticeFragment = new NoticeFragment();
        args.putStringArray("TypeArray", typeArray);
        noticeFragment.setArguments(args);
        return noticeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeArray = getArguments().getStringArray("TypeArray");
        LogTool.d(TAG, "typeArray=" + typeArray);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogTool.d(TAG, "=====onCreateView");
        if (null == view) {
            view = super.onCreateView(inflater, container, savedInstanceState);
            initView();
            isPrepared = true;
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (null != parent) {
            parent.removeView(view);
        }
        return view;
    }

    private void initView() {
        ((TextView) emptyLayout.findViewById(R.id.empty_text)).setText(getString(R.string.empty_view_no_notice_data));
        ((ImageView) emptyLayout.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_no_notice);
        all_notice_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        all_notice_listview.setLayoutManager(new LinearLayoutManager(getActivity()));
        all_notice_listview.setHasFixedSize(true);
        all_notice_listview.setItemAnimator(new DefaultItemAnimator());
        all_notice_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getActivity()
                .getApplicationContext()));
        all_notice_listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getNoticeList(Constant.FROM_START, typeArray, pullDownListener);
            }
        });

        noticeAdapter = new NoticeAdapter(getContext(), all_notice_listview.getRefreshableView(), new
                RecyclerItemCallBack() {
                    @Override
                    public void onClick(int position, Object obj) {
                        UserMessage noticeInfo = (UserMessage) obj;
                        LogTool.d(TAG, "position=" + position + " noticeInfo:" + noticeInfo.getContent());
                        Bundle detailBundle = new Bundle();
                        detailBundle.putString(Global.MSG_ID, noticeInfo.get_id());
                        startActivity(NoticeDetailActivity.class, detailBundle);
                    }
                });
        noticeAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                getNoticeList(noticeAdapter.getData().size(), typeArray, loadMoreListener);
            }
        });
        noticeAdapter.setEmptyView(emptyLayout);
        noticeAdapter.setErrorView(errorLayout);
        all_notice_listview.setAdapter(noticeAdapter);
    }

    private void onVisible() {
        loadData();
    }

    private void onInvisible() {

    }

    private void loadData() {
        if (!isPrepared || !isVisible) {
            return;
        }
        getNoticeList(Constant.FROM_START, typeArray, pullDownListener);
    }

    @OnClick(R.id.error_include)
    public void onClick() {
        getNoticeList(Constant.FROM_START, typeArray, pullDownListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void getNoticeList(int from, String[] typeStr, ApiCallback<ApiResponse<UserMessageList>> listener) {
        SearchUserMsgRequest request = new SearchUserMsgRequest();
        Map<String, Object> params = new HashMap<>();
        params.put("$in", typeStr);
        Map<String, Object> conditionParam = new HashMap<>();
        conditionParam.put("message_type", params);
        request.setQuery(conditionParam);
        request.setFrom(from);
        request.setLimit(Constant.HOME_PAGE_LIMIT);
        Api.searchUserMsg(request, listener);
    }

    private ApiCallback<ApiResponse<UserMessageList>> pullDownListener = new
            ApiCallback<ApiResponse<UserMessageList>>() {

                @Override
                public void onPreLoad() {
                    if (!mHasLoadedOnce) {
                        showWaitDialog();
                    }
                }

                @Override
                public void onHttpDone() {
                    hideWaitDialog();
                    all_notice_listview.onRefreshComplete();
                }

                @Override
                public void onSuccess(ApiResponse<UserMessageList> apiResponse) {
                    UserMessageList noticeListInfo = apiResponse.getData();
                    setLoadData(noticeListInfo, true);
                }

                @Override
                public void onFailed(ApiResponse<UserMessageList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    if (!mHasLoadedOnce) {
                        noticeAdapter.setErrorViewShow();
                        noticeAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
                    }
                }
            };

    private void setLoadData(UserMessageList noticeListInfo, boolean isClearOldData) {
        LogTool.d(TAG, "noticeListInfo:" + noticeListInfo);
        if (null != noticeListInfo) {
            int total = noticeListInfo.getTotal();
            if (total > 0) {
                if (isClearOldData) {
                    noticeAdapter.clear();
                }
                noticeAdapter.addData(noticeListInfo.getList());
                LogTool.d(TAG, "total size =" + total);
                LogTool.d(TAG, "myCommentInfoAdapter.getData().size() =" +
                        noticeAdapter.getData().size());
                if (total > noticeAdapter.getData().size()) {
                    noticeAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_LOAD_MORE);
                } else {
                    noticeAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NO_MORE);
                }
                noticeAdapter.hideErrorAndEmptyView();
            } else {
                noticeAdapter.setEmptyViewShow();
            }
            mHasLoadedOnce = true;
        }
    }

    private ApiCallback<ApiResponse<UserMessageList>> loadMoreListener = new
            ApiCallback<ApiResponse<UserMessageList>>() {


                @Override
                public void onPreLoad() {

                }

                @Override
                public void onHttpDone() {
                    all_notice_listview.onRefreshComplete();
                }

                @Override
                public void onSuccess(ApiResponse<UserMessageList> apiResponse) {
                    UserMessageList noticeListInfo = apiResponse.getData();
                    setLoadData(noticeListInfo, false);
                }

                @Override
                public void onFailed(ApiResponse<UserMessageList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    noticeAdapter.setErrorViewShow();
                    noticeAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
                }
            };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_all_notice;
    }
}
