package com.jianfanjia.cn.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jianfanjia.cn.base.BaseAnnotationActivity;
import com.jianfanjia.cn.bean.RegisterInfo;
import com.jianfanjia.cn.config.Global;
import com.jianfanjia.cn.http.JianFanJiaClient;
import com.jianfanjia.cn.interf.ApiUiUpdateListener;
import com.jianfanjia.cn.tools.NetTool;
import com.jianfanjia.cn.tools.UiHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_forget_psw)
public class ForgetPswActivity extends BaseAnnotationActivity{
    private static final String TAG = ForgetPswActivity.class.getClass()
            .getName();
    @ViewById(R.id.act_register_input_phone)
    EditText mEtForgetPswUserName = null;// 注册用户名输入框
    @ViewById(R.id.act_register_input_password)
    EditText mEtForgetPswPassword = null;// 注册用户密码输入框
    @ViewById(R.id.btn_next)
    Button mBtnNext;
    @ViewById(R.id.register_layout)
    RelativeLayout registerLayout;
    private String mUserName = null;// 用户名
    private String mPassword = null;// 密码


    @Click({R.id.head_back_layout,R.id.btn_next})
    void onClick(View view){
        int resId = view.getId();
        switch (resId){
            case R.id.head_back_layout:
                finish();
                break;
            case R.id.btn_next:
                mUserName = mEtForgetPswUserName.getText().toString().trim();
                mPassword = mEtForgetPswPassword .getText().toString().trim();
                if(checkRegisterInput(mUserName,mPassword)){
                    sendVerification(mUserName,mPassword);
                }
                break;
        }
    }

    private boolean checkRegisterInput(String name, String password) {
        if (TextUtils.isEmpty(name)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_username));
            mEtForgetPswUserName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            makeTextShort(getResources().getString(
                    R.string.tip_please_input_password));
            mEtForgetPswPassword.requestFocus();
            return false;
        }
        return true;
    }

    @AfterViews
    void initUi(){
        UiHelper.controlKeyboardLayout(registerLayout, mBtnNext);
    }

    /**
     * 发送验证码
     * @param name
     * @param password
     */
    private void sendVerification(final String name, final String password) {
        if (NetTool.isNetworkAvailable(this)) {
            JianFanJiaClient.send_verification(this, name, new ApiUiUpdateListener() {
                @Override
                public void preLoad() {

                }

                @Override
                public void loadSuccess(Object data) {
                    RegisterInfo registerInfo = new RegisterInfo();
                    registerInfo.setPass(password);
                    registerInfo.setPhone(name);
                    Bundle registerBundle = new Bundle();
                    registerBundle.putSerializable(Global.REGISTER_INFO, registerInfo);
                    registerBundle.putInt(Global.REGISTER,RegisterNewActivity.UPDATE_PSW_CODE);
                    startActivity(RegisterNewActivity_.class, registerBundle);
                }

                @Override
                public void loadFailture(String error_msg) {
                    makeTextLong(error_msg);
                }
            }, this);
        } else {
            makeTextLong(getString(R.string.tip_internet_not));
        }
    }


}