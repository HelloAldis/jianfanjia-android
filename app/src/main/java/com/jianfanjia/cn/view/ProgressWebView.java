package com.jianfanjia.cn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jianfanjia.cn.tools.LogTool;

/**
 * Name: ProgressWebView
 * User: fengliang
 * Date: 2015-10-26
 * Time: 11:15
 */
public class ProgressWebView extends WebView {
    private WebViewProgressBar progressBar;
    private Handler handler;
    private WebView _this;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressBar = new WebViewProgressBar(context);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setVisibility(GONE);
        addView(progressBar);
        handler = new Handler();
        _this = this;
        setWebChromeClient(new MyWebChromeClient());
        setWebViewClient(new MyWebClient());
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setProgress(100);
                handler.postDelayed(runnable, 200);
            } else if (progressBar.getVisibility() == GONE) {
                progressBar.setVisibility(VISIBLE);
            }
            if (newProgress < 5) {
                newProgress = 5;
            }
            progressBar.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            _this.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && this.canGoBack()) {  //表示按返回键时的操作
                this.goBack();
                return true;    //已处理
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
        }
    };
}