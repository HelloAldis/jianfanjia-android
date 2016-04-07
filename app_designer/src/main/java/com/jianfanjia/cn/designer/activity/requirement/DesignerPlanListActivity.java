package com.jianfanjia.cn.designer.activity.requirement;

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
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Plan;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.designer.GetRequirementPlanListRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.activity.common.CommentActivity;
import com.jianfanjia.cn.designer.adapter.DesignerPlanAdapter;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Global;
import com.jianfanjia.cn.designer.interf.ItemClickListener;
import com.jianfanjia.cn.designer.tools.UiHelper;
import com.jianfanjia.cn.designer.view.MainHeadView;
import com.jianfanjia.cn.designer.view.library.PullToRefreshBase;
import com.jianfanjia.cn.designer.view.library.PullToRefreshRecycleView;
import com.jianfanjia.common.tool.LogTool;

/**
 * Description:设计师方案列表
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerPlanListActivity extends BaseSwipeBackActivity implements
        ItemClickListener, PullToRefreshBase.OnRefreshListener2<RecyclerView> {
    private static final String TAG = DesignerPlanListActivity.class.getName();

    @Bind(R.id.my_plan_head_layout)
    protected MainHeadView mainHeadView;

    @Bind(R.id.designer_plan_listview)
    protected PullToRefreshRecycleView designer_plan_listview;


    private List<Plan> designerPlanList = new ArrayList<Plan>();
    private String requirementid = null;
    //    private String designerid = null;
    private Requirement requirementInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = this.getIntent();
        Bundle designerBundle = intent.getExtras();
        requirementInfo = (Requirement) designerBundle.getSerializable(Global.REQUIREMENT_INFO);
        if (requirementInfo != null) {
            requirementid = requirementInfo.get_id();
        }
        LogTool.d(TAG, "requirementid:" + requirementid);
    }

    public void initView() {
        initMainHeadView();
        initRecycleView();
    }

    private void initRecycleView() {
        designer_plan_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        designer_plan_listview.setLayoutManager(new LinearLayoutManager(this));
        designer_plan_listview.setItemAnimator(new DefaultItemAnimator());
        designer_plan_listview.addItemDecoration(UiHelper.buildDefaultHeightDecoration(this));
        designer_plan_listview.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDesignerPlansList(requirementid, dataManager.getUserId());
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
        getDesignerPlansList(requirementid, dataManager.getUserId());
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

    }

    //业主获取我的方案
    private void getDesignerPlansList(String requestmentid, String designerid) {
        GetRequirementPlanListRequest getRequirementPlanListRequest = new GetRequirementPlanListRequest();
        getRequirementPlanListRequest.setRequirementid(requestmentid);
        getRequirementPlanListRequest.setDesignerid(designerid);

        Api.getRequirementPlanList(getRequirementPlanListRequest, new ApiCallback<ApiResponse<List<Plan>>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                designer_plan_listview.onRefreshComplete();
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<List<Plan>> apiResponse) {
                designerPlanList = apiResponse.getData();
                LogTool.d(TAG, "designerPlanList:" + designerPlanList);
                if (null != designerPlanList && designerPlanList.size() > 0) {
                    DesignerPlanAdapter adapter = new DesignerPlanAdapter(DesignerPlanListActivity.this,
                            designerPlanList,
                            DesignerPlanListActivity.this);
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
        LogTool.d(TAG, "position:" + position + "  pos:" + pos);
        Plan plan = designerPlanList.get(position);
        LogTool.d(TAG, "plan:" + plan);
        String planid = plan.get_id();
        LogTool.d(TAG, "planid:" + planid);
        startToActivity(plan, requirementInfo);
    }

    @Override
    public void onItemCallBack(int position, int itemType) {
        LogTool.d(TAG, "itemType:" + itemType);
        Plan plan = designerPlanList.get(position);
        LogTool.d(TAG, "plan:" + plan);
        String planid = plan.get_id();
        String designerid = plan.getDesignerid();
        LogTool.d(TAG, "planid:" + planid + " designerid:" + designerid);
        switch (itemType) {
            case Constant.PLAN_COMMENT_ITEM:
                Intent commentIntent = new Intent(DesignerPlanListActivity.this, CommentActivity.class);
                Bundle commentBundle = new Bundle();
                commentBundle.putString(Global.TOPIC_ID, planid);
                commentBundle.putString(Global.TO, plan.getUserid());
                commentBundle.putString(Global.TOPICTYPE, Global.TOPIC_PLAN);
                commentIntent.putExtras(commentBundle);
                startActivityForResult(commentIntent, Constant.REQUESTCODE_GOTO_COMMENT);
                break;
            case Constant.PLAN_PREVIEW_ITEM:
                startToActivity(plan, requirementInfo);
                break;
            default:
                break;
        }
    }

    private void startToActivity(Plan plan, Requirement requirement) {
        Bundle planBundle = new Bundle();
        planBundle.putSerializable(Global.PLAN_DETAIL, plan);
        planBundle.putSerializable(Global.REQUIREMENT_INFO, requirement);
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
                getDesignerPlansList(requirementid, dataManager.getUserId());
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
