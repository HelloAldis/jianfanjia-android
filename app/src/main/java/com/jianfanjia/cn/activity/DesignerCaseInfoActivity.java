package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.DesignerCaseAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerCaseInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.UiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:设计师作品案例详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseInfoActivity extends BaseActivity implements ApiUiUpdateListener, OnClickListener {
    private static final String TAG = DesignerCaseInfoActivity.class.getName();
    private Toolbar toolbar = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    private ListView designer_case_listview = null;
    private TextView stylelText = null;
    private ImageView designerinfo_head_img = null;
    private TextView produceText = null;

    private DesignerCaseAdapter adapter = null;
    private List<DesignerCaseInfo> designerCaseList = new ArrayList<DesignerCaseInfo>();
    private String productid = null;
    private String designertid = null;

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_register_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.site_listview_item_text_style_big);
        collapsingToolbar.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL);
        collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbar.setExpandedTitleColor(Color.BLACK);
        designer_case_listview = (ListView) findViewById(R.id.designer_case_listview);
        designer_case_listview.setFocusable(false);
        stylelText = (TextView) findViewById(R.id.stylelName);
        designerinfo_head_img = (ImageView) findViewById(R.id.designerinfo_head_img);
        produceText = (TextView) findViewById(R.id.produceText);
        //---------------------------------------------
        Intent intent = this.getIntent();
        Bundle productBundle = intent.getExtras();
        productid = productBundle.getString(Global.PRODUCT_ID);
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
        designerinfo_head_img.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.designerinfo_head_img:
                Bundle designerBundle = new Bundle();
                designerBundle.putString(Global.DESIGNER_ID, designertid);
                startActivity(DesignerInfoActivity.class, designerBundle);
                break;
            default:
                break;
        }
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
            designertid = designerCaseInfo.getDesigner().get_id();
            collapsingToolbar.setTitle(designerCaseInfo.getCell());
            stylelText.setText(designerCaseInfo.getHouse_area() + "㎡");
            imageLoader.displayImage(Url_New.GET_IMAGE + designerCaseInfo.getDesigner().getImageid(), designerinfo_head_img, options);
            produceText.setText("设计简介:" + designerCaseInfo.getDescription());
            adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this, designerCaseInfo.getImages());
            designer_case_listview.setAdapter(adapter);
            UiHelper.setListViewHeightBasedOnChildren(designer_case_listview);//此处是必须要做的计算Listview的高度
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
