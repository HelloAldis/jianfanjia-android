package com.jianfanjia.cn.designer.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.request.common.SearchUserCommentRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.requirement.MyProcessDetailActivity;
import com.jianfanjia.cn.designer.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.designer.adapter.MyCommentInfoAdapter;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.designer.base.BaseRecycleAdapter;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;

import butterknife.Bind;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getMyCommentInfo(Constant.FROM_START, pullDownCallback);
    }

    private void initView() {
        mainHeadView.setMianTitle(getString(R.string.my_comment));
        ((TextView) emptyView.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_commnet));
        ((ImageView) emptyView.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_no_comment);
        refreshRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        refreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));
        refreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        myCommentInfoAdapter = new MyCommentInfoAdapter(this, refreshRecycleView.getRefreshableView(), new
                MyCommentInfoAdapter.OnItemCallback() {
                    @Override
                    public void onResponse(UserMessage userMessage, int viewType) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Global.TOPIC_ID, userMessage.getTopicid());
                        bundle.putString(Global.TOPICTYPE, viewType + "");
                        switch (viewType) {
                            case MyCommentInfoAdapter.PLAN_TYPE:
                                bundle.putString(Global.TO, userMessage.getPlan().getUserid());
                                break;
                            case MyCommentInfoAdapter.NODE_TYPE:
                                bundle.putString(Global.TO,userMessage.getProcess().getUserid());
                                bundle.putString(Global.SECTION, userMessage.getSection());
                                bundle.putString(Global.ITEM, userMessage.getItem());
                                break;
                        }
                        startActivity(CommentActivity.class, bundle);
                    }

                    @Override
                    public void showDetail(UserMessage userMessage, int viewType) {
                        switch (viewType) {
                            case MyCommentInfoAdapter.PLAN_TYPE:
                                startPlanInfoActivity(userMessage.getPlan(), userMessage.getRequirement());
                                break;
                            case MyCommentInfoAdapter.NODE_TYPE:
                                startProcessDetailActivity(userMessage.getProcess().get_id());
                                break;
                        }
                    }
                });
        myCommentInfoAdapter.setLoadMoreListener(new BaseRecycleAdapter.LoadMoreListener() {
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

    private void startPlanInfoActivity(Plan plandetailInfo, Requirement requirementInfo) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(Global.PLAN_DETAIL, plandetailInfo);
        planBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    private void startProcessDetailActivity(String processId) {
        Intent gotoMyProcess = new Intent(CommentListActivity.this, MyProcessDetailActivity.class);
        gotoMyProcess.putExtra(Global.PROCESS_ID, processId);
        startActivity(gotoMyProcess);
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
                    refreshRecycleView.onRefreshComplete();
                }

                @Override
                public void onSuccess(ApiResponse<UserMessageList> apiResponse) {
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
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    myCommentInfoAdapter.setErrorViewShow();
                    myCommentInfoAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
                }
            };

    private ApiCallback<ApiResponse<UserMessageList>> loadMoreCallback = new
            ApiCallback<ApiResponse<UserMessageList>>() {
                @Override
                public void onPreLoad() {

                }

                @Override
                public void onHttpDone() {
                    refreshRecycleView.onRefreshComplete();
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
                }

                @Override
                public void onNetworkError(int code) {
                    makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
                    myCommentInfoAdapter.setErrorViewShow();
                    myCommentInfoAdapter.setState(BaseLoadMoreRecycleAdapter.STATE_NETWORK_ERROR);
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment_list;
    }
}
