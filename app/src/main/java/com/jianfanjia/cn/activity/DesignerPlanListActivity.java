package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.jianfanjia.cn.adapter.DesignerPlanAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.PlanInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:设计师方案列表
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerPlanListActivity extends BaseActivity implements OnClickListener, ApiUiUpdateListener, ItemClickListener {
    private static final String TAG = DesignerPlanListActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private ListView designer_plan_listview = null;
    private List<PlanInfo> designerPlanList = new ArrayList<PlanInfo>();
    private String requirementid = null;
    private String designerid = null;

    @Override
    public void initView() {
        initMainHeadView();
        Intent intent = this.getIntent();
        Bundle designerBundle = intent.getExtras();
        requirementid = designerBundle.getString(Global.REQUIREMENT_ID);
        designerid = designerBundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "requirementid:" + requirementid + "  designerid:" + designerid);
        designer_plan_listview = (ListView) findViewById(R.id.designer_plan_listview);
        getDesignerPlansList(requirementid, designerid);
    }


    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_plan_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.planText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                finish();
                break;
            default:
                break;
        }
    }

    //业主获取我的方案
    private void getDesignerPlansList(String requestmentid, String designerid) {
        JianFanJiaClient.getDesignerPlansByUser(DesignerPlanListActivity.this, requestmentid, designerid, this, this);
    }

    @Override
    public void preLoad() {
        showWaitDialog(R.string.loading);
    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
        hideWaitDialog();
        designerPlanList = JsonParser.jsonToList(data.toString(), new TypeToken<List<PlanInfo>>() {
        }.getType());
        LogTool.d(TAG, "designerPlanList:" + designerPlanList);
        if (null != designerPlanList && designerPlanList.size() > 0) {
            DesignerPlanAdapter adapter = new DesignerPlanAdapter(this, designerPlanList, this);
            designer_plan_listview.setAdapter(adapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
        hideWaitDialog();
    }

    @Override
    public void onCallBack(int position, int pos) {
        LogTool.d(TAG, "position:" + position + "  pos:" + pos);
    }

    @Override
    public void onCallBack(int position) {
        PlanInfo planInfo = designerPlanList.get(position);
        LogTool.d(TAG, "planInfo:" + planInfo);
        String planid = planInfo.get_id();
        LogTool.d(TAG, "planid:" + planid);
        Bundle planBundle = new Bundle();
        planBundle.putString(Global.PLAN_ID, planid);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_plan_list;
    }

}
