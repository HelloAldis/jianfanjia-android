package com.jianfanjia.cn.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import com.aldis.hud.Hud;
import com.jianfanjia.api.ApiCallback;
import com.jianfanjia.api.ApiResponse;
import com.jianfanjia.api.HttpCode;
import com.jianfanjia.api.request.user.BindingWeiXinRequest;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.api.Api;
import com.jianfanjia.cn.base.BaseSwipeBackActivity;
import com.jianfanjia.cn.constant.IntentConstant;
import com.jianfanjia.cn.tools.AuthUtil;
import com.jianfanjia.cn.ui.Event.BindingPhoneEvent;
import com.jianfanjia.cn.view.MainHeadView;
import com.jianfanjia.common.tool.LogTool;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.sso.UMSsoHandler;
import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.my
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-05 11:51
 */
public class BindingAccountActivity extends BaseSwipeBackActivity {
    private static final String TAG = BindingAccountActivity.class.getName();

    @Bind(R.id.bindingaccount_head_layout)
    MainHeadView mainHeadView;

    @Bind(R.id.bindingaccount_weixin_layout)
    RelativeLayout bindingaccount_weixin_layout;

    @Bind(R.id.bindingaccount_phone_layout)
    RelativeLayout bindingaccount_phone_layout;

    @Bind(R.id.bindingaccount_phone_content)
    TextView bindingaccount_phoneText;

    @Bind(R.id.bindingaccount_weixin_content)
    TextView bindingaccount_wexinText;

    @Bind(R.id.bindingaccount_phone_goto)
    ImageView bindingaccount_phone_goto;

    @Bind(R.id.bindingaccount_weixin_goto)
    ImageView bindingaccount_weixin_goto;

    @Bind(R.id.weixin_layout_line)
    View weixin_layout_line;

    private String phone;

    private AuthUtil authUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        authUtil = AuthUtil.getInstance(this);

        initView();
    }

    public void initView() {
        mainHeadView.setMianTitle(getString(R.string.account_binding));

        phone = dataManager.getAccount();
        if (phone != null) {
            bindingaccount_phone_layout.setEnabled(false);
            bindingaccount_phoneText.setText(phone);
            bindingaccount_phone_goto.setVisibility(View.GONE);
            bindingaccount_weixin_layout.setVisibility(View.GONE);
            weixin_layout_line.setVisibility(View.GONE);
        } else {
            bindingaccount_weixin_layout.setVisibility(View.VISIBLE);
            weixin_layout_line.setVisibility(View.VISIBLE);
            bindingaccount_phone_layout.setEnabled(true);
            bindingaccount_phoneText.setText(getString(R.string.not_binding));
            bindingaccount_phone_goto.setVisibility(View.VISIBLE);
        }

        if (dataManager.getWechat_unionid() != null) {
            bindingaccount_weixin_layout.setEnabled(false);
            bindingaccount_wexinText.setText(getString(R.string.already_binding));
            bindingaccount_weixin_goto.setVisibility(View.GONE);
        } else {
            bindingaccount_weixin_layout.setEnabled(true);
            bindingaccount_wexinText.setText(getString(R.string.not_binding));
            bindingaccount_weixin_goto.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.head_back_layout, R.id.bindingaccount_phone_layout, R.id.bindingaccount_weixin_layout})
    protected void click(View view) {
        switch (view.getId()) {
            case R.id.bindingaccount_weixin_layout:
                SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
                authUtil.doOauthVerify(this, platform, umDataListener);
                break;
            case R.id.bindingaccount_phone_layout:
                Bundle bundle = new Bundle();
                bundle.putInt(IntentConstant.BINDING_PHONE_INTENT, IntentConstant.BINDING_PHONE_USERINFO);
                startActivity(BindingPhoneActivity.class, bundle);
                overridePendingTransition(R.anim.slide_and_fade_in_from_bottom, R.anim.fade_out);
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    public void onEventMainThread(BindingPhoneEvent bindingPhoneEvent) {
        if (TextUtils.isEmpty(phone = bindingPhoneEvent.getPhone())) return;
        LogTool.d("event:" + bindingPhoneEvent.getPhone());
        bindingaccount_phoneText.setText(phone);
        bindingaccount_phone_goto.setVisibility(View.GONE);
    }

    private SocializeListeners.UMDataListener umDataListener = new SocializeListeners.UMDataListener() {
        @Override
        public void onStart() {

        }


        @Override
        public void onComplete(int i, Map<String, Object> data) {
            LogTool.d("i:" + i + " data:" + data);
            if (i == 200 && data != null) {
                bindWinxin(data.get("openid").toString(), data.get("unionid").toString());
            } else {
                Hud.dismiss();
                makeTextShort(getString(R.string.authorize_fail));
            }
        }
    };


    private void bindWinxin(String openid, String unionid) {
        BindingWeiXinRequest request = new BindingWeiXinRequest();
        request.setOpenid(openid);
        request.setUnionid(unionid);
        Api.bindingWeixin(request, new ApiCallback<ApiResponse<String>>() {
            @Override
            public void onPreLoad() {
                Hud.show(getUiContext());
            }

            @Override
            public void onHttpDone() {
                Hud.dismiss();
            }

            @Override
            public void onSuccess(ApiResponse<String> apiResponse) {
                bindingaccount_wexinText.setText(getString(R.string.already_binding));
                bindingaccount_weixin_layout.setEnabled(false);
                bindingaccount_weixin_goto.setVisibility(View.GONE);
            }

            @Override
            public void onFailed(ApiResponse<String> apiResponse) {
                makeTextShort(apiResponse.getErr_msg());
            }

            @Override
            public void onNetworkError(int code) {
                makeTextShort(HttpCode.getMsg(code));
            }
        },this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogTool.d("onActivityResult");
        UMSsoHandler ssoHandler = authUtil.getUmSocialService().getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_binding_account;
    }
}
