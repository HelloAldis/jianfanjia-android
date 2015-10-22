package com.jianfanjia.cn.activity;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.jianfanjia.cn.adapter.MyDesignerAdapter;
import com.jianfanjia.cn.bean.OrderDesignerInfo;
import com.jianfanjia.cn.bean.PlanInfo;
import com.jianfanjia.cn.interf.ClickCallBack;
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
public class MyDesignerActivity extends Activity {

    @ViewById(R.id.act_my_designer_head)
    protected MainHeadView mainHeadView;

    @ViewById(R.id.act_my_designer_recyclerview)
    protected RecyclerView recyclerView;

    MyDesignerAdapter myDesignerAdapter;
    List<OrderDesignerInfo> orderDesignerInfos = new ArrayList<>();


    @AfterViews
    protected void initMainHeadView() {
        LogTool.d(this.getClass().getName(), "initMainHeadView");

        mainHeadView
                .setMianTitle(getResources().getString(R.string.my_designer));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        initRecycleView();
        initdata();
    }

    @Click(R.id.head_back)
    protected void back(){
        finish();
    }

    protected void initdata() {
        LogTool.d(this.getClass().getName(),"initdata");
        orderDesignerInfos.clear();
        for (int i = 0;i < 9;i++) {
            OrderDesignerInfo orderDesignerInfo = new OrderDesignerInfo();
            PlanInfo plan = new PlanInfo();
            plan.setStatus(i + "");
            orderDesignerInfo.setPlan(plan);
            orderDesignerInfos.add(orderDesignerInfo);
        }
        LogTool.d(this.getClass().getName(),orderDesignerInfos.size()+"");
        myDesignerAdapter.addItem(orderDesignerInfos);
    }

    public void initRecycleView() {

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // 创建一个线性布局管理器
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new FadeInUpAnimator(new DecelerateInterpolator(0.5F)));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        myDesignerAdapter = new MyDesignerAdapter(this, new ClickCallBack() {
            @Override
            public void click(int position, int itemType) {
                switch (itemType) {

                }
            }
        });
        recyclerView.setAdapter(myDesignerAdapter);
        recyclerView.getItemAnimator().setAddDuration(300);
    }


}