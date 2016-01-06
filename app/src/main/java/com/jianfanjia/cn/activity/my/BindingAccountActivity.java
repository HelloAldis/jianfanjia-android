package com.jianfanjia.cn.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jianfanjia.cn.Event.BindingPhoneEvent;
import com.jianfanjia.cn.activity.R;
import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.OwnerInfo;
import com.jianfanjia.cn.bean.WeiXinRegisterInfo;
import com.jianfanjia.cn.config.Constant;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.AuthUtil;
import com.jianfanjia.cn.tools.LogTool;
import com.jianfanjia.cn.view.MainHeadView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Description: com.jianfanjia.cn.activity.my
 * Author: zhanghao
 * Email: jame.zhang@myjyz.com
 * Date:2016-01-05 11:51
 */
@EActivity(R.layout.activity_binding_account)
public class BindingAccountActivity extends BaseAnnotationActivity{

    @ViewById(R.id.bindingaccount_head_layout)
    MainHeadView mainHeadView;

    @ViewById(R.id.bindingaccount_weixin_layout)
    RelativeLayout bindingaccount_weixin_layout;

    @ViewById(R.id.bindingaccount_phone_layout)
    RelativeLayout bindingaccount_phone_layout;

    @ViewById(R.id.bindingaccount_phone_content)
    TextView bindingaccount_phoneText;

    @ViewById(R.id.bindingaccount_weixin_content)
    TextView bindingaccount_wexinText;

    private String phone;

    private OwnerInfo ownerInfo;

    private AuthUtil authUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        authUtil = AuthUtil.getInstance(this);
    }

    @AfterViews
    protected void afterView(){
        mainHeadView.setMianTitle(getString(R.string.account_binding));

        Intent intent = getIntent();
        ownerInfo = (OwnerInfo)intent.getSerializableExtra(Constant.OWNER_INFO);

        phone = ownerInfo.getPhone();
        if(phone != null){
            bindingaccount_phone_layout.setEnabled(false);
            bindingaccount_phoneText.setText(phone);
        }else{
            bindingaccount_phone_layout.setEnabled(true);
        }

        if(ownerInfo != null && ownerInfo.getWechat_unionid() != null){
            bindingaccount_weixin_layout.setEnabled(false);
            bindingaccount_wexinText.setText(getString(R.string.already_binding));
        }else{
            bindingaccount_weixin_layout.setEnabled(true);
        }


    }

    @Click({R.id.head_back_layout,R.id.bindingaccount_phone_layout,R.id.bindingaccount_weixin_layout})
    protected void click(View view){
        switch (view.getId()){
            case R.id.bindingaccount_weixin_layout:
                SHARE_MEDIA platform = SHARE_MEDIA.WEIXIN;
                authUtil.doOauthVerify(this,platform,umAuthListener);
                break;
            case R.id.bindingaccount_phone_layout:
                startActivity(BindingPhoneActivity_.class);
                break;
            case R.id.head_back_layout:
                appManager.finishActivity(this);
                break;
        }
    }

    public void onEventMainThread(BindingPhoneEvent bindingPhoneEvent) {
        if(TextUtils.isEmpty(phone = bindingPhoneEvent.getPhone())) return;
        LogTool.d(this.getClass().getName(), "event:" + bindingPhoneEvent.getPhone());
        bindingaccount_phoneText.setText(phone);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data!=null){
                LogTool.d(this.getClass().getName(),data.toString());
                authUtil.getPlatformInfo(BindingAccountActivity.this,SHARE_MEDIA.WEIXIN,umAuthInfoListener);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    private UMAuthListener umAuthInfoListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data != null){
                LogTool.d(this.getClass().getName(), data.toString());
                WeiXinRegisterInfo weiXinRegisterInfo = new WeiXinRegisterInfo();
                weiXinRegisterInfo.setUsername(data.get("nickname"));
                weiXinRegisterInfo.setImage_url(data.get("headimgurl"));
                weiXinRegisterInfo.setSex(data.get("sex"));
                weiXinRegisterInfo.setWechat_openid(data.get("openid"));
                weiXinRegisterInfo.setWechat_unionid(data.get("unionid"));

                JianFanJiaClient.bindingWeixin(BindingAccountActivity.this, weiXinRegisterInfo.getWechat_openid(), weiXinRegisterInfo.getWechat_unionid(), new ApiUiUpdateListener() {
                    @Override
                    public void preLoad() {

                    }

                    @Override
                    public void loadSuccess(Object data) {
                        bindingaccount_wexinText.setText(getString(R.string.already_binding));
                        bindingaccount_weixin_layout.setEnabled(false);
                    }

                    @Override
                    public void loadFailture(String error_msg) {

                    }
                },BindingAccountActivity.this);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
}
