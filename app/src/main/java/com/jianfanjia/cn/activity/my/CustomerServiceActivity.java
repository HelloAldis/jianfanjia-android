package com.jianfanjia.cn.activity.my;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description:客服
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {
    private static final String TAG = CustomerServiceActivity.class.getName();
    private MainHeadView mainHeadView = null;
    private WebView webView = null;

    @Override
    public void initView() {
        initMainHeadView();
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(Constant.HOTLINE_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id.my_contract_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.kefuText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }


    @Override
    public void setListener() {
        webView.setOnKeyListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK &&
                    webView.canGoBack()) {  //表示按返回键时的操作
                webView.goBack();   //后退
                return true;    //已处理
            }
        }
        return false;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_service;
    }
}