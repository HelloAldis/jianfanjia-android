package layout.activity.home;

import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.activity.SwipeBackActivity;
import com.jianfanjia.cn.config.Global;
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
@EActivity(R.layout.activity_web_view)
public class WebViewActivity extends SwipeBackActivity {

    private static final String TAG = WebViewActivity.class.getName();
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
        progressWebView.loadUrl(Url_New.getInstance().MOBILE_SERVER_URL + this.getUrlFromIntent());
        progressWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        progressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                LogTool.d("WebView", "onPageFinished ");
                WebViewActivity.this.description = null;
                WebViewActivity.this.imageUrl = null;
                view.loadUrl("javascript:var meta = document.getElementsByTagName('meta');\n" +
                        "for (i in meta) {\n" +
                        "  if (typeof meta[i].name!=\"undefined\" && meta[i].name.toLowerCase()==\"description\") {\n" +
                        "    window.local_obj.description(meta[i].content);\n" +
                        "  }\n" +
                        "}");
                view.loadUrl("javascript:window.local_obj.imageUrl(document.getElementsByTagName('img')[0].src);");
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

    @Click({R.id.head_back_layout, R.id.toolbar_share_layout})
    protected void click(View view){
        switch (view.getId()){
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

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void description(String description) {
            LogTool.d(TAG, description);
            WebViewActivity.this.description = description;
        }

        @JavascriptInterface
        public void imageUrl(String imageUrl) {
            LogTool.d(TAG, imageUrl);
            WebViewActivity.this.imageUrl = imageUrl;
        }
    }

    private String getUrlFromIntent() {
        Intent intent = this.getIntent();
        String url = intent.getStringExtra(Global.WEB_VIEW_URL);
        return url;
    }

    private String getWebTitle() {
        if (StringUtils.isEmpty(this.progressWebView.getTitle())) {
            return "";
        } else {
            return this.progressWebView.getTitle();
        }
    }

    private String getDescription() {
        if (StringUtils.isEmpty(this.description)) {
            return "我在使用 #简繁家# 的App，业内一线设计师为您量身打造房间，比传统装修便宜20%，让你一手轻松掌控装修全过程。";
        } else {
            return this.description;
        }
    }

    private String getImageUrl() {
        if (StringUtils.isEmpty(this.imageUrl)) {
            return  "http://www.jianfanjia.com/static/img/design/head.jpg";
        } else {
            return this.imageUrl;
        }
    }
}