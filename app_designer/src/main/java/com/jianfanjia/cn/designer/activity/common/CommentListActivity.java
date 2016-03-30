package com.jianfanjia.cn.designer.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.requirement.MyProcessDetailActivity_;
import com.jianfanjia.cn.designer.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.designer.adapter.MyCommentInfoAdapter;
import com.jianfanjia.cn.designer.base.BaseAnnotationActivity;
import com.jianfanjia.cn.designer.base.BaseRecycleAdapter;
import com.jianfanjia.api.model.UserMessage;
import com.jianfanjia.api.model.UserMessageList;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Process;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.http.JianFanJiaClient;
import com.jianfanjia.cn.designer.http.request.SearchUserCommentRequest;
import com.jianfanjia.cn.designer.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.designer.tools.JsonParser;
import com.jianfanjia.cn.designer.tools.LogTool;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;

/**
 * Description: com.jianfanjia.cn.activity.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-07 15:21
 */
@EActivity(R.layout.activity_comment_list)
public class CommentListActivity extends BaseAnnotationActivity {

    @ViewById(R.id.pullrefresh_recycleview)
    protected PullToRefreshRecycleView refreshRecycleView;

    @ViewById(R.id.common_head)
    protected MainHeadView mainHeadView;

    @ViewById(R.id.empty_include)
    protected View emptyView;

    @ViewById(R.id.error_include)
    protected View errorView;

    private boolean mHasLoadOnce;

    private MyCommentInfoAdapter myCommentInfoAdapter;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        getMyCommentInfo(Constant.FROM_START, pullDownListener);
    }

    @AfterViews
    protected void initAnnotationView() {
        mainHeadView.setMianTitle(getString(R.string.my_comment));

        ((TextView) emptyView.findViewById(R.id.empty_text)).setText(getString(R.string.search_no_commnet));
        ((ImageView) emptyView.findViewById(R.id.empty_img)).setImageResource(R.mipmap.icon_no_comment);

        refreshRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        refreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));
        refreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        myCommentInfoAdapter = new MyCommentInfoAdapter(this, refreshRecycleView.getRefreshableView(), new MyCommentInfoAdapter.OnItemCallback() {
            @Override
            public void onResponse(UserMessage userMessage, int viewType) {

                Bundle bundle = new Bundle();
                bundle.putString(Global.TOPIC_ID, userMessage.getTopicid());
                bundle.putString(Global.TOPICTYPE, viewType + "");
                bundle.putString(Global.TO, userMessage.getUserid());
                switch (viewType) {
                    case MyCommentInfoAdapter.PLAN_TYPE:
                        break;
                    case MyCommentInfoAdapter.NODE_TYPE:
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
                        startProcessInfoDetailActivity(userMessage.getProcessInfo());
                        break;
                }
            }
        });
        myCommentInfoAdapter.setLoadMoreListener(new BaseRecycleAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                getMyCommentInfo(myCommentInfoAdapter.getData().size(), CommentListActivity.this);
            }
        });
        refreshRecycleView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getMyCommentInfo(Constant.FROM_START, pullDownListener);
            }
        });
        refreshRecycleView.setAdapter(myCommentInfoAdapter);

        myCommentInfoAdapter.setEmptyView(emptyView);
        myCommentInfoAdapter.setErrorView(errorView);

        getMyCommentInfo(Constant.FROM_START, pullDownListener);
    }

    private void startPlanInfoActivity(Plan plandetailInfo, Requirement requirementInfo) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(Global.PLAN_DETAIL, plandetailInfo);
        planBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    private void startProcessDetailActivity(Process processInfo) {
        Bundle processBundle = new Bundle();
        processBundle.putSerializable(Global.PROCESS_INFO, processInfo);
        startActivity(MyProcessDetailActivity_.class, processBundle);
    }

    private void getMyCommentInfo(int from, ApiUiUpdateListener apiUiUpdateListener) {
        Map<String, Object> param = new HashMap<>();
        param.put(Constant.FROM, from);
        param.put(Constant.LIMIT, Constant.HOME_PAGE_LIMIT);

        JianFanJiaClient.searchUserComment(new SearchUserCommentRequest(this, param), apiUiUpdateListener, this);
    }

    private ApiUiUpdateListener pullDownListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {
            if (!mHasLoadOnce) {
                showWaitDialog();
            }
        }

        @Override
        public void loadSuccess(Object data) {
            hideWaitDialog();
            refreshRecycleView.onRefreshComplete();
            UserMessageList userMessageList = JsonParser.jsonToBean(data.toString(), UserMessageList.class);
            if (userMessageList != null) {
                int total = userMessageList.getTotal();
                if (total > 0) {
                    myCommentInfoAdapter.clear();
                    myCommentInfoAdapter.addData(userMessageList.getList());
                    LogTool.d(this.getClass().getName(), "total size =" + total);
                    LogTool.d(this.getClass().getName(), "myCommentInfoAdapter.getData().size() =" + myCommentInfoAdapter.getData().size());
                    if (total > myCommentInfoAdapter.getData().size()) {
                        myCommentInfoAdapter.setState(BaseRecycleAdapter.STATE_LOAD_MORE);
                    } else {
                        myCommentInfoAdapter.setState(BaseRecycleAdapter.STATE_NO_MORE);
                    }
                    myCommentInfoAdapter.hideErrorAndEmptyView();
                } else {
                    myCommentInfoAdapter.setEmptyViewShow();
                }
                mHasLoadOnce = true;
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            hideWaitDialog();
            makeTextShort(error_msg);
            refreshRecycleView.onRefreshComplete();
            myCommentInfoAdapter.setErrorViewShow();
            myCommentInfoAdapter.setState(BaseRecycleAdapter.STATE_NETWORK_ERROR);
        }
    };

    @Override
    public void preLoad() {
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        UserMessageList userMessageList = JsonParser.jsonToBean(data.toString(), UserMessageList.class);
        if (userMessageList != null) {
            int total = userMessageList.getTotal();
            if (total > 0) {
                myCommentInfoAdapter.addData(userMessageList.getList());
                LogTool.d(this.getClass().getName(), "total size =" + total);
                LogTool.d(this.getClass().getName(), "myCommentInfoAdapter.getData().size() =" + myCommentInfoAdapter.getData().size());
                if (total > myCommentInfoAdapter.getData().size()) {
                    myCommentInfoAdapter.setState(BaseRecycleAdapter.STATE_LOAD_MORE);
                } else {
                    myCommentInfoAdapter.setState(BaseRecycleAdapter.STATE_NO_MORE);
                }
                myCommentInfoAdapter.hideErrorAndEmptyView();
            } else {
                myCommentInfoAdapter.setEmptyViewShow();
            }
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        super.loadFailture(error_msg);
        myCommentInfoAdapter.setErrorViewShow();
        myCommentInfoAdapter.setState(BaseRecycleAdapter.STATE_NETWORK_ERROR);
    }

    @Click({R.id.head_back_layout,R.id.img_error})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.img_error:
                getMyCommentInfo(Constant.FROM_START, pullDownListener);
                break;
        }
    }
}
