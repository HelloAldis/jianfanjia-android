package com.jianfanjia.cn.supervisor.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.supervisor.R;
import com.jianfanjia.cn.supervisor.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.supervisor.config.Global;
import com.jianfanjia.cn.supervisor.config.Url_New;
import com.jianfanjia.cn.tools.JavaScriptObject;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.ProgressWebView;
import com.jianfanjia.common.tool.LogTool;


/**
 * Description:装修攻略
 * Author：Aldis.Zhan
 */
public class WebViewActivity extends BaseSwipeBackActivity {

    private static final String TAG = WebViewActivity.class.getName();
    private JavaScriptObject javaScriptObject = null;

    @Bind(R.id.webView)
    protected ProgressWebView progressWebView = null;

    @Bind(R.id.my_contract_head_layout)
    protected MainHeadView mainHeadView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    public void initView() {
        this.initMainHeadView();

        progressWebView.getSettings().setJavaScriptEnabled(true);
        progressWebView.getSettings().setSupportZoom(true);
        progressWebView.getSettings().setBuiltInZoomControls(true);
        progressWebView.getSettings().setUseWideViewPort(true);
        progressWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        progressWebView.getSettings().setLoadWithOverviewMode(true);
        progressWebView.loadUrl(Url_New.getInstance().MOBILE_SERVER_URL + this.getUrlFromIntent());
        this.javaScriptObject = new JavaScriptObject();
        this.javaScriptObject.injectIntoWebView(this.progressWebView);

        progressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                LogTool.d("WebView", "onPageFinished ");
                WebViewActivity.this.javaScriptObject.runGetDescriptionCode(view);
                WebViewActivity.this.javaScriptObject.runGetImageCode(view);
                super.onPageFinished(view, url);

                mainHeadView.setMianTitle(WebViewActivity.this.getWebTitle());
            }
        });

    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(this.getWebTitle());
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                this.goBackOrQuit();
                break;
            default:
                break;
        }
    }


    private void goBackOrQuit() {
        if (this.progressWebView.canGoBack()) {
            progressWebView.goBack();
        } else {
            appManager.finishActivity(this);
        }
    }

    private String getUrlFromIntent() {
        Intent intent = this.getIntent();
        String url = intent.getStringExtra(Global.WEB_VIEW_URL);
        LogTool.d(TAG, url);
        return url;
    }

    private String getWebTitle() {
        if (TextUtils.isEmpty(this.progressWebView.getTitle())) {
            return "";
        } else {
            return this.progressWebView.getTitle();
        }
    }
}