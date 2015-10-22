package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.adapter.MyDesignerAdapter;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ClickCallBack;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.baseview.DividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * Description:我的设计师
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
@EActivity(R.layout.activity_my_designer)
public class MyDesignerActivity extends BaseAnnotationActivity {

    public static final int CHANGE_DESIGNER = 0;//替换设计师
    public static final int VIEW_COMMENT = 1;//查看评价
    public static final int COMMENT = 2;//评价
    public static final int VIEW_PLAN = 3;//查看方案
    public static final int VIEW_CONTRACT = 4;//查看合同
    public static final int CONFIRM_MEASURE_HOUSE = 5;//确认已量房

    @ViewById(R.id.act_my_designer_head)
    protected MainHeadView mainHeadView;

    @ViewById(R.id.act_my_designer_recyclerview)
    protected RecyclerView recyclerView;

    @ViewById(R.id.act_my_designer_pull_refresh)
    protected SwipeRefreshLayout refreshView;

    private String requirementid;

    MyDesignerAdapter myDesignerAdapter;
    List<OrderDesignerInfo> orderDesignerInfos = new ArrayList<>();


    @AfterViews
    protected void initMainHeadView() {
        LogTool.d(this.getClass().getName(), "initMainHeadView");

        mainHeadView
                .setMianTitle(getResources().getString(R.string.my_designer));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);

        Intent intent = getIntent();
        requirementid = intent.getStringExtra(Global.REQUIREMENT_ID);
        initRecycleView();
        initdata();
        initPullRefresh();
    }

    private void initPullRefresh() {
        refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LogTool.d("MyDesignerActivity", "onRefresh");
                initdata();
            }
        });
    }

    @Click(R.id.head_back)
    protected void back() {
        finish();
    }

    protected void initdata() {
        LogTool.d(this.getClass().getName(), "initdata");
        if (requirementid != null) {
            JianFanJiaClient.getOrderedDesignerList(this, requirementid, new ApiUiUpdateListener() {
                @Override
                public void preLoad() {
                }

                @Override
                public void loadSuccess(Object data) {
                    refreshView.setRefreshing(false);
                    if (data != null) {
                        orderDesignerInfos = JsonParser.jsonToList(data.toString(),
                                new TypeToken<List < OrderDesignerInfo >>(){}.getType());
                        if(orderDesignerInfos != null && orderDesignerInfos.size() > 0){
                            myDesignerAdapter.addItem(orderDesignerInfos);
                        }
                    }
                }

                @Override
                public void loadFailture(String error_msg) {
                    refreshView.setRefreshing(false);
                }
            }, this);
        }
    }

    public void initRecycleView() {

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // 创建一个线性布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new FadeInUpAnimator(new DecelerateInterpolator(0.5F)));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        myDesignerAdapter = new MyDesignerAdapter(this, new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {
                OrderDesignerInfo orderDesignerInfo = orderDesignerInfos.get(position);
                switch (itemType) {
                    case VIEW_COMMENT:
                        break;
                    case COMMENT:
                        break;
                    case VIEW_CONTRACT:
                        break;
                    case VIEW_PLAN:
                        break;
                    case CHANGE_DESIGNER:
                        break;
                    case CONFIRM_MEASURE_HOUSE:
                        JianFanJiaClient.confirmMeasureHouse(MyDesignerActivity.this, requirementid, orderDesignerInfo.get_id(), new ApiUiUpdateListener() {
                            @Override
                            public void preLoad() {
                                showWaitDialog();
                            }

                            @Override
                            public void loadSuccess(Object data) {
                                hideWaitDialog();
                                initdata();
                            }

                            @Override
                            public void loadFailture(String error_msg) {
                                hideWaitDialog();
                            }
                        },MyDesignerActivity.this);
                        break;
                }
            }
        });
        recyclerView.setAdapter(myDesignerAdapter);
        recyclerView.getItemAnimator().setAddDuration(300);
    }


}