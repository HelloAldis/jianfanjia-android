package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.model.Designer;
import com.jianfanjia.api.model.Requirement;
import com.jianfanjia.api.request.user.ConfirmMeasureHouseRequest;
import com.jianfanjia.api.request.user.GetOrderedDesignerListRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.MyDesignerAdapter;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description:我的设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class MyDesignerActivity extends BaseSwipeBackActivity {
    private static final String TAG = MyDesignerActivity.class.getName();
    public static final int CHANGE_DESIGNER = 0;//替换设计师
    public static final int VIEW_COMMENT = 1;//查看评价
    public static final int COMMENT = 2;//评价
    public static final int VIEW_PLAN = 3;//查看方案
    public static final int VIEW_CONTRACT = 4;//查看合同
    public static final int CONFIRM_MEASURE_HOUSE = 5;//确认已量房
    public static final int VIEW_DESIGNER = 6;//查看设计师


    @Bind(R.id.act_my_designer_head)
    protected MainHeadView mainHeadView;

    @Bind(R.id.act_my_designer_pull_refresh)
    protected PullToRefreshRecycleView refreshView;

    @Bind(R.id.error_include)
    protected RelativeLayout error_Layout;

    private String requirementid;
    private Requirement requirementInfo;

    MyDesignerAdapter myDesignerAdapter;
    List<Designer> orderDesignerInfos = new ArrayList<>();

    private boolean isLoadedOnce;//是否成功加载过一次数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.my_designer));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        Intent intent = getIntent();
        requirementInfo = (Requirement) intent.getSerializableExtra(IntentConstant.REQUIREMENT_INFO);
        if (requirementInfo != null) {
            requirementid = requirementInfo.get_id();
        }
        initPullRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    private void initPullRefresh() {
        refreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        refreshView.setLayoutManager(new LinearLayoutManager(this));
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initdata();
            }
        });
        myDesignerAdapter = new MyDesignerAdapter(this, new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {
                Designer orderDesignerInfo = orderDesignerInfos.get(position);
                switch (itemType) {
                    case VIEW_COMMENT:
                        Bundle viewBundle = new Bundle();
                        viewBundle.putString(IntentConstant.IMAGE_ID, orderDesignerInfo.getImageid());
                        viewBundle.putString(IntentConstant.DESIGNER_NAME, orderDesignerInfo.getUsername());
                        viewBundle.putFloat(IntentConstant.RESPOND_SPEED, orderDesignerInfo.getRespond_speed());
                        viewBundle.putFloat(IntentConstant.SERVICE_ATTITUDE, orderDesignerInfo.getService_attitude());
                        viewBundle.putSerializable(IntentConstant.EVALUATION, orderDesignerInfo.getEvaluation());
                        startActivity(PingJiaInfoActivity.class, viewBundle);
                        break;
                    case COMMENT:
                        Bundle commentBundle = new Bundle();
                        commentBundle.putString(IntentConstant.IMAGE_ID, orderDesignerInfo.getImageid());
                        commentBundle.putString(IntentConstant.DESIGNER_NAME, orderDesignerInfo.getUsername());
                        commentBundle.putString(IntentConstant.DESIGNER_ID, orderDesignerInfo.get_id());
                        commentBundle.putFloat(IntentConstant.SPEED, orderDesignerInfo.getRespond_speed());
                        commentBundle.putFloat(IntentConstant.ATTITUDE, orderDesignerInfo.getService_attitude());
                        commentBundle.putString(IntentConstant.REQUIREMENT_ID, requirementid);
                        startActivity(PingjiaActivity.class, commentBundle);
                        break;
                    case VIEW_CONTRACT:
                        Bundle contractBundle = new Bundle();
                        contractBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, orderDesignerInfo
                                .getRequirement());
                        contractBundle.putInt(ContractActivity.CONSTRACT_INTENT_FLAG, ContractActivity
                                .DESIGNER_LIST_INTENT);
                        startActivity(ContractActivity.class, contractBundle);
                        break;
                    case VIEW_PLAN:
                        Bundle planBundle = new Bundle();
                        planBundle.putString(IntentConstant.DESIGNER_ID, orderDesignerInfo.get_id());
                        planBundle.putSerializable(IntentConstant.REQUIREMENT_INFO, requirementInfo);
                        planBundle.putString(IntentConstant.DESIGNER_NAME, orderDesignerInfo.getUsername());
                        startActivity(DesignerPlanListActivity.class, planBundle);
                        break;
                    case CHANGE_DESIGNER:
                        Bundle changeBundle = new Bundle();
                        changeBundle.putString(IntentConstant.DESIGNER_ID, orderDesignerInfo.get_id());
                        changeBundle.putString(IntentConstant.REQUIREMENT_ID, requirementid);
                        startActivity(ReplaceDesignerActivity.class, changeBundle);
                        break;
                    case CONFIRM_MEASURE_HOUSE:
                        confirmMeasureHouse(orderDesignerInfo.get_id());
                        break;
                    case VIEW_DESIGNER:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(IntentConstant.DESIGNER_ID, orderDesignerInfo.get_id());
                        startActivity(DesignerInfoActivity.class, bundle);
                        break;
                    default:
                        break;
                }
            }
        });
        refreshView.setAdapter(myDesignerAdapter);
        refreshView.addItemDecoration(UiHelper.buildDefaultHeightDecoration(getApplicationContext()));
    }

    @OnClick(R.id.error_include)
    protected void errorRefresh() {
        initdata();
    }

    protected void confirmMeasureHouse(String designerid) {
        ConfirmMeasureHouseRequest confirmMeasureHouseRequest = new ConfirmMeasureHouseRequest();
        confirmMeasureHouseRequest.setRequirementid(requirementid);
        confirmMeasureHouseRequest.setDesignerid(designerid);
        Api.confirmMeasureHouse(confirmMeasureHouseRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                initdata();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @OnClick(R.id.head_back_layout)
    protected void back() {
        appManager.finishActivity(this);
    }

    private void initdata() {
        GetOrderedDesignerListRequest getOrderedDesignerListRequest = new GetOrderedDesignerListRequest();
        getOrderedDesignerListRequest.setRequirementid(requirementid);
        Api.getOrderedDesignerList(getOrderedDesignerListRequest, new ApiCallback<ApiResponse<List<Designer>>>() {
            @Override
            public void onPreLoad() {
                if (!isLoadedOnce) {
                    showWaitDialog();
                }
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
                refreshView.onRefreshComplete();
            }

            @Override
            public void onSuccess(ApiResponse<List<Designer>> apiResponse) {
                orderDesignerInfos = apiResponse.getData();
                if (orderDesignerInfos != null && orderDesignerInfos.size() > 0) {
                    isLoadedOnce = true;
                    myDesignerAdapter.addItem(orderDesignerInfos);
                    error_Layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(ApiResponse<List<Designer>> apiResponse) {
                if (orderDesignerInfos == null || orderDesignerInfos.size() == 0) {
                    if (!isLoadedOnce) {
                        error_Layout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.NO_NETWORK_ERROR_MSG);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_designer;
    }
}