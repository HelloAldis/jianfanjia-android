package com.jianfanjia.cn.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.jianfanjia.cn.adapter.DesignerPlanAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.PlanInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.interf.ItemClickListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

import java.util.ArrayList;
import java.util.HashMap;
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
    private List<List<HashMap<String, Object>>> designerPlanList;

    @Override
    public void initView() {
        initMainHeadView();
        designer_plan_listview = (ListView) findViewById(R.id.designer_plan_listview);
        initData();
        DesignerPlanAdapter adapter = new DesignerPlanAdapter(this, designerPlanList, this);
        designer_plan_listview.setAdapter(adapter);
    }

    public void initData() {
        designerPlanList = new ArrayList<List<HashMap<String, Object>>>();
        HashMap<String, Object> hashMap = null;
        List<HashMap<String, Object>> arrayListForEveryGridView;
        for (int i = 0; i < 8; i++) {
            arrayListForEveryGridView = new ArrayList<HashMap<String, Object>>();
            for (int j = 0; j < 8; j++) {
                hashMap = new HashMap<String, Object>();
                hashMap.put("content", R.mipmap.ic_launcher);
                arrayListForEveryGridView.add(hashMap);
            }
            designerPlanList.add(arrayListForEveryGridView);
        }
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

    }

    @Override
    public void loadSuccess(Object data) {
        LogTool.d(TAG, "data:" + data);
        PlanInfo planInfo = JsonParser.jsonToBean(data.toString(), PlanInfo.class);
        LogTool.d(TAG, "planInfo:" + planInfo);
        if (null != planInfo) {

        }
    }

    @Override
    public void loadFailture(String error_msg) {
        makeTextLong(error_msg);
    }

    @Override
    public void onCallBack(int position, int pos) {
        makeTextLong("position:" + position + "  pos:" + pos);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_plan_list;
    }

}
