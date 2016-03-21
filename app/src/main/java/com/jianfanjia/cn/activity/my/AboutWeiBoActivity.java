package com.jianfanjia.cn.activity.my;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.activity.requirement.ContractActivity;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.activity.my
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-03-21 17:32
 */
public class AboutWeiBoActivity extends SwipeBackActivity implements View.OnClickListener, View.OnKeyListener {

    private static final String TAG = ContractActivity.class.getName();

    private MainHeadView mainHeadView = null;
    private WebView webView = null;

    @Override
    public void initView() {

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
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm
                .SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(Url_New.WEIBO_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void initMainHeadView() {
        mainHeadView = (MainHeadView) findViewById(R.id
                .my_contract_head_layout);
        mainHeadView.setBackListener(this);
        mainHeadView.setRightTextListener(this);
        mainHeadView.setMianTitle(getResources().getString(R.string
                .app_name));
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
        return R.layout.activity_follow_weibo;
    }
}
