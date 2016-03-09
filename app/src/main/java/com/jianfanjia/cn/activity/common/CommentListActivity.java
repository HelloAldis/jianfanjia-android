package com.jianfanjia.cn.activity.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.requirement.MyProcessDetailActivity;
import com.jianfanjia.cn.activity.requirement.PreviewDesignerPlanActivity;
import com.jianfanjia.cn.adapter.MyCommentInfoAdapter;
import com.jianfanjia.cn.base.BaseRecycleAdapter;
import com.jianfanjia.cn.bean.MyCommentList;
import com.jianfanjia.cn.bean.PlandetailInfo;
import com.jianfanjia.cn.bean.ProcessInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

    private MyCommentInfoAdapter myCommentInfoAdapter;

    @AfterViews
    protected void initAnnotationView() {
        mainHeadView.setMianTitle(getString(R.string.my_comment));

        refreshRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        refreshRecycleView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));
        refreshRecycleView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        myCommentInfoAdapter = new MyCommentInfoAdapter(this, refreshRecycleView.getRefreshableView(), new MyCommentInfoAdapter.OnItemCallback() {
            @Override
            public void onResponse(MyCommentList.MyCommentInfo myCommentInfo, int viewType) {

                Bundle bundle = new Bundle();
                bundle.putString(Global.TOPIC_ID, myCommentInfo.getTopicid());
                bundle.putString(Global.TOPICTYPE, myCommentInfo.getTopictype());
                bundle.putString(Global.TO, myCommentInfo.getTo());
                switch (viewType) {
                    case MyCommentInfoAdapter.PLAN_TYPE:
                        break;
                    case MyCommentInfoAdapter.NODE_TYPE:
                        bundle.putString(Global.SECTION, myCommentInfo.getSection());
                        bundle.putString(Global.ITEM, myCommentInfo.getItem());
                        break;
                }
                startActivity(CommentActivity.class);

            }

            @Override
            public void showDetail(MyCommentList.MyCommentInfo myCommentInfo, int viewType) {
                switch (viewType) {
                    case MyCommentInfoAdapter.PLAN_TYPE:
                        startPlanInfoActivity(myCommentInfo.getPlanInfo());
                        break;
                    case MyCommentInfoAdapter.NODE_TYPE:
                        startProcessDetailActivity(myCommentInfo.getProcess());
                        break;
                }
            }
        });
        refreshRecycleView.setAdapter(myCommentInfoAdapter);

        myCommentInfoAdapter.setEmptyView(emptyView);
        myCommentInfoAdapter.setErrorView(errorView);

        getMyCommentInfo();
    }

    private void startPlanInfoActivity(PlandetailInfo plandetailInfo) {
        Bundle planBundle = new Bundle();
        planBundle.putString(Global.PLAN_ID, plandetailInfo.get_id());
        planBundle.putSerializable(Global.REQUIREMENT_INFO, plandetailInfo.getRequirement());
//        planBundle.putInt(Global.POSITION, itemPosition);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    private void startProcessDetailActivity(ProcessInfo processInfo){
        Bundle processBundle = new Bundle();
        processBundle.putSerializable(Global.PROCESS_INFO,processInfo);
        startActivity(MyProcessDetailActivity.class,processBundle);
    }

    private void getMyCommentInfo() {

    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        MyCommentList myCommentList = JsonParser.jsonToBean(data.toString(), MyCommentList.class);
        if (myCommentList != null) {
            int total = myCommentList.getTotal();
            if (total > 0) {
                myCommentInfoAdapter.addData(myCommentList.getComments());
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

    @Click({R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }
}
