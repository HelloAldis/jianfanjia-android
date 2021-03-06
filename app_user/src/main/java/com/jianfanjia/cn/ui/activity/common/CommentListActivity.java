package com.jianfanjia.cn.ui.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.request.common.SearchUserCommentRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseLoadMoreRecycleAdapter;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.ui.Event.ChoosedPlanEvent;
import com.jianfanjia.cn.ui.Event.RefreshDiaryInfoEvent;
import com.jianfanjia.cn.ui.activity.diary.DiaryDetailInfoActivity;
import com.jianfanjia.cn.ui.activity.requirement.MyProcessDetailActivity;
import com.jianfanjia.cn.ui.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.ui.adapter.MyCommentInfoAdapter;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.common
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
        pullDownData();
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
                        bundle.putString(IntentConstant.TOPIC_ID, noticeInfo.getTopicid());
                        bundle.putString(IntentConstant.TOPICTYPE, viewType + "");
                        switch (viewType) {
                            case MyCommentInfoAdapter.PLAN_TYPE:
                                bundle.putString(IntentConstant.TO, noticeInfo.getPlan().getDesignerid());
                                startActivity(CommentActivity.class, bundle);
                                break;
                            case MyCommentInfoAdapter.NODE_TYPE:
                                bundle.putString(IntentConstant.TO, noticeInfo.getProcess().getFinal_designerid());
                                bundle.putString(IntentConstant.SECTION, noticeInfo.getSection());
                                bundle.putString(IntentConstant.ITEM, noticeInfo.getItem());
                                startActivity(CommentActivity.class, bundle);
                                break;
                            case MyCommentInfoAdapter.DIARY_TYPE:
                                DiaryDetailInfoActivity.intentToDiaryDetailInfoByComment
                                        (CommentListActivity.this, noticeInfo.getDiary(), DiaryDetailInfoActivity
                                                .intentFromComment, noticeInfo.getUser(), noticeInfo.getCommentid());
                                break;
                        }
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
                            case MyCommentInfoAdapter.DIARY_TYPE:
                                DiaryDetailInfoActivity.intentToDiaryDetailInfo
                                        (CommentListActivity.this, noticeInfo.getDiary(), DiaryDetailInfoActivity
                                                .intentFromBaseinfo, null);
                                break;
                        }
                    }
                });
        myCommentInfoAdapter.setLoadMoreListener(new BaseLoadMoreRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                getMyCommentInfo(myCommentInfoAdapter.getData().size(), Constant.HOME_PAGE_LIMIT, loadMoreCallback);
            }
        });
        refreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getMyCommentInfo(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownCallback);
            }
        });
        refreshRecycleView.setAdapter(myCommentInfoAdapter);

        myCommentInfoAdapter.setEmptyView(emptyView);
        myCommentInfoAdapter.setErrorView(errorView);

        getMyCommentInfo(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownCallback);
    }

    private void startPlanInfoActivity(Plan planInfo, Requirement requirementInfo) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(IntentConstant.PLAN_DETAIL, planInfo);
        planBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfo);
        planBundle.putInt(PreviewDesignerPlanActivity.PLAN_INTENT_FLAG, PreviewDesignerPlanActivity.COMMENT_INTENT);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    private void startProcessDetailActivity(Process processInfo) {
        Bundle processBundle = new Bundle();
        processBundle.putSerializable(IntentConstant.PROCESS_INFO, processInfo);
        startActivity(MyProcessDetailActivity.class, processBundle);
    }

    private void getMyCommentInfo(int from, int limit, ApiCallback<ApiResponse<UserMessageList>> apiCallback) {
        SearchUserCommentRequest request = new SearchUserCommentRequest();
        request.setFrom(from);
        request.setLimit(limit);
        Api.searchUserComment(request, apiCallback, this);
    }

    private ApiCallback<ApiResponse<UserMessageList>> pullDownCallback = new
            ApiCallback<ApiResponse<UserMessageList>>() {
                @Override
                public void onPreLoad() {
                    if (!mHasLoadOnce) {
                        Hud.show(getUiContext());
                    }
                }

                @Override
                public void onHttpDone() {
                    Hud.dismiss();
                    if (refreshRecycleView != null) {
                        refreshRecycleView.onRefreshComplete();
                    }
                }

                @Override
                public void onSuccess(ApiResponse<UserMessageList> apiResponse) {
                    UserMessageList noticeListInfo = apiResponse.getData();
                    if (noticeListInfo != null) {
                        int total = noticeListInfo.getTotal();
                        if (total > 0) {
                            myCommentInfoAdapter.clear();
                            myCommentInfoAdapter.addData(0, noticeListInfo.getList());
                            LogTool.d("total size =" + total);
                            LogTool.d("myCommentInfoAdapter.getData().size() =" +
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
                    makeTextShort(HttpCode.getMsg(code));
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

                }

                @Override
                public void onSuccess(ApiResponse<UserMessageList> apiResponse) {
                    UserMessageList noticeListInfo = apiResponse.getData();
                    if (noticeListInfo != null) {
                        int total = noticeListInfo.getTotal();
                        if (total > 0) {
                            myCommentInfoAdapter.addData(noticeListInfo.getList());
                            LogTool.d("total size =" + total);
                            LogTool.d("myCommentInfoAdapter.getData().size() =" +
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
                    makeTextShort(HttpCode.getMsg(code));
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
                getMyCommentInfo(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownCallback);
                break;
        }
    }

    public void onEventMainThread(ChoosedPlanEvent choosedPlanEvent) {
        LogTool.d("onEventMainThread");
        currentnNoticeInfo.getPlan().setStatus(Global.PLAN_STATUS5);
        myCommentInfoAdapter.updateItem(currentnNoticeInfo);
    }

    public void onEventMainThread(RefreshDiaryInfoEvent refreshDiaryInfoEvent) {
        pullDownData();
    }

    private void pullDownData(){
        if(myCommentInfoAdapter.getData().size() > 0){
            getMyCommentInfo(Constant.FROM_START, myCommentInfoAdapter.getData().size(), pullDownCallback);
        }else {
            getMyCommentInfo(Constant.FROM_START, Constant.HOME_PAGE_LIMIT, pullDownCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
