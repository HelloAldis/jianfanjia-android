package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianfanjia.cn.adapter.DesignerCaseAdapter;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerCaseInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

/**
 * Description:设计师作品案例详情
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerCaseInfoActivity extends BaseActivity implements ApiUiUpdateListener, OnClickListener, AppBarLayout.OnOffsetChangedListener {
    private static final String TAG = DesignerCaseInfoActivity.class.getName();
    private Toolbar toolbar = null;
    private AppBarLayout appBarLayout = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    private RelativeLayout activity_case_info_top_layout = null;
    private RecyclerView designer_case_listview = null;
    private LinearLayoutManager mLayoutManager = null;
    private TextView stylelText = null;
    private ImageView designerinfo_head_img = null;
    private ImageView designerinfo_auth = null;
    private TextView produceText = null;
    private ImageView head_img = null;
    private TextView nameText = null;

    private String productid = null;
    private String designertid = null;

    private State state;

    @Override
    public void initView() {
        activity_case_info_top_layout = (RelativeLayout) findViewById(R.id.top_info_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_register_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.listview_item_text_style_title);
        collapsingToolbar.setExpandedTitleGravity(Gravity.CENTER_HORIZONTAL);
        collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
        collapsingToolbar.setExpandedTitleColor(Color.BLACK);

        designer_case_listview = (RecyclerView) findViewById(R.id.designer_case_listview);
        mLayoutManager = new LinearLayoutManager(DesignerCaseInfoActivity.this);
        designer_case_listview.setLayoutManager(mLayoutManager);
        designer_case_listview.setItemAnimator(new DefaultItemAnimator());
        designer_case_listview.setHasFixedSize(true);
        stylelText = (TextView) findViewById(R.id.stylelName);
        designerinfo_head_img = (ImageView) findViewById(R.id.designerinfo_head_img);
        designerinfo_auth = (ImageView) findViewById(R.id.designerinfo_auth);
        produceText = (TextView) findViewById(R.id.produceText);
        head_img = (ImageView) findViewById(R.id.head_img);
        nameText = (TextView) findViewById(R.id.name_text);
        //---------------------------------------------
        Intent intent = this.getIntent();
        Bundle productBundle = intent.getExtras();
        productid = productBundle.getString(Global.PRODUCT_ID);
        LogTool.d(TAG, "productid=" + productid);
        getProductHomePageInfo(productid);
    }

    private void getProductHomePageInfo(String productid) {
        JianFanJiaClient.getProductHomePage(DesignerCaseInfoActivity.this, productid, this, this);
    }

    @Override
    public void setListener() {
        designerinfo_head_img.setOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(this);
        activity_case_info_top_layout.setOnClickListener(this);
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
            case R.id.top_info_layout:
                Bundle designerInfoBundle = new Bundle();
                designerInfoBundle.putString(Global.DESIGNER_ID, designertid);
                startActivity(DesignerInfoActivity.class, designerInfoBundle);
                break;
            default:
                break;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticaloffset) {
        if (verticaloffset == 0) {
            if (state != State.COLLAPSED) {
                activity_case_info_top_layout.setVisibility(View.INVISIBLE);
            }
            state = State.COLLAPSED;
        } else if (Math.abs(verticaloffset) >= appBarLayout.getTotalScrollRange()) {
            if (state != State.EXPANDED) {
                activity_case_info_top_layout.setVisibility(View.VISIBLE);
            }
            state = State.EXPANDED;
        } else {
            if (state != State.IDLE) {
                activity_case_info_top_layout.setVisibility(View.INVISIBLE);
            }
            state = State.IDLE;
        }
    }

    @Override
    public void preLoad() {
        showWaitDialog(R.string.loding);
        collapsingToolbar.setTitle("");
    }

    @Override
    public void loadSuccess(Object data) {
        super.loadSuccess(data);
        LogTool.d(TAG, "data:" + data);
        hideWaitDialog();
        parseResponse(data.toString());
    }

    private void parseResponse(String response) {
        DesignerCaseInfo designerCaseInfo = JsonParser.jsonToBean(response, DesignerCaseInfo.class);
        LogTool.d(TAG, "designerCaseInfo" + designerCaseInfo);
        if (null != designerCaseInfo) {
            designertid = designerCaseInfo.getDesigner().get_id();
            collapsingToolbar.setTitle(designerCaseInfo.getCell());
            stylelText.setText(designerCaseInfo.getHouse_area() + "㎡，" + getHouseType(designerCaseInfo.getHouse_type()) + "，" + getDecStyle(designerCaseInfo.getDec_type()));
            imageShow.displayScreenWidthThumnailImage(this, designerCaseInfo.getDesigner().getImageid(), designerinfo_head_img);
            imageShow.displayScreenWidthThumnailImage(this, designerCaseInfo.getDesigner().getImageid(), head_img);
            produceText.setText("设计简介:" + designerCaseInfo.getDescription());
            nameText.setText(designerCaseInfo.getDesigner().getUsername());
            DesignerCaseAdapter adapter = new DesignerCaseAdapter(DesignerCaseInfoActivity.this, designerCaseInfo.getImages());
            designer_case_listview.setAdapter(adapter);
        }
    }

    @Override
    public void loadFailture(String error_msg) {
        hideWaitDialog();
        makeTextLong(error_msg);
        collapsingToolbar.setTitle("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    private enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_case_info;
    }


}
