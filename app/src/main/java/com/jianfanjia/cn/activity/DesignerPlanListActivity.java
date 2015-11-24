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
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.library.PullToRefreshBase;
import com.jianfanjia.cn.view.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:设计师方案列表
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerPlanListActivity extends BaseActivity implements OnClickListener, ApiUiUpdateListener, ItemClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {
    private static final String TAG = DesignerPlanListActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private PullToRefreshListView designer_plan_listview = null;
    private List<PlanInfo> designerPlanList = new ArrayList<PlanInfo>();
    private String requirementid = null;
    private String designerid = null;
    private String designerName = null;

    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle designerBundle = intent.getExtras();
        requirementid = designerBundle.getString(Global.REQUIREMENT_ID);
        designerid = designerBundle.getString(Global.DESIGNER_ID);
        designerName = designerBundle.getString(Global.DESIGNER_NAME);
        LogTool.d(TAG, "requirementid:" + requirementid + "  designerid:" + designerid + "  designerName:" + designerName);
        initMainHeadView();
        designer_plan_listview = (PullToRefreshListView) findViewById(R.id.designer_plan_listview);
        designer_plan_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        getDesignerPlansList(requirementid, designerid);
    }


    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_plan_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView
                .setMianTitle(designerName + getResources().getString(R.string.planText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void setListener() {
        designer_plan_listview.setOnRefreshListener(this);
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        getDesignerPlansList(requirementid, designerid);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

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
        designer_plan_listview.onRefreshComplete();
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
        hideWaitDialog();
        designer_plan_listview.onRefreshComplete();
    }

    @Override
    public void onCallBack(int position, int pos) {
        LogTool.d(TAG, "position:" + position + "  pos:" + pos);
        PlanInfo planInfo = designerPlanList.get(position);
        LogTool.d(TAG, "planInfo:" + planInfo);
        String planid = planInfo.get_id();
        LogTool.d(TAG, "planid:" + planid);
        startToActivity(planid);
    }

    @Override
    public void onItemCallBack(int position, int itemType) {
        LogTool.d(TAG, "itemType:" + itemType);
        PlanInfo planInfo = designerPlanList.get(position);
        LogTool.d(TAG, "planInfo:" + planInfo);
        String planid = planInfo.get_id();
        String designerid = planInfo.getDesignerid();
        LogTool.d(TAG, "planid:" + planid + " designerid:" + designerid);
        switch (itemType) {
            case Constant.PLAN_COMMENT_ITEM:
                Intent commentIntent = new Intent(DesignerPlanListActivity.this, CommentActivity.class);
                Bundle commentBundle = new Bundle();
                commentBundle.putString(Global.TOPIC_ID, planid);
                commentBundle.putString(Global.TO, designerid);
                commentBundle.putString(Global.TOPICTYPE, Global.TOPIC_PLAN);
                commentIntent.putExtras(commentBundle);
                startActivityForResult(commentIntent, Constant.REQUESTCODE_GOTO_COMMENT);
                break;
            case Constant.PLAN_PREVIEW_ITEM:
                startToActivity(planid);
                break;
            default:
                break;
        }
    }

    private void startToActivity(String planid) {
        Bundle planBundle = new Bundle();
        planBundle.putString(Global.PLAN_ID, planid);
        startActivity(PreviewDesignerPlanActivity.class, planBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
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
