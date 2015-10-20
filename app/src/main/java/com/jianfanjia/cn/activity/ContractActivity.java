package com.jianfanjia.cn.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jianfanjia.cn.base.BaseActivity;

/**
 * Description:合同查看
 * Author：fengliang
 * Email：leo.feng@myjyz.com
 * Date:15-10-11 14:30
 */
public class ContractActivity extends BaseActivity {
    private WebView webview = null;
    private WebSettings mWebSettings;


    @Override
    public void initView() {
        webview = (WebView) this.findViewById(R.id.webview);
        webview.loadUrl("http://developer.android.com/");
        mWebSettings = webview.getSettings();
        mWebSettings.setJavaScriptEnabled(true);    //允许加载javascript
        //mWebSettings.setSupportZoom(true);          //允许缩放
        //mWebSettings.setBuiltInZoomControls(true);
        //mWebSettings.setUseWideViewPort(true);      //任意比例缩放

        mWebSettings.setUseWideViewPort(true);      //设置加载进来的页面自适应手机屏幕（可缩放）
        mWebSettings.setLoadWithOverviewMode(true);
    }

    @Override
    public void setListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contract;
    }
}
