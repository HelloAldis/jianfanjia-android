package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.DesignerCaseAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerCaseInfo;
import com.jianfanjia.cn.config.Url_New;
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
    private TextView cellName = null;
    private TextView stylelText = null;
    private ImageView designerinfo_head_img = null;
    private TextView produceText = null;

    private DesignerCaseAdapter adapter = null;
    private List<DesignerCaseInfo> designerCaseList = new ArrayList<DesignerCaseInfo>();
    private String productid = null;

    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        designer_case_listview = (ListView) findViewById(R.id.designer_case_listview);
        designer_case_listview.setFocusable(false);
        cellName = (TextView) findViewById(R.id.cellName);
        stylelText = (TextView) findViewById(R.id.stylelName);
        designerinfo_head_img = (ImageView) findViewById(R.id.designerinfo_head_img);
        produceText = (TextView) findViewById(R.id.produceText);
        //---------------------------------------------
        Intent intent = this.getIntent();
        Bundle productBundle = intent.getExtras();
        productid = productBundle.getString("productId");
        LogTool.d(TAG, " productid======" + productid);
        initDesignerCasesList();
    }

    private void initDesignerCasesList() {
        getProductHomePageInfo(productid);
    }

    private void getProductHomePageInfo(String productid) {
        JianFanJiaClient.getProductHomePage(DesignerCaseInfoActivity.this, productid, this, this);
    }

    @Override
    public void setListener() {

    }

    @Override
    public void preLoad() {
        showWaitDialog(R.string.loding);
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        LogTool.d(TAG, "data======" + data);
        hideWaitDialog();
        parseResponse(data.toString());
    }

    private void parseResponse(String response) {
        DesignerCaseInfo designerCaseInfo = JsonParser.jsonToBean(response, DesignerCaseInfo.class);
        LogTool.d(TAG, "designerCaseInfo" + designerCaseInfo);
        if (null != designerCaseInfo) {
            cellName.setText(designerCaseInfo.getCell());
            stylelText.setText(designerCaseInfo.getHouse_area() + "㎡");
            imageLoader.displayImage(Url_New.GET_IMAGE + designerCaseInfo.getDesigner().getImageid(), designerinfo_head_img, options);
            produceText.setText("设计简介:" + designerCaseInfo.getDescription());
            adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this, designerCaseInfo.getImages());
            designer_case_listview.setAdapter(adapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
        makeTextLong(error_msg);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_case_info;
    }
}
