package com.jianfanjia.cn.activity.home;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.config.Url_New;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.tools.ShareUtil;
import com.jianfanjia.cn.tools.StringUtils;
import com.jianfanjia.cn.tools.UiHelper;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.cn.view.ProgressWebView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


/**
 * Description:装修攻略
 * Author：Aldis.Zhan
 */
@EActivity(R.layout.activity_dec_strategy)
public class DecStrategyActivity extends SwipeBackActivity implements View.OnKeyListener {

    private static final String TAG = DecStrategyActivity.class.getName();
    private ShareUtil shareUtil = null;
    private String description = null;
    private String imageUrl = null;

    @ViewById(R.id.webView)
    protected ProgressWebView progressWebView = null;

    @ViewById(R.id.my_contract_head_layout)
    protected MainHeadView mainHeadView = null;

    @ViewById(R.id.toolbar_share)
    protected ImageView toolbar_share = null;

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
        progressWebView.loadUrl(Url_New.getInstance().MOBILE_SERVER_URL + "/view/article/");
        progressWebView.addJavascriptInterface( new InJavaScriptLocalObj(), "local_obj");
        progressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                LogTool.d("WebView", "onPageFinished ");
                DecStrategyActivity.this.description = null;
                DecStrategyActivity.this.imageUrl = null;
                view.loadUrl("javascript:var meta = document.getElementsByTagName('meta');\n" +
                                "for (i in meta) {\n" +
                                "  if (typeof meta[i].name!=\"undefined\" && meta[i].name.toLowerCase()==\"description\") {\n" +
                                "    window.local_obj.description(meta[i].content);\n" +
                                "  }\n" +
                                "}");
                view.loadUrl("javascript:window.local_obj.imageUrl(document.getElementsByTagName('img')[0].src);");
                super.onPageFinished(view, url);

                mainHeadView.setMianTitle(DecStrategyActivity.this.getWebTitle());
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

    @Click({R.id.head_back_layout, R.id.toolbar_share_layout})
    protected void click(View view){
        switch (view.getId()){
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
            case R.id.toolbar_share_layout:
                UiHelper.imageButtonAnim(toolbar_share, null);
                showPopwindow();
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

    private void showPopwindow() {
        shareUtil.shareUrl(this, this.getImageUrl(), this.getWebTitle(), this.getDescription(), this.progressWebView.getUrl(), new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, SocializeEntity socializeEntity) {
                LogTool.d(TAG, "status =" + i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must icon_add this**/
        UMSsoHandler ssoHandler = SocializeConfig.getSocializeConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void description(String description) {
            LogTool.d(TAG, description);
            DecStrategyActivity.this.description = description;
        }

        @JavascriptInterface
        public void imageUrl(String imageUrl) {
            LogTool.d(TAG, imageUrl);
            DecStrategyActivity.this.imageUrl = imageUrl;
        }
    }

    private String getWebTitle() {
        if (StringUtils.isEmpty(this.progressWebView.getTitle())) {
            return getResources().getString(R.string.home_tag1);
        } else {
            return this.progressWebView.getTitle();
        }
    }

    private String getDescription() {
        if (StringUtils.isEmpty(this.description)) {
            return "default description";
        } else {
            return this.description;
        }
    }

    private String getImageUrl() {
        if (StringUtils.isEmpty(this.imageUrl)) {
            return  "http://www.jianfanjia.com/static/img/public/logo.png";
        } else {
            return this.imageUrl;
        }
    }
}