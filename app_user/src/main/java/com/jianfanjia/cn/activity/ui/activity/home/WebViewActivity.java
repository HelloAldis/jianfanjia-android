package com.jianfanjia.cn.activity.ui.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.cn.activity.config.Global;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.activity.config.Url_New;
import com.jianfanjia.cn.activity.tools.JavaScriptObject;
import com.jianfanjia.cn.activity.tools.ShareUtil;
import com.jianfanjia.cn.activity.tools.UiHelper;
import com.jianfanjia.cn.activity.view.MainHeadView;
import com.jianfanjia.cn.activity.view.webview.ProgressWebView;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;


/**
 * Description:装修攻略
 * Author：Aldis.Zhan
 */
public class WebViewActivity extends BaseSwipeBackActivity {

    private static final String TAG = WebViewActivity.class.getName();
    private ShareUtil shareUtil = null;
    private JavaScriptObject javaScriptObject = null;

    @Bind(R.id.webView)
    protected ProgressWebView progressWebView = null;

    @Bind(R.id.my_contract_head_layout)
    protected MainHeadView mainHeadView = null;

    @Bind(R.id.toolbar_share)
    protected ImageView toolbar_share = null;

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

        this.shareUtil = new ShareUtil(this);

    }

    private void initMainHeadView() {
        mainHeadView.setMianTitle(this.getWebTitle());
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.VISIBLE);
    }

    @OnClick({R.id.head_back_layout, R.id.toolbar_share_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.head_back_layout:
                this.goBackOrQuit();
                break;
            case R.id.toolbar_share_layout:
                UiHelper.imageButtonAnim(toolbar_share, null);
                showPopwindow();
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must icon_add this**/
        UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void goBackOrQuit() {
        if (this.progressWebView.canGoBack()) {
            progressWebView.goBack();
        } else {
            appManager.finishActivity(this);
        }
    }

    private void showPopwindow() {
        shareUtil.shareUrl(this, this.javaScriptObject.getImageUrl(), this.getWebTitle(), this.javaScriptObject
                .getDescription(), this.progressWebView.getUrl(), new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                LogTool.d(TAG, "status =" + i);
            }
        });
    }

    private String getUrlFromIntent() {
        Intent intent = this.getIntent();
        String url = intent.getStringExtra(Global.WEB_VIEW_URL);
        if (!TextUtils.isEmpty(url)) {
            LogTool.d(TAG, url);
        }
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