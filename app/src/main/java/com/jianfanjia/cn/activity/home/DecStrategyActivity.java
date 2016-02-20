package com.jianfanjia.cn.activity.home;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.ProgressWebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * Description:装修攻略
 * Author：Aldis.Zhan
 */
@EActivity(R.layout.activity_dec_strategy)
public class DecStrategyActivity extends SwipeBackActivity implements View.OnClickListener, View.OnKeyListener {

    @ViewById(R.id.webView)
    protected ProgressWebView progressWebView = null;

    @ViewById(R.id.my_contract_head_layout)
    protected MainHeadView mainHeadView = null;

    @AfterViews
    void setView() {
        super.initView();
        this.initMainHeadView();

        progressWebView.getSettings().setJavaScriptEnabled(true);
        progressWebView.getSettings().setSupportZoom(true);
        progressWebView.getSettings().setBuiltInZoomControls(true);
        progressWebView.getSettings().setUseWideViewPort(true);
        progressWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        progressWebView.getSettings().setLoadWithOverviewMode(true);
        progressWebView.loadUrl("http://devm.jianfanjia.com/view/article/");
        progressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private void initMainHeadView() {
        mainHeadView.setBackListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string.home_tag1));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            if (keyCode == KeyEvent.KEYCODE_BACK && progressWebView.canGoBack()) {  //表示按返回键时的操作
                progressWebView.goBack();   //后退
                return true;    //已处理
            }
        }
        return false;
    }
}
