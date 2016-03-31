package com.jianfanjia.cn.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.request.common.SearchUserCommentRequest;
import com.jianfanjia.cn.Event.ChoosedPlanEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.requirement.MyProcessDetailActivity;
import com.jianfanjia.cn.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.adapter.MyCommentInfoAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.activity.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-07 15:21
 */
public class CommentListActivity extends BaseSwipeBackActivity {

    @Bind(R.id.pullrefresh_recycleview)
    protected PullToRefreshRecycleView refreshRecycleView;

    @Bind(R.id.common_head)
    protected MainHeadView mainHeadView;

    @Bind(R.id.empty_include)
    protected View emptyView;

    @Bind(R.id.error_include)
    protected View errorView;

    private boolean mHasLoadOnce;

    private MyCommentInfoAdapter myCommentInfoAdapter;

    private UserMessage currentnNoticeInfo;//当前点击的留言位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getMyCommentInfo(Constant.FROM_START, pullDownCallback);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment_list;
    }

    public void initView() {
        mainHeadView.setMianTitle(getString(R.string.my_comment));

        ((TextView) emptyView.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_commnet));
        ((ImageView) emptyView.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_no_comment);

        refreshRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        refreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));
        refreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        myCommentInfoAdapter = new MyCommentInfoAdapter(this, refreshRecycleView.getRefreshableView(), new
                MyCommentInfoAdapter.OnItemCallback() {
                    @Override
                    public void onResponse(UserMessage noticeInfo, int viewType) {

                        Bundle bundle = new Bundle();
                        bundle.putString(Global.TOPIC_ID, noticeInfo.getTopicid());
                        bundle.putString(Global.TOPICTYPE, viewType + "");
                        bundle.putString(Global.TO, noticeInfo.getDesignerid());
                        switch (viewType) {
                            case MyCommentInfoAdapter.PLAN_TYPE:
                                break;
                            case MyCommentInfoAdapter.NODE_TYPE:
                                bundle.putString(Global.SECTION, noticeInfo.getSection());
                                bundle.putString(Global.ITEM, noticeInfo.getItem());
                                break;
                        }
                        startActivity(CommentActivity.class, bundle);

                    }

                    @Override
                    public void showDetail(UserMessage noticeInfo, int viewType) {
                        switch (viewType) {
                            case MyCommentInfoAdapter.PLAN_TYPE:
                                currentnNoticeInfo = noticeInfo;
                                startPlanInfoActivity(noticeInfo.getPlan(), noticeInfo.getRequirement());
                                break;
                            case MyCommentInfoAdapter.NODE_TYPE:
                                startProcessDetailActivity(noticeInfo.getProcess());
                                break;
                        }
                    }
                });
        myCommentInfoAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                getMyCommentInfo(myCommentInfoAdapter.getData().size(), loadMoreCallback);
            }
        });
        refreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getMyCommentInfo(Constant.FROM_START, pullDownCallback);
            }
        });
        refreshRecycleView.setAdapter(myCommentInfoAdapter);

        myCommentInfoAdapter.setEmptyView(emptyView);
        myCommentInfoAdapter.setErrorView(errorView);

        getMyCommentInfo(Constant.FROM_START, pullDownCallback);
    }

    private void startPlanInfoActivity(Plan planInfo, Requirement requirementInfo) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(Global.PLAN_DETAIL, planInfo);
        planBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        planBundle.putInt(PreviewDesignerPlanActivity.PLAN_INTENT_FLAG, PreviewDesignerPlanActivity.COMMENT_INTENT);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    private void startProcessDetailActivity(Process processInfo) {
        Bundle processBundle = new Bundle();
        processBundle.putSerializable(Global.PROCESS_INFO, processInfo);
        startActivity(MyProcessDetailActivity.class, processBundle);
    }

    private void getMyCommentInfo(int from, ApiCallback<ApiResponse<UserMessageList>> apiCallback) {
        SearchUserCommentRequest request = new SearchUserCommentRequest();
        request.setFrom(from);
        request.setLimit(Constant.HOME_PAGE_LIMIT);
        Api.searchUserComment(request, apiCallback);
    }

    private ApiCallback<ApiResponse<UserMessageList>> pullDownCallback = new
            ApiCallback<ApiResponse<UserMessageList>>() {
                @Override
                public void onPreLoad() {
                    if (!mHasLoadOnce) {
                        showWaitDialog();
                    }
                }

                @Override
                public void onHttpDone() {
                    hideWaitDialog();
                }

                @Override
                public void onSuccess(ApiResponse<UserMessageList> apiResponse) {
                    refreshRecycleView.onRefreshComplete();
                    UserMessageList noticeListInfo = apiResponse.getData();
                    if (noticeListInfo != null) {
                        int total = noticeListInfo.getTotal();
                        if (total > 0) {
                            myCommentInfoAdapter.clear();
                            myCommentInfoAdapter.addData(noticeListInfo.getList());
                            LogTool.d(this.getClass().getName(), "total size =" + total);
                            LogTool.d(this.getClass().getName(), "myCommentInfoAdapter.getData().size() =" +
                                    myCommentInfoAdapter.getData().size());
                            if (total > myCommentInfoAdapter.getData().size()) {
                                myCommentInfoAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_LOAD_MORE);
                            } else {
                                myCommentInfoAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NO_MORE);
                            }
                            myCommentInfoAdapter.hideErrorAndEmptyView();
                        } else {
                            myCommentInfoAdapter.setEmptyViewShow();
                        }
                        mHasLoadOnce = true;
                    }
                }

                @Override
                public void onFailed(ApiResponse<UserMessageList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                    refreshRecycleView.onRefreshComplete();
                    myCommentInfoAdapter.setErrorViewShow();
                    myCommentInfoAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
                }

                @Override
                public void onNetworkError(int code) {

                }
            };

    private ApiCallback<ApiResponse<UserMessageList>> loadMoreCallback = new
            ApiCallback<ApiResponse<UserMessageList>>() {
                @Override
                public void onPreLoad() {

                }

                @Override
                public void onHttpDone() {

                }

                @Override
                public void onSuccess(ApiResponse<UserMessageList> apiResponse) {
                    UserMessageList noticeListInfo = apiResponse.getData();
                    if (noticeListInfo != null) {
                        int total = noticeListInfo.getTotal();
                        if (total > 0) {
                            myCommentInfoAdapter.addData(noticeListInfo.getList());
                            LogTool.d(this.getClass().getName(), "total size =" + total);
                            LogTool.d(this.getClass().getName(), "myCommentInfoAdapter.getData().size() =" +
                                    myCommentInfoAdapter
                                            .getData().size());
                            if (total > myCommentInfoAdapter.getData().size()) {
                                myCommentInfoAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_LOAD_MORE);
                            } else {
                                myCommentInfoAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NO_MORE);
                            }
                            myCommentInfoAdapter.hideErrorAndEmptyView();
                        } else {
                            myCommentInfoAdapter.setEmptyViewShow();
                        }
                    }
                }

                @Override
                public void onFailed(ApiResponse<UserMessageList> apiResponse) {
                    makeTextShort(apiResponse.getErr_msg());
                    refreshRecycleView.onRefreshComplete();
                    myCommentInfoAdapter.setErrorViewShow();
                    myCommentInfoAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
                }

                @Override
                public void onNetworkError(int code) {

                }
            };

    @OnClick({R.id.head_back_layout, R.id.img_error})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.img_error:
                getMyCommentInfo(Constant.FROM_START, pullDownCallback);
                break;
        }
    }

    public void onEventMainThread(ChoosedPlanEvent choosedPlanEvent) {
        LogTool.d(this.getClass().getName(), "onEventMainThread");
        currentnNoticeInfo.getPlan().setStatus(Global.PLAN_STATUS5);
        myCommentInfoAdapter.updateItem(currentnNoticeInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
