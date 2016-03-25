package com.jianfanjia.cn.activity.my;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.view.MainHeadView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Description:客服
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class CustomerServiceActivity extends SwipeBackActivity implements View.OnKeyListener {
    private static final String TAG = CustomerServiceActivity.class.getName();

    @Bind(R.id.my_contract_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initMainHeadView();
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
        webView.setOnKeyListener(this);
    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(getResources().getString(R.string.kefuText));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick(R.id.head_back_layout)
    public void onClick() {
        appManager.finishActivity(this);
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