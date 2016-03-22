package com.jianfanjia.cn.activity.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.home.DesignerInfoActivity;
import com.jianfanjia.cn.adapter.MyDesignerAdapter;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.RequirementInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshRecycleView;

/**
 * Description:我的设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
@EActivity(R.layout.activity_my_designer)
public class MyDesignerActivity extends SwipeBackActivity {
    private static final String TAG = MyDesignerActivity.class.getName();
    public static final int CHANGE_DESIGNER = 0;//替换设计师
    public static final int VIEW_COMMENT = 1;//查看评价
    public static final int COMMENT = 2;//评价
    public static final int VIEW_PLAN = 3;//查看方案
    public static final int VIEW_CONTRACT = 4;//查看合同
    public static final int CONFIRM_MEASURE_HOUSE = 5;//确认已量房
    public static final int VIEW_DESIGNER = 6;//查看设计师


    @ViewById(R.id.act_my_designer_head)
    protected MainHeadView mainHeadView;

    @ViewById(R.id.act_my_designer_pull_refresh)
    protected PullToRefreshRecycleView refreshView;

    @ViewById(R.id.error_include)
    protected RelativeLayout error_Layout;

    private String requirementid;
    private RequirementInfo requirementInfo;

    MyDesignerAdapter myDesignerAdapter;
    List<OrderDesignerInfo> orderDesignerInfos = new ArrayList<>();

    private boolean isLoadedOnce;//是否成功加载过一次数据

    @AfterViews
    protected void initMainHeadView() {
        LogTool.d(this.getClass().getName(), "initMainHeadView");
        mainHeadView.setMianTitle(getResources().getString(R.string.my_designer));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);

        Intent intent = getIntent();
        requirementInfo = (RequirementInfo) intent.getSerializableExtra(Global.REQUIREMENT_INFO);
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
                OrderDesignerInfo orderDesignerInfo = orderDesignerInfos.get(position);
                switch (itemType) {
                    case VIEW_COMMENT:
                        Bundle viewBundle = new Bundle();
                        viewBundle.putString(Global.IMAGE_ID, orderDesignerInfo.getImageid());
                        viewBundle.putString(Global.DESIGNER_NAME, orderDesignerInfo.getUsername());
                        viewBundle.putFloat(Global.RESPOND_SPEED, orderDesignerInfo.getRespond_speed());
                        viewBundle.putFloat(Global.SERVICE_ATTITUDE, orderDesignerInfo.getService_attitude());
                        viewBundle.putSerializable(Global.EVALUATION, orderDesignerInfo.getEvaluation());
                        startActivity(PingJiaInfoActivity.class, viewBundle);
                        break;
                    case COMMENT:
                        Bundle commentBundle = new Bundle();
                        commentBundle.putString(Global.IMAGE_ID, orderDesignerInfo.getImageid());
                        commentBundle.putString(Global.DESIGNER_NAME, orderDesignerInfo.getUsername());
                        commentBundle.putString(Global.DESIGNER_ID, orderDesignerInfo.get_id());
                        commentBundle.putFloat(Global.SPEED, orderDesignerInfo.getRespond_speed());
                        commentBundle.putFloat(Global.ATTITUDE, orderDesignerInfo.getService_attitude());
                        commentBundle.putString(Global.REQUIREMENT_ID, requirementid);
                        startActivity(PingjiaActivity.class, commentBundle);
                        break;
                    case VIEW_CONTRACT:
                        Bundle contractBundle = new Bundle();
                        contractBundle.putSerializable(Global.REQUIREMENT_INFO, orderDesignerInfo.getRequirement());
                        contractBundle.putInt(ContractActivity.CONSTRACT_INTENT_FLAG, ContractActivity
                                .DESIGNER_LIST_INTENT);
                        startActivity(ContractActivity.class, contractBundle);
                        break;
                    case VIEW_PLAN:
                        Bundle planBundle = new Bundle();
                        planBundle.putString(Global.DESIGNER_ID, orderDesignerInfo.get_id());
                        planBundle.putSerializable(Global.REQUIREMENT_INFO, requirementInfo);
                        planBundle.putString(Global.DESIGNER_NAME, orderDesignerInfo.getUsername());
                        startActivity(DesignerPlanListActivity.class, planBundle);
                        break;
                    case CHANGE_DESIGNER:
                        Bundle changeBundle = new Bundle();
                        changeBundle.putString(Global.DESIGNER_ID, orderDesignerInfo.get_id());
                        changeBundle.putString(Global.REQUIREMENT_ID, requirementid);
                        startActivity(ReplaceDesignerActivity.class, changeBundle);
                        break;
                    case CONFIRM_MEASURE_HOUSE:
                        confirmMeasureHouse(orderDesignerInfo.get_id());
                        break;
                    case VIEW_DESIGNER:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Global.DESIGNER_ID, orderDesignerInfo.get_id());
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

    @Click(R.id.error_include)
    protected void errorRefresh() {
        initdata();
    }


    protected void confirmMeasureHouse(String designerid) {
        JianFanJiaClient.confirmMeasureHouse(MyDesignerActivity.this, requirementid, designerid, new
                ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {
                        showWaitDialog();
                    }

                    @Override
                    public void loadSuccess(Object data) {
                        initdata();
                    }

                    @Override
                    public void loadFailture(String error_msg) {
                        makeTextShort(error_msg);
                        hideWaitDialog();
                    }
                }, MyDesignerActivity.this);
    }

    @Click(R.id.head_back_layout)
    protected void back() {
        appManager.finishActivity(this);
    }

    protected void initdata() {
        LogTool.d(this.getClass().getName(), "initdata");
        if (requirementid != null) {
            JianFanJiaClient.getOrderedDesignerList(this, requirementid, this, this);
        }
    }

    @Override
    public void preLoad() {
        if (!isLoadedOnce) {
            super.preLoad();
        }
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        refreshView.onRefreshComplete();
        if (data != null) {
            orderDesignerInfos = JsonParser.jsonToList(data.toString(),
                    new TypeToken<List<OrderDesignerInfo>>() {
                    }.getType());
            if (orderDesignerInfos != null && orderDesignerInfos.size() > 0) {
                isLoadedOnce = true;
                myDesignerAdapter.addItem(orderDesignerInfos);
                error_Layout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        super.loadFailture(error_msg);
        refreshView.onRefreshComplete();
        if (orderDesignerInfos == null || orderDesignerInfos.size() == 0) {
            if (!isLoadedOnce) {
                error_Layout.setVisibility(View.VISIBLE);
            }
        }
    }


}