package com.jianfanjia.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.bean.ContractInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.JsonParser;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:合同查看
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ContractActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = ContractActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private WebView webView = null;
    private String requirementStatus = null;
    private String requirementid = null;
    private String final_planid = null;


    @Override
    public void initView() {
        Intent intent = this.getIntent();
        Bundle contractBundle = intent.getExtras();
        requirementStatus = contractBundle.getString(Global.REQUIREMENT_STATUS);
        requirementid = contractBundle.getString(Global.REQUIREMENT_ID);
        LogTool.d(TAG, "requirementStatus:" + requirementStatus + " requirementid:" + requirementid);
        initMainHeadView();
        webView = (WebView) findViewById(R.id.webView);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(Url_New.CONTRACT_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        getContractInfo(requirementid);
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_contract_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView
                .setMianTitle(getResources().getString(R.string.contractText));
        mainHeadView.setRightTitle(getResources().getString(R.string.comfirmText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.VISIBLE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
        if (requirementStatus.equals(Global.REQUIREMENT_STATUS5)) {
            mainHeadView.setRigthTitleEnable(false);
        } else {
            mainHeadView.setRigthTitleEnable(true);
        }
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
            case R.id.head_right_title:
                postUserProcess(requirementid, final_planid);
                break;
            default:
                break;
        }
    }

    //查看合同
    private void getContractInfo(String requirementid) {
        JianFanJiaClient.getContractInfo(ContractActivity.this, requirementid, getContractListener, this);
    }

    private ApiUiUpdateListener getContractListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            ContractInfo contractInfo = JsonParser.jsonToBean(data.toString(), ContractInfo.class);
            LogTool.d(TAG, "contractInfo:" + contractInfo);
            if (null != contractInfo) {
                final_planid = contractInfo.getFinal_planid();
                LogTool.d(TAG, "final_planid:" + final_planid);
            }
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    //确认开启工地
    private void postUserProcess(String requirementid, String final_planid) {
        LogTool.d(TAG, "requirementid=" + requirementid + "  final_planid=" + final_planid);
        JianFanJiaClient.post_Owner_Process(ContractActivity.this, requirementid, final_planid, postUserProcessListener, this);
    }


    private ApiUiUpdateListener postUserProcessListener = new ApiUiUpdateListener() {
        @Override
        public void preLoad() {

        }

        @Override
        public void loadSuccess(Object data) {
            LogTool.d(TAG, "data:" + data.toString());
            makeTextLong("确认成功");
        }

        @Override
        public void loadFailture(String error_msg) {
            makeTextLong(error_msg);
        }
    };

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contract;
    }
}
