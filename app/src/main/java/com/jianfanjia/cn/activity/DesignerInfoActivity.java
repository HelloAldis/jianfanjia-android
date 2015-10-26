package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.DesignerInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.fragment.DesignerInfoFragment;
import com.jianfanjia.cn.fragment.DesignerWorksFragment;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;

/**
 * Description:设计师信息
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class DesignerInfoActivity extends BaseActivity implements
        OnCheckedChangeListener, OnClickListener {
    private static final String TAG = DesignerInfoActivity.class.getName();
    private RadioGroup mTabRadioGroup = null;
    private Toolbar toolbar = null;
    private CollapsingToolbarLayout collapsingToolbar = null;
    private ImageView designerinfo_head_img = null;
    private TextView viewCountText = null;
    private TextView productCountText = null;
    private TextView appointCountText = null;
    private Button addBtn = null;
    private static final int DESIGNER_INFO = 0;
    private static final int DESIGNER_WORK = 1;
    private DesignerInfoFragment infoFragment = null;
    private DesignerWorksFragment workFragment = null;
    private String designerid = null;


    @Override
    public void initView() {
        mTabRadioGroup = (RadioGroup) findViewById(R.id.tab_rg_layout);
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
        designerinfo_head_img = (ImageView) findViewById(R.id.designerinfo_head_img);
        viewCountText = (TextView) findViewById(R.id.viewCountText);
        productCountText = (TextView) findViewById(R.id.productCountText);
        appointCountText = (TextView) findViewById(R.id.appointCountText);
        addBtn = (Button) findViewById(R.id.btn_add);
        //---------------------------------------------
        Intent intent = this.getIntent();
        Bundle designerBundle = intent.getExtras();
        designerid = designerBundle.getString(Global.DESIGNER_ID);
        LogTool.d(TAG, "designerid=" + designerid);
        getDesignerPageInfo(designerid);

        setTabSelection(DESIGNER_INFO);
    }

    @Override
    public void setListener() {
        mTabRadioGroup.setOnCheckedChangeListener(this);
        addBtn.setOnClickListener(this);
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
            case R.id.btn_add:
                addFavoriteDesignerToList(designerid);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.tab_designer_info:
                setTabSelection(DESIGNER_INFO);
                break;
            case R.id.tab_designer_works:
                setTabSelection(DESIGNER_WORK);
                break;
            default:
                break;
        }
    }

    private void getDesignerPageInfo(String designerid) {
        JianFanJiaClient.getDesignerHomePage(DesignerInfoActivity.this, designerid, designerHomePage, this);
    }

    private void addFavoriteDesignerToList(String designerid) {
        JianFanJiaClient.Add_Favorite_Designer_List(DesignerInfoActivity.this, designerid, addFavoriteDesigner, this);
    }

    private ApiUiUpdateListener designerHomePage = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data);
            DesignerInfo designerInfo = JsonParser.jsonToBean(data.toString(), DesignerInfo.class);
            LogTool.d(TAG, "designerInfo:" + designerInfo);
            if (null != designerInfo) {
                collapsingToolbar.setTitle(designerInfo.getUsername());
                imageLoader.displayImage(Url_New.GET_IMAGE + designerInfo.getImageid(), designerinfo_head_img, options);
                viewCountText.setText("" + designerInfo.getView_count());
                productCountText.setText("" + designerInfo.getProduct_count());
                appointCountText.setText("" + designerInfo.getOrder_count());
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    private ApiUiUpdateListener addFavoriteDesigner = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            makeTextLong(data.toString());
            addBtn.setText("已添加意向");
            addBtn.setEnabled(false);
        }

        @Override
        public void loadFailture(String error_msg) {

        }
    };

    private void setTabSelection(int index) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case DESIGNER_INFO:
                if (infoFragment != null) {
                    transaction.show(infoFragment);
                } else {
                    infoFragment = new DesignerInfoFragment();
                    Bundle designerBundle = new Bundle();
                    designerBundle.putString(Global.DESIGNER_ID, designerid);
                    infoFragment.setArguments(designerBundle);
                    transaction.add(R.id.contentLayout, infoFragment);
                }
                break;
            case DESIGNER_WORK:
                if (workFragment != null) {
                    transaction.show(workFragment);
                } else {
                    workFragment = new DesignerWorksFragment();
                    Bundle designerBundle = new Bundle();
                    designerBundle.putString(Global.DESIGNER_ID, designerid);
                    workFragment.setArguments(designerBundle);
                    transaction.add(R.id.contentLayout, workFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (infoFragment != null) {
            ft.hide(infoFragment);
        }
        if (workFragment != null) {
            ft.hide(workFragment);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_designer_info;
    }
}