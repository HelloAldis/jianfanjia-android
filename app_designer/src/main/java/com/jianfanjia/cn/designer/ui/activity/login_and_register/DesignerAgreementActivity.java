package com.jianfanjia.cn.designer.ui.activity.login_and_register;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Bind;
import butterknife.OnClick;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.request.designer.DesignerAgreeRequest;
import com.jianfanjia.cn.designer.R;
import com.jianfanjia.cn.designer.api.Api;
import com.jianfanjia.cn.designer.base.BaseActivity;
import com.jianfanjia.cn.designer.config.Constant;
import com.jianfanjia.cn.designer.config.Url_New;
import com.jianfanjia.cn.designer.ui.activity.MainActivity;
import com.jianfanjia.cn.designer.ui.activity.my_info_auth.base_info.BaseInfoAuthActicity;
import com.jianfanjia.cn.designer.view.MainHeadView;

/**
 * Description: com.jianfanjia.cn.designer.ui.activity.login_and_register
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-05-31 09:19
 */
public class DesignerAgreementActivity extends BaseActivity implements View.OnKeyListener {

    private static final String TAG = DesignerAgreementActivity.class.getName();

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
        webView.loadUrl(Url_New.getInstance().AGREEMENT_LICENSE);
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
        mainHeadView.setMianTitle(getResources().getString(R.string.designer_agreement));
        mainHeadView.setLayoutBackground(R.color.head_layout_bg);
        mainHeadView.setRightTitleVisable(View.GONE);
        mainHeadView.setBackLayoutVisable(View.GONE);
    }

    @OnClick(R.id.head_back_layout)
    public void onClick() {
        appManager.finishActivity(this);
    }

    @OnClick(R.id.btn_agree)
    protected void agree() {
        DesignerAgreeRequest designerAgreeRequest = new DesignerAgreeRequest();
        Api.designerAgree(designerAgreeRequest, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                showWaitDialog();
            }

            @Override
            public void onHttpDone() {
                hideWaitDialog();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                dataManager.setLogin(true);
                navigateNext();
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {

            }
        });
    }

    private void navigateNext() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra(MainActivity.TAB_POSITION, Constant.MORE);

        Intent baseInfoIntent = new Intent(this, BaseInfoAuthActicity.class);
        baseInfoIntent.putExtra(BaseInfoAuthActicity.INTENT_FROM_FLAG, BaseInfoAuthActicity.FROM_REGISTER_INTENT);

        Intent[] intents = new Intent[]{mainIntent, baseInfoIntent};
        startActivities(intents);
        appManager.finishActivity(DesignerAgreementActivity.class);
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
        return R.layout.activity_designer_agreement;
    }
}
