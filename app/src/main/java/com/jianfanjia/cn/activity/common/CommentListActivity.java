package com.jianfanjia.cn.activity.common;

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

import com.jianfanjia.cn.Event.ChoosedPlanEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.requirement.MyProcessDetailActivity_;
import com.jianfanjia.cn.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.adapter.MyCommentInfoAdapter;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.NoticeInfo;
import com.jianfanjia.cn.bean.NoticeListInfo;
import com.jianfanjia.cn.bean.PlandetailInfo;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.http.request.SearchUserCommentRequest;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.activity.common
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-07 15:21
 */
@EActivity(R.layout.activity_comment_list)
public class CommentListActivity extends SwipeBackActivity {

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

    private NoticeInfo currentnNoticeInfo;//当前点击的留言位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

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

        myCommentInfoAdapter = new MyCommentInfoAdapter(this, refreshRecycleView.getRefreshableView(), new
                MyCommentInfoAdapter.OnItemCallback() {
            @Override
            public void onResponse(NoticeInfo noticeInfo, int viewType) {

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
            public void showDetail(NoticeInfo noticeInfo, int viewType) {
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

    private void startPlanInfoActivity(PlandetailInfo plandetailInfo, RequirementInfo requirementInfo) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(Global.PLAN_DETAIL, plandetailInfo);
        planBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
        planBundle.putInt(PreviewDesignerPlanActivity.PLAN_INTENT_FLAG, PreviewDesignerPlanActivity.COMMENT_INTENT);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    private void startProcessDetailActivity(ProcessInfo processInfo) {
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
            NoticeListInfo noticeListInfo = JsonParser.jsonToBean(data.toString(), NoticeListInfo.class);
            if (noticeListInfo != null) {
                int total = noticeListInfo.getTotal();
                if (total > 0) {
                    myCommentInfoAdapter.clear();
                    myCommentInfoAdapter.addData(noticeListInfo.getList());
                    LogTool.d(this.getClass().getName(), "total size =" + total);
                    LogTool.d(this.getClass().getName(), "myCommentInfoAdapter.getData().size() =" +
                            myCommentInfoAdapter.getData().size());
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
        NoticeListInfo noticeListInfo = JsonParser.jsonToBean(data.toString(), NoticeListInfo.class);
        if (noticeListInfo != null) {
            int total = noticeListInfo.getTotal();
            if (total > 0) {
                myCommentInfoAdapter.addData(noticeListInfo.getList());
                LogTool.d(this.getClass().getName(), "total size =" + total);
                LogTool.d(this.getClass().getName(), "myCommentInfoAdapter.getData().size() =" + myCommentInfoAdapter
                        .getData().size());
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

    @Click({R.id.head_back_layout, R.id.img_error})
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
