package com.jianfanjia.cn.activity.ui.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.config.Global;
import com.jianfanjia.cn.activity.ui.adapter.DesignerPlanAdapter;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.user.GetDesignerPlanListRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.ui.activity.common.CommentActivity;
import com.jianfanjia.cn.activity.api.Api;
import com.jianfanjia.cn.activity.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.activity.config.Constant;
import com.jianfanjia.cn.activity.constant.IntentConstant;
import com.jianfanjia.cn.activity.ui.interf.ItemClickListener;
import com.jianfanjia.cn.activity.tools.UiHelper;
import com.jianfanjia.cn.activity.view.MainHeadView;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshBase;
import com.jianfanjia.cn.pulltorefresh.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:设计师方案列表
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerPlanListActivity extends BaseSwipeBackActivity implements ItemClickListener, PullToRefreshBase
        .OnRefreshListener2<RecyclerView> {
    private static final String TAG = DesignerPlanListActivity.class.getName();

    @Bind(R.id.my_plan_head_layout)
    protected MainHeadView mainHeadView;

    @Bind(R.id.designer_plan_listview)
    protected PullToRefreshRecycleView designer_plan_listview;

    private List<Plan> designerPlanList = new ArrayList<>();
    private String requirementid = null;
    private Requirement requirementInfo = null;
    private String designerid = null;
    private String designerName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        this.initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle designerBundle = intent.getExtras();
        requirementInfo = (Requirement) designerBundle.getSerializable(IntentConstant.REQUIREMENT_INFO);
        requirementid = requirementInfo.get_id();
        designerid = designerBundle.getString(IntentConstant.DESIGNER_ID);
        designerName = designerBundle.getString(IntentConstant.DESIGNER_NAME);
        LogTool.d(TAG, "requirementid:" + requirementid + "  designerid:" + designerid + "  designerName:" +
                designerName);
    }

    private void initView() {
        initMainHeadView();
        initRecycleView();
    }

    private void initRecycleView() {
        designer_plan_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        designer_plan_listview.setLayoutManager(new LinearLayoutManager(this));
        designer_plan_listview.setHasFixedSize(true);
        designer_plan_listview.setItemAnimator(new DefaultItemAnimator());
        designer_plan_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));
        designer_plan_listview.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDesignerPlansList(requirementid, designerid);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.plan_list));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
        getDesignerPlansList(requirementid, designerid);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    //业主获取我的方案
    private void getDesignerPlansList(String requestmentid, String designerid) {
        GetDesignerPlanListRequest getDesignerPlanListRequest = new GetDesignerPlanListRequest();
        getDesignerPlanListRequest.setRequirementid(requestmentid);
        getDesignerPlanListRequest.setDesignerid(designerid);
        Api.getDesignerPlanList(getDesignerPlanListRequest, new ApiCallback<ApiResponse<List<Plan>>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
                designer_plan_listview.onRefreshComplete();
            }

            @Override
            public void onSuccess(ApiResponse<List<Plan>> apiResponse) {
                designerPlanList = apiResponse.getData();
                if (null != designerPlanList && designerPlanList.size() > 0) {
                    DesignerPlanAdapter adapter = new DesignerPlanAdapter(DesignerPlanListActivity.this,
                            designerPlanList, DesignerPlanListActivity.this);
                    designer_plan_listview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailed(ApiResponse<List<Plan>> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @Override
    public void onCallBack(int position, int pos) {
        LogTool.d(TAG, "position=" + position + "  pos=" + pos);
        Plan planInfo = designerPlanList.get(position);
        LogTool.d(TAG, "planInfo:" + planInfo);
        String planid = planInfo.get_id();
        LogTool.d(TAG, "planid:" + planid);
        startToActivity(planInfo);
    }

    @Override
    public void onItemCallBack(int position, int itemType) {
        LogTool.d(TAG, "position:" + position + "itemType:" + itemType);
        Plan planInfo = designerPlanList.get(position);
        LogTool.d(TAG, "planInfo:" + planInfo);
        String planid = planInfo.get_id();
        String designerid = planInfo.getDesignerid();
        LogTool.d(TAG, "planid:" + planid + " designerid:" + designerid);
        switch (itemType) {
            case Constant.PLAN_COMMENT_ITEM:
                Bundle commentBundle = new Bundle();
                commentBundle.putString(IntentConstant.TOPIC_ID, planid);
                commentBundle.putString(IntentConstant.TO, designerid);
                commentBundle.putString(IntentConstant.TOPICTYPE, Global.TOPIC_PLAN);
                startActivityForResult(CommentActivity.class, commentBundle, Constant.REQUESTCODE_GOTO_COMMENT);
                break;
            case Constant.PLAN_PREVIEW_ITEM:
                startToActivity(planInfo);
                break;
            default:
                break;
        }
    }

    private void startToActivity(Plan planInfo) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(IntentConstant.PLAN_DETAIL, planInfo);
        planBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfo);
//        planBundle.putString(Global.POSITION, planInfo.getName());
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUESTCODE_GOTO_COMMENT:
                getDesignerPlansList(requirementid, designerid);
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_plan_list;
    }

}
