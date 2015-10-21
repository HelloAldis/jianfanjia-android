package com.jianfanjia.cn.activity;

import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.jianfanjia.cn.adapter.DesignerCaseAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerCaseInfo;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:设计师案例详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseInfoActivity extends BaseActivity implements ApiUiUpdateListener {
    private static final String TAG = DesignerCaseInfoActivity.class.getName();
    private Toolbar mToolbar = null;
    private ListView designer_case_listview = null;
    private DesignerCaseAdapter adapter = null;
    private List<DesignerCaseInfo> designerCaseList = new ArrayList<DesignerCaseInfo>();

    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        designer_case_listview = (ListView) findViewById(R.id.designer_case_listview);
        designer_case_listview.setFocusable(false);
        initDesignerCasesList();
    }

    private void initDesignerCasesList() {
//        for (int i = 0; i < 5; i++) {
//            DesignerCaseInfo info = new DesignerCaseInfo();
//            info.setTitle("大厅" + 1);
//            info.setProduceInfo("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊。");
//            designerCaseList.add(info);
//        }
//        adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this, designerCaseList);
//        designer_case_listview.setAdapter(adapter);

        getProductHomePageInfo("55eed89bb36a91960da6bc3f");
    }

    private void getProductHomePageInfo(String productid) {
        JianFanJiaClient.getProductHomePage(DesignerCaseInfoActivity.this, productid, this, this);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        LogTool.d(TAG, "data======" + data);
        DesignerCaseInfo designerCaseInfo = JsonParser.jsonToBean(data.toString(), DesignerCaseInfo.class);
        LogTool.d(TAG, "designerCaseInfo" + designerCaseInfo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_case_info;
    }
}
